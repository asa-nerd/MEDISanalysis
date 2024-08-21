//  ------------------------------------------------------------
//  irmaANALYSIS V 0.2
//  Class to store subject data
//  Andreas Pirchner, 2018-2020
//  ------------------------------------------------------------

// Constants
// totalActivity, averageActivity

//  Functions

//  1. General 
//  getLength()
//  getRecName()

//  2. Calculate:
//  getPointsByIndex
//  getPointbyIndex
//  getPointsDistance
//  getMovementSum
//  getCurrentActivity

/// 3. Visualize:
//  drawActivity
//  drawDistanceOne
//  drawDistanceTwo
//  drawDistanceThree
//  drawSubject
//  ------------------------------------------------------------

package medis.MEDISanalysis;

import java.util.ArrayList;

import org.json.simple.*;
import math.geom2d.Point2D;

public class Subject {

	JSONArray JSONData;
	JSONArray subjectPoints;

	ArrayList<Point> PointsList = new ArrayList<Point>(); // Store measuring points of subject
	Point2D triOne, triTwo, triThree;
	long triangleSize = 400;
	double triHeight = triangleSize / 2 * Math.sqrt(3);
	long id;
	String recordName = "";
	long age;
	long gender;
	String dataModel;
	long dataDimensions;
	String education = "";
	
	long projectDataSetLength;
	long projectTimecodeLength;
	long projectTimecodeInterval;
	
	int subjectDataSetLength;
	
	double missingXdata = 0.0;				// the default data that is painted over gaps and missing data
	double missingYdata = 0.0;
	double missingButtonData = 1.0;

	double subjectActivitySum = 0;			// total activity 
	double subjectActivityCount = 0;		// sum of timecode positions in which a movement occurred 
	double subjectActivityAverage = 0;		// average distance per movement
	double subjectActivityTotalAverage = 0;	// average distance for whole dataset
	
	
	Subject(JSONArray _JSONData) {
		JSONData = _JSONData;

		JSONObject u = (JSONObject) JSONData.get(0);
		id = (long) u.get("id");

		recordName = (String) u.get("recording");
		JSONObject persBackground = (JSONObject) u.get("personalBackground");
		if (u.containsKey("tcLength")) 	{ 	projectTimecodeLength = (long) u.get("tcLength");}
		if (u.containsKey("tcInterval")){	projectTimecodeInterval = (long) u.get("tcInterval");}
		if (u.containsKey("datamodel")) {	dataModel = (String) u.get("datamodel");}
		if (u.containsKey("dimensions")){	dataDimensions = (long) u.get("dimensions");}
		if (persBackground.containsKey("Age")) { age = (long) persBackground.get("Age");}
		if (persBackground.containsKey("Gender")) { gender = (long) persBackground.get("Gender");}
		if (persBackground.containsKey("Education")) { education = (String) persBackground.get("Education");}
		projectDataSetLength = projectTimecodeLength/projectTimecodeInterval;
		
		JSONObject timeseries = (JSONObject) JSONData.get(1);
		subjectPoints = (JSONArray) timeseries.get("timesequence");

		triOne = new Point2D(-triangleSize / 2, triHeight / 3);
		triTwo = new Point2D(triangleSize / 2, triHeight / 3);
		triThree = new Point2D(0, -(triHeight / 3 * 2));

		subjectDataSetLength = subjectPoints.size();

		long tcIteratorCounter = projectTimecodeInterval;			// don't start at 0 but at the first recorded timecode (e.g. 500)

		
		for (int i = 0; i < projectDataSetLength-1; i++) {			// iterate through timecodes of project and match measured data to timecode
			matchDataPoint(tcIteratorCounter);
			tcIteratorCounter = tcIteratorCounter+projectTimecodeInterval;
		}
		
		for (int i = 1; i < PointsList.size(); i++) { 				// calculate activity for Points and save them in the
																	// Point-Objects
		/*int startOFF = 18;
		int endOFF = 1026;
		for (int i = startOFF; i<=endOFF; i++){*/
			Point lastPoint = PointsList.get(i - 1);
			Point currentPoint = PointsList.get(i);
			
			double activity = Point2D.distance(lastPoint.getPoint(), currentPoint.getPoint());
			currentPoint.activity = activity;
			if (activity > 0) 
				subjectActivityCount ++;
			subjectActivitySum += activity;
		}
		
		subjectActivityAverage = subjectActivitySum/subjectActivityCount;
		subjectActivityTotalAverage = subjectActivitySum / PointsList.size();
	}
	
	public void updateData(int _begin, int _end) {
		int range = _end - _begin;
		
		subjectActivitySum = 0;
		subjectActivityCount = 0;
		
		for (int i = _begin+1; i < _end-1; i++) { 
			Point currentPoint = PointsList.get(i);
			if (currentPoint.activity > 0) 
				subjectActivityCount ++;
			subjectActivitySum += currentPoint.activity;
		}
		
		if (subjectActivityCount > 0) {
			subjectActivityAverage = subjectActivitySum/subjectActivityCount;
		}else {
			subjectActivityAverage = 0;
		}
		subjectActivityTotalAverage = subjectActivitySum / range;
	}
	
	
	// 1. General functions

	public String getDataModel(){
		return dataModel;
	}
	public long getDataDimensions(){
		return dataDimensions;
	}
	public long getProjectlDatasetLength() {
		return projectDataSetLength;
	}
	
	public long getProjectInterval() {
		return projectTimecodeInterval;
	}
	
	public String getProjectTitle() {
		return recordName;
	}
	
	public int getDatasetLength() {
		return subjectDataSetLength;
	}

	public long getId() {
		return id;
	}

	public String getRecordName() {
		return recordName;
	}

	public long getAge() {
		return (long) age;
	}
	
	public double getGender() {
		return gender;
	}

	public String getEducation() {
		return education;
	}
	
	public double getSubjectActivitySum() {
		return subjectActivitySum;
	}
	
	public double getSubjectActivityCount() {
		return subjectActivityCount;
	}
	
	public double getSubjectActivityAverage() {
		return subjectActivityAverage;
	}
	
	public double getSubjectActivityTotalAverage() {
		return subjectActivityTotalAverage;
	}
	
	
	// 2. Calculations
	
	public void matchDataPoint(long _tc) {
		int foundone = -1;
		for (int i = 0; i < subjectPoints.size(); i++) {												// <-- Brute force – needs to be optimized for elegance
			JSONObject pointJSON = (JSONObject) subjectPoints.get(i);
			long ts = (long) pointJSON.get("timeStamp");
			double tt = ts/projectTimecodeInterval;
			long tsRound = Math.round(tt)*projectTimecodeInterval;
			if (tsRound == _tc) {
				foundone = i;
			}
		}
		if(foundone != -1) {
			JSONObject pointJSON = (JSONObject) subjectPoints.get(foundone);
			Object tx = pointJSON.get("x"); 															// load x,y 
			Object ty = pointJSON.get("y");
			double xJSON;
			double yJSON;
			
			if (tx != null) { xJSON = Double.parseDouble(tx.toString());} else {xJSON = 0;}				//make sure that data is not null
			if (ty != null) { yJSON = Double.parseDouble(ty.toString());} else {yJSON = 0;}
			
			if (pointJSON.containsKey("buttonState")) { 												// if there is button data
				double bsJSON;
				Object bs = pointJSON.get("buttonState");
				if (bs != null) { bsJSON = Double.parseDouble(bs.toString()); } else { bsJSON = 1; }

				PointsList.add(new Point(1, _tc, xJSON, yJSON, bsJSON, true)); 							// store coordinates in Point array
			} else {
				PointsList.add(new Point(1, _tc, xJSON, yJSON, true)); 

			}
		}else {
			PointsList.add(new Point(1, _tc, missingXdata, missingYdata, missingButtonData, false));	// if no data was matched for this timecode add filler point
		}
		
	}
	
	public Point2D getPointByIndex(int _t) {
		return PointsList.get(_t).getPoint();
	}

	public Point getPointObjectByIndex(int _t) {
		return PointsList.get(_t).getPointObject();
	}

	public double getActivity(int _t) {
		double currentActivity;
		if (_t == 0) {
			currentActivity = 0;
		} else {
			currentActivity = PointsList.get(_t).getActivityData();
		}
		return currentActivity;
	}
}
