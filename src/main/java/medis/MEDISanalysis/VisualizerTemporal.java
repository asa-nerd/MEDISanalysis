package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import math.geom2d.Point2D;

public class VisualizerTemporal {
	Sample s;
	ScrollPane sc;
	VBox moduleContainer; 
	ArrayList<timeline> timelines;	
	int timelineCounter = 0;
	int mainTimer = 0;
	
	VisualizerTemporal (Sample _s){
		s = _s;
		sc = new ScrollPane();
		moduleContainer = new VBox();
		sc.setMinSize(1360, 400);
		sc.setVbarPolicy(ScrollBarPolicy.ALWAYS);  
        sc.setContent(moduleContainer);
        sc.setFitToWidth(true);
        timelines = new ArrayList<timeline>();
	}
	
	public ScrollPane getTemporalContainer() {
		return sc;
	}
	
	public int getMainTimer() {
		return mainTimer;
	}
	
	public void setMainTimer(int _t) {
		mainTimer = _t;
	}
	
	public void discardTimeline(int _id) {
		for (int i = 0; i < timelines.size(); i++) {
			timeline tl = timelines.get(i);
			int cid = tl.id;
			if (cid == _id) {
				timelines.remove(i);
				moduleContainer.getChildren().remove(tl.getTimeline());
				return;
			}
		}
	}
	
	public void clearTimelines() {
		moduleContainer.getChildren().clear();
		timelines.clear();
	}
	
	public void makeTimelineElement(Sample _s, String _type) {
		
		switch(_type) {
			case "AFA":
				timelineAFA newTL = new timelineAFA(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(newTL);
				moduleContainer.getChildren().add(newTL.getTimeline());
				break;
			case "ACTIVITY":
				timelineActivity newTLA = new timelineActivity(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(newTLA);
				moduleContainer.getChildren().add(newTLA.getTimeline());
				break;
			case "SUBJECTACTIVITY":
				timelineSubjectsActivity newTSA = new timelineSubjectsActivity(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(newTSA);
				moduleContainer.getChildren().add(newTSA.getTimeline());
				break;
			case "SUBJECTATTENTION":
				timelineSubjectsAttention newTSAT = new timelineSubjectsAttention(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(newTSAT);
				moduleContainer.getChildren().add(newTSAT.getTimeline());
				break;	
			case "BOOKMARKS":
				timelineBookmarks newBOOK = new timelineBookmarks(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(newBOOK);
				moduleContainer.getChildren().add(newBOOK.getTimeline());
				break;
			case "1DCARTESIAN":
				timeline1dCartesian new1DCartesian = new timeline1dCartesian(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(new1DCartesian);
				moduleContainer.getChildren().add(new1DCartesian.getTimeline());
				break;
			case "1DCARTESIAN-AVERAGE":
				timeline1dCartesianAverage new1DCartesianAverage = new timeline1dCartesianAverage(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(new1DCartesianAverage);
				moduleContainer.getChildren().add(new1DCartesianAverage.getTimeline());
				break;				
			case "1DCARTESIAN-ACTIVITY":
				//timeline1dCartesianActivity new1DCartesianActivity = new timeline1dCartesianActivity(_s, timelineCounter, GUI.getTimerCounter());
				//timelines.add(new1DCartesian);
				//moduleContainer.getChildren().add(new1DCartesian.getTimeline());
				break;
			case "STANDARDDEVIATION":
				timelineSDOA newStandardDeviationTimeline = new timelineSDOA(_s, timelineCounter, GUI.getTimerCounter());
				timelines.add(newStandardDeviationTimeline);
				moduleContainer.getChildren().add(newStandardDeviationTimeline.getTimeline());
				break;

		}	
		timelineCounter ++;
	}
	
	public void movePlaybackLines(int _t) {
		timelines.forEach((tl) -> {tl.updatePlaybackTimer(_t);});
	}
	
	public void redrawAllTimelines() {
		timelines.forEach((tl) -> {
			tl.updateTimeline();
		});
	}
	
	public static double[] getColor(Point2D _p) {
		double[] rgb = {0,0,0};
		Point2D topCorner = new Point2D(0.0, -0.577350269189626);
		Point2D rightCorner = new Point2D(0.5, 0.288675134594813);
		Point2D leftCorner = new Point2D(-0.5, 0.288675134594813);
		Point2D currentPoint = _p;
		double propRed = distancePointLine(currentPoint, leftCorner, topCorner); 		// get red
		rgb[0] = mapValue(propRed, 0.0, 0.88/2, 0, 255); 										// map Value and save it to rgb array
		double propGreen = distancePointLine(currentPoint, topCorner, rightCorner); 	// get green
	    rgb[1] = mapValue(propGreen, 0, 0.88/2, 0, 255); 											// map Value and save it to rgb array
		double propBlue = distancePointLine(currentPoint, leftCorner, rightCorner);	// get blue
	    rgb[2] = mapValue(propBlue, 0, 0.88/2, 0, 255); 											// map Value and save it to rgb array				
		return rgb;
		
	}
	
	public static double distancePointLine(Point2D PV, Point2D LV1, Point2D LV2) {
		// PV = Point ; LV1 = Line Point 1 ; LV2 = Line Point 2
		//double dist = Math.abs((L2.y()-L1.y())*P.x()-(L2.x()-L1.x())*P.y()+L2.x()*L1.y()-L2.y()*L1.x()) / Math.sqrt(Math.sqrt((L2.y()-L1.y())+Math.sqrt(L2.x()-L1.x())));
										
	    Point2D slope = new Point2D (LV2.x() - LV1.x(), LV2.y() - LV1.y());		// slope of line
	    double lineLengthi = slope.x() * slope.x() + slope.y() * slope.y();     // squared length of line;

	    Point2D s = new Point2D(PV.x() - LV1.x(), PV.y() - LV1.y());
		double ti = (s.x()* slope.x()+ s.y()*slope.y())/lineLengthi;
		Point2D p = new Point2D(slope.x() * ti, slope.y() * ti);				// crawl the line according to its slope to distance t
		Point2D projectionOnLine = new Point2D(LV1.x()+p.x(), LV1.y()+p.y());	// add the starting coordinates			
		Point2D subber = new Point2D(projectionOnLine.x()- PV.x(), projectionOnLine.y()- PV.y()); // now calculate the distance of the measuring point to the projected point on the line
		double dist = (float) Math.sqrt(subber.x() * subber.x() + subber.y() * subber.y());
		return dist;
	}
	
	public double map(double value, double istart, double istop, double ostart, double ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
		}
	
	public static double mapValue(double _value, double _valMin, double _valMax, double _destMin, double _destMax){
        double proportion = (_value - _valMin) / (_valMax - _valMin);  // calculate the proportion between 0 and 1
        double scal = _destMax - _destMin;                             
        double result = (proportion * scal) + _valMin;                 // now scale it according to the desired scale
        if (result > _destMax){result = _destMax;}                  // coinstrain value to 0 - 255
        if (result < _destMin){result = _destMin;}
        return result;
    }
	

}
