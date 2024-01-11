
//  ------------------------------------------------------------
//  irmaANALYSIS V 0.1.9
//  Class to store data of the whole sample of a measurement (comprising of subjects)
//  Andreas Pirchner, 2018-2020
//  ------------------------------------------------------------

//  Functions

//  Calculate:
//  getAFA()
//  getDOA()
//  entireAFA --> probably covered by sectionAverage
//  sectionAverage()
//  getColor()


//  Visualize:
//  drawTimeAFA
//  drawTimeDOA
//  drawTimeActivity
//  
//  ------------------------------------------------------------

package medis.MEDISanalysis;

import java.util.ArrayList;
//import java.util.List;
import java.util.List;

import javafx.scene.paint.Color;
import math.geom2d.Point2D;

public class Sample {
	ArrayList<Subject> SubjectsList = new ArrayList<Subject>();
	long datasetLength;
	int sampleSize;
	String projectTitle;
	long interval;
	//long duration;
	double duration;
	long offsetBegin = 0;
	long offsetEnd;
	
	Sample(){          
		sampleSize = 0;
	}
	
	public void clearSample() {
		SubjectsList.clear();
		offsetBegin = 0;
		datasetLength = 0;
		sampleSize = 0;
		projectTitle = "";
		interval = 0;
		duration = 0;
	}
	
	public long getDatasetLength() {
		return datasetLength;
	}
	
	public void addSubject(Subject _s){
       SubjectsList.add(_s);
       //datasetLength = getShortestDataset();						// amount of measuring points (temporal) of the sample  
       if (sampleSize == 0) {
    	   datasetLength = _s.getProjectlDatasetLength();
    	   offsetEnd = datasetLength;
    	   interval = _s.getProjectInterval();
    	   projectTitle = _s.getProjectTitle();
    	   duration = datasetLength * interval/1000.0;
    	   GUI.mainNavipanel.setProjectInfos(datasetLength, interval, duration, projectTitle);
       }
       sampleSize ++;
       makeSampleActivityTotalAverage();
       makeSampleActivityMomentsAverage();
       makeSampleStepSizeAverage();
       System.out.println("----");
    }
	
	public Subject getSubject(int i){
	       return(SubjectsList.get(i)); 
	}
	
	public ArrayList<Subject> getSubjectList(){
		return SubjectsList;
	}
	
	public void setDataOffsetBegin(int _t) {
		offsetBegin = _t;
	}
	
	public void setDataOffsetEnd(int _t) {
		offsetEnd = _t;
	}
	
	public long getDataOffsetBegin() {
		return offsetBegin;
	}
	
	public long getDataOffsetEnd() {
		return offsetEnd;
	}
	
	
	public void makeSampleStepSizeAverage() {
		double sumActivityAverage = 0;
		for (int i = 0; i < SubjectsList.size(); i ++) {
			double ca = SubjectsList.get(i).getSubjectActivityAverage();
			sumActivityAverage += ca;
		}
		//System.out.println(sumActivityAverage/SubjectsList.size());
	}
	
	
	
	
	public void makeSampleActivityMoments() {	// Activity Moments per Subject
		double sumActivity = 0;
		for (int i = 0; i < SubjectsList.size(); i ++) {
			double ca = SubjectsList.get(i).getSubjectActivityCount();
			sumActivity += ca;
		}
		//System.out.println(sumActivity/sampleSize);
	}
	
	public void makeSampleActivitySum() { //Activity Distance per Subject
		double sumActivity = 0;
		for (int i = 0; i < SubjectsList.size(); i ++) {
			double ca = SubjectsList.get(i).getSubjectActivitySum();
			sumActivity += ca;
		}
		//System.out.println(sumActivity/sampleSize);
	}
	
	public void makeSampleActivityTotalAverage() {
		double sumActivity = 0;
		for (int i = 0; i < SubjectsList.size(); i ++) {
			double ca = SubjectsList.get(i).getSubjectActivityTotalAverage();
			sumActivity += ca;
		}
		double sampleActivityTotalAverage = sumActivity / SubjectsList.size();
		//System.out.println(sampleActivityTotalAverage);
		//return subjectActivityTotalAverage;
	}
	
	public void makeSampleActivityMomentsAverage() {
		double sumActivity = 0;
		for (int i = 0; i < SubjectsList.size(); i ++) {
			double ca = SubjectsList.get(i).getSubjectActivityCount()/datasetLength;
			sumActivity += ca;
		}
		double sampleActivityMomentsAverage = sumActivity / SubjectsList.size();
		//System.out.println(sampleActivityMomentsAverage);
	}
	
	
	
	public int getShortestDataset() {						// find the shortes Dataset in the Sample to avoid breaking the iterator while looping through non-existent data
		int minimalLength = 1000000000;						// Update 2021: now the length of the recording is saved in the raw data and can be read from there
		for (int i = 0; i < SubjectsList.size(); i ++) {
			int testLength = SubjectsList.get(i).getDatasetLength();
			if (testLength < minimalLength) {
				minimalLength = testLength;
			}
		}
		//return minimalLength;
		return (int) datasetLength-1;							// new! length is now in the RAW data (2021)
	}
   
    
   
    public Point2D getSectionAFA(int _start, int _end,  ArrayList<Boolean> filterList) {
    	int len = _end-_start;
    	double AFAx = 0;
    	double AFAy = 0;
    	
    	for (int i = _start; i < _end; i++) {
	    		Point2D cp = getAFA(i, filterList);
	    		AFAx += cp.x();
	    		AFAy += cp.y();
    	
    	}
    	AFAx = AFAx / len;
    	AFAy = AFAy / len;
    	Point2D SectionAFA = new Point2D(AFAx, AFAy);
    	return SectionAFA;
    }
    
     public Point2D getAFA(int _t, ArrayList<Boolean> filterList) {			// Average Focus of Attention at _t
    	int countedSubjects = 0;
    	double AFAx = 0;
    	double AFAy = 0;
    	for (int i = 0; i < SubjectsList.size(); i ++) {
    		if (filterList.get(i) == Boolean.TRUE) {
	    		Point2D cp = SubjectsList.get(i).getPointByIndex(_t);
	    		AFAx += cp.x();
	    		AFAy += cp.y();
	    		countedSubjects ++;
    		}
    	}
    	AFAx = AFAx / countedSubjects;
    	AFAy = AFAy / countedSubjects;
    	
    	Point2D AverageFocusOfAttention = new Point2D(AFAx, AFAy);
    	return AverageFocusOfAttention;
    }
    
    public double get1dCartesianAverage(int _t,  ArrayList<Boolean> filterList) {
    	int countedSubjects = 0;
    	double avg = 0;
    	for (int i = 0; i < SubjectsList.size(); i ++) {
    		if (filterList.get(i) == Boolean.TRUE) {
    			Point cp = SubjectsList.get(i).getPointObjectByIndex(_t);
    			avg += cp.x;
    			countedSubjects ++;
    		}
    	}
    	avg = avg / countedSubjects;
    	return avg;
    }
    
    public double get1dCartesianActivity(int _t,  ArrayList<Boolean> filterList) {
    	double act = 0;
    	
    	return act;
    }
     
    public double getDOA(int _t,  ArrayList<Boolean> filterList) {												// Deviation of Attention at _t
    	int countedSubjects = 0;
    	double distance = 0;
    	for (int i = 0; i < SubjectsList.size(); i ++) {
    		if (filterList.get(i) == Boolean.TRUE) {
	    		Point2D currentFocus = SubjectsList.get(i).getPointByIndex(_t);
	    		Point2D AFA = this.getAFA(_t, filterList);
	    		double d = AFA.distance(currentFocus);
	    		distance += d;
	    		countedSubjects ++;
    		}
    	}
    	double deviationOfAttention = distance/countedSubjects;
    	return deviationOfAttention;
    	
    }
    
    public double getSectionDOA(int _start, int _end,  ArrayList<Boolean> filterList) {
    	double sectionDOA;
    	double sumDOA=0;
    	for (int i = _start; i < _end; i++) {
    		double cd = getDOA(i, filterList);
    		sumDOA += cd;
    	}
    	sectionDOA = sumDOA/(_end-_start);
    	return sectionDOA;
    }
    
    public double getActivity(int _t, ArrayList<Boolean> filterList) {
    	double sampleAct = 0;
    	for (int i = 0; i < SubjectsList.size(); i ++) {
    		if (filterList.get(i) == Boolean.TRUE) {
	    		double a = SubjectsList.get(i).getActivity(_t);
	    		sampleAct += a; 
    		}
    	}
    	return sampleAct;
    }
    
    public ArrayList<Point> getCurrentButtonsPressed(int _t, ArrayList<Boolean> filterList) {
    	ArrayList<Point> subjectsButtonsPressed= new ArrayList<Point>();
    	
    	for (int i = 0; i < SubjectsList.size(); i ++) {
    		if (filterList.get(i) == Boolean.TRUE) {
	    		Point cp = SubjectsList.get(i).getPointObjectByIndex(_t);				// in this function we need the whole measure point data
	    		if (cp.bs == 0) {
	    			subjectsButtonsPressed.add(cp);
	    		}
    		}
    	}
    	return subjectsButtonsPressed;
    }
    
    public ArrayList<Point> getCurrentMeasurePoints(int _t, ArrayList<Boolean> filterList) {
    	ArrayList<Point> currentMeasurePoints= new ArrayList<Point>();
    	for (int i = 0; i < SubjectsList.size(); i ++) {
    		if (filterList.get(i) == Boolean.TRUE) {
	    		Point cp = SubjectsList.get(i).getPointObjectByIndex(_t);
	    		currentMeasurePoints.add(cp);
    		}
    	}
    	return currentMeasurePoints;
    }
    
    public Color getColorObject(Point2D _p) {
		Color rgb;
		Point2D topCorner = new Point2D(0.0, -0.577350269189626);
		Point2D rightCorner = new Point2D(0.5, 0.288675134594813);
		Point2D leftCorner = new Point2D(-0.5, 0.288675134594813);
		Point2D currentPoint = _p;
		double propRed = distancePointLine(currentPoint, leftCorner, topCorner); 		// get red
		propRed = mapValue(propRed, 0.0, 0.88/2, 0, 255); 								// map Value and save it to rgb array
		double propGreen = distancePointLine(currentPoint, topCorner, rightCorner); 	// get green
		propGreen = mapValue(propGreen, 0, 0.88/2, 0, 255); 							// map Value and save it to rgb array
		double propBlue = distancePointLine(currentPoint, leftCorner, rightCorner);		// get blue
		propBlue = mapValue(propBlue, 0, 0.88/2, 0, 255); 								// map Value and save it to rgb array				
		rgb = Color.rgb((int) propRed, (int) propGreen, (int) propBlue);
		
		return rgb;
	}
    
    public double distancePointLine(Point2D PV, Point2D LV1, Point2D LV2) {
		// PV = Point ; LV1 = Line Point 1 ; LV2 = Line Point 2
		//double dist = Math.abs((L2.y()-L1.y())*P.x()-(L2.x()-L1.x())*P.y()+L2.x()*L1.y()-L2.y()*L1.x()) / Math.sqrt(Math.sqrt((L2.y()-L1.y())+Math.sqrt(L2.x()-L1.x())));
										
	    Point2D slope = new Point2D (LV2.x() - LV1.x(), LV2.y() - LV1.y());				// slope of line
	    double lineLengthi = slope.x() * slope.x() + slope.y() * slope.y();     		// squared length of line;

	    Point2D s = new Point2D(PV.x() - LV1.x(), PV.y() - LV1.y());
		double ti = (s.x()* slope.x()+ s.y()*slope.y())/lineLengthi;
		Point2D p = new Point2D(slope.x() * ti, slope.y() * ti);						// crawl the line acoording to its slope to distance t
		Point2D projectionOnLine = new Point2D(LV1.x()+p.x(), LV1.y()+p.y());			// add the starting coordinates			
		Point2D subber = new Point2D(projectionOnLine.x()- PV.x(), projectionOnLine.y()- PV.y()); // now calculate the distance of the measuring point to the projected point on the line
		double dist = (float) Math.sqrt(subber.x() * subber.x() + subber.y() * subber.y());
		return dist;
	}
	
	public double map(double value, double istart, double istop, double ostart, double ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}
	
	public double mapValue(double _value, double _valMin, double _valMax, double _destMin, double _destMax){
        double proportion = (_value - _valMin) / (_valMax - _valMin);  					// calculate the proportion between 0 and 1
        double scal = _destMax - _destMin;                             
        double result = (proportion * scal) + _valMin;                 					// now scale it according to the desired scale
        if (result > _destMax){result = _destMax;}                  					// coinstrain value to 0 - 255
        if (result < _destMin){result = _destMin;}
        return result;
    }
}
