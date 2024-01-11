package medis.MEDISanalysis;

import static medis.MEDISanalysis.VisualizerTemporal.getColor;

import java.util.ArrayList;

import javafx.collections.ObservableList;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import math.geom2d.Point2D;
//import medis.MEDISanalysis.Sample;
//import medis.MEDISanalysis.spatializer;

public class spatializer2dTriangular extends spatializer {
	
	int scaleTriangle = 350;
	int heightTriangle = (int) Math.round(Math.sqrt(3)/2 * scaleTriangle);
	
	spatializer2dTriangular(Sample _s){
		super(_s);
        drawChart();
	}
	
	@Override
	public void drawChart() {
		Polygon triangle = new Polygon();
    		triangle.getPoints().addAll((double)scaleTriangle/2, 0.0, (double)scaleTriangle, (double)heightTriangle, 0.0, (double) heightTriangle);
    		triangle.setFill(Color.rgb(255,255,255,0.1));
	    Polygon greenCorner = new Polygon();
	    	greenCorner.setFill(Color.rgb(0, 255, 0));
	    	greenCorner.getPoints().addAll(0.0, (double) heightTriangle, 20.0, (double) heightTriangle, 10.0, (double) heightTriangle-17.3);
	    	greenCorner.setStrokeWidth(0);
	    Polygon redCorner = new Polygon();
	    	redCorner.setFill(Color.rgb(255, 0, 0));
	    	redCorner.getPoints().addAll((double)scaleTriangle-20, (double) heightTriangle, (double)scaleTriangle, (double) heightTriangle, (double)scaleTriangle-10.0, (double) heightTriangle-17.3);
	    	redCorner.setStrokeWidth(0);
	    Polygon blueCorner = new Polygon();
	    	blueCorner.setFill(Color.rgb(0, 0, 255));
	    	blueCorner.getPoints().addAll((double)scaleTriangle/2, 0.0,(double)scaleTriangle/2+10.0, (double) 17.3, (double)scaleTriangle/2-10.0, (double) 17.3);
	    	blueCorner.setStrokeWidth(0);
	    	chartCanvas.getChildren().addAll(triangle, greenCorner, redCorner, blueCorner);
	}
	
	@Override
	public void drawSampleVector(int _t, ArrayList<Boolean> filterList){
		dataCanvas.getChildren().clear(); 					// empty the data Pane
		subjectLines.clear();
		subjectCircles.clear();
		
		Point2D pointAFA = s.getAFA(_t, filterList);				// get AFA
		double afaX = pointAFA.x()*scaleTriangle+scaleTriangle/2;
		double afaY = pointAFA.y()*scaleTriangle+heightTriangle*2/3;
		//double afaY = pointAFA.y()*heightTriangle+heightTriangle*2/3;
		double[] colorAFA = getColor(pointAFA);				// get color for current AFA
															
		Circle circleAFA = new Circle(afaX,afaY,7);			// make, style and position Circle for AFA
		circleAFA.setStrokeWidth(1);
		circleAFA.setFill(Color.rgb((int) colorAFA[0], (int) colorAFA[1], (int) colorAFA[2]));
		circleAFA.setStroke(Color.WHITE);
		
		
		//for (Subject sub: s.SubjectsList) {			
		for (int k = 0; k < s.SubjectsList.size(); k++) {	// loop all subjects
			if (filterList.get(k) == Boolean.TRUE) {
				Subject subject = s.SubjectsList.get(k);
				Point2D p = subject.getPointByIndex(_t);
				double px = p.x()*scaleTriangle+scaleTriangle/2;
				double py = p.y()*scaleTriangle+heightTriangle*2/3;
				//double py = p.y()*heightTriangle+heightTriangle*2/3;
				double[] colorP = getColor(p);					// get color for current subject
				
				Circle c = new Circle(px,py,1);					// make and style circle
				c.setStrokeType(StrokeType.OUTSIDE);
				c.setStrokeWidth(2);
				c.setFill(Color.BLACK);
				c.setStroke(Color.rgb((int) colorP[0], (int) colorP[1], (int) colorP[2]));
				subjectCircles.add(c);
				Line l = new Line(px,py,afaX,afaY);				// make and style line
				l.setStrokeWidth(1);
				l.setStroke(Color.WHITE);
				subjectLines.add(l);
			}
		}
			
		dataCanvas.getChildren().addAll(subjectLines);
		dataCanvas.getChildren().addAll(subjectCircles);
		dataCanvas.getChildren().add(circleAFA);
	}
	
	@Override
	public void drawSampleIDs(int _t, ArrayList<Boolean> filterList){
		subjectIDs.clear();
		for (int k = 0; k < s.SubjectsList.size(); k++) {	// loop all subjects
			if (filterList.get(k) == Boolean.TRUE) {
				Subject subject = s.SubjectsList.get(k);
				Point2D p = subject.getPointByIndex(_t);
				double px = p.x()*scaleTriangle+scaleTriangle/2;
				double py = p.y()*scaleTriangle+heightTriangle*2/3;
				//double py = p.y()*heightTriangle+heightTriangle*2/3;
				Text t = new Text();
				t.setFill(baseColor);
				t.setStyle("-fx-font: 8 Lato;");
				t.setText(String.valueOf(k+1));
				t.setX(px); 
				t.setY(py+10);
				subjectIDs.add(t);
			}
		}
		dataCanvas.getChildren().addAll(subjectIDs);
	}
	

	@Override
	public void drawConvexHUll(int _t, ArrayList<Boolean> filterList){
		convexHullPoints.clear();
		Polygon polygon = new Polygon();
		ObservableList<Double> polyPoints = polygon.getPoints();
		
		for (int k = 0; k < s.SubjectsList.size(); k++) {	// loop all subjects
			Subject subject = s.SubjectsList.get(k);
			Point2D p = subject.getPointByIndex(_t);
			double px = p.x()*scaleTriangle+scaleTriangle/2;
			double py = p.y()*heightTriangle+heightTriangle*2/3;
			polyPoints.add(px);
			polyPoints.add(py);
		}
		
		/*ArrayList<Double> polyPoints = new ArrayList<Double>();
		for (int k = 0; k < convexHullPoints.size(); k++) {
			Point2D p = convexHullPoints.get(_t);
			polyPoints.add(p.x());
			polyPoints.add(p.y());
		}*/
		
		polygon.setFill(baseColor);
	    polygon.setStroke(hightlightColor);
		polygon.setOpacity(0.2);
		dataCanvas.getChildren().addAll(polygon);
	}
	
	@Override
	public void drawOverlayOne() {
		//scaleTriangle
		//heightTriangle
		Line l1 = new Line(0, heightTriangle, scaleTriangle/2,heightTriangle/3*2);
		Line l2 = new Line(scaleTriangle/2, 0, scaleTriangle/2,heightTriangle/3*2);
		Line l3 = new Line(scaleTriangle, heightTriangle, scaleTriangle/2,heightTriangle/3*2);
		l1.setStrokeWidth(1);
		l2.setStrokeWidth(1);
		l3.setStrokeWidth(1);
		l1.setStroke(Color.rgb(255,255,255,0.1));
		l2.setStroke(Color.rgb(255,255,255,0.1));
		l3.setStroke(Color.rgb(255,255,255,0.1));
		dataCanvas.getChildren().addAll(l1,l2,l3);
		//System.out.println("ja");
	}
	
	@Override
	public void drawOverlayTwo() {		
		Line l1 = new Line(scaleTriangle/2, heightTriangle, scaleTriangle/2,heightTriangle/3*2);
		Line l2 = new Line(scaleTriangle/4, heightTriangle/2,scaleTriangle/2,heightTriangle/3*2);
		Line l3 = new Line(scaleTriangle/4*3, heightTriangle/2, scaleTriangle/2,heightTriangle/3*2);
		l1.setStrokeWidth(1);
		l2.setStrokeWidth(1);
		l3.setStrokeWidth(1);
		l1.setStroke(Color.rgb(255,255,255,0.1));
		l2.setStroke(Color.rgb(255,255,255,0.1));
		l3.setStroke(Color.rgb(255,255,255,0.1));
		dataCanvas.getChildren().addAll(l1,l2,l3);
	}
	
	@Override
	public void drawDeviation(int _t, ArrayList<Boolean> filterList) {
		double currentDOA = s.getDOA(_t, filterList);
		Point2D currentAFA = s.getAFA(_t, filterList);
		double px = currentAFA.x()*scaleTriangle+scaleTriangle/2;
		double py = currentAFA.y()*scaleTriangle+heightTriangle*2/3;
		//double py = currentAFA.y()*heightTriangle+heightTriangle*2/3;
		Circle c = new Circle(px,py,currentDOA*scaleTriangle);
		c.setStrokeType(StrokeType.OUTSIDE);
		c.setStrokeWidth(1);
		//c.setFill(Color.BLACK);
		c.setFill(Color.rgb(255,255,255, 0.1));
		c.setStroke(Color.rgb(255,255,255, 0.2));
		dataCanvas.getChildren().add(c);
	}
	
	@Override
	public void drawSection(int _begin, int _end, ArrayList<Boolean> filterList) {
		clearDataCanvas();
		
		// 1. Make measuring points for all subjects in section
				for (int i = _begin; i < _end; i++) {
					//for (Subject subject: s.SubjectsList) {	
					for (int k = 0; k < s.SubjectsList.size(); k++) {	
						if (filterList.get(k) == Boolean.TRUE) {
							Subject subject = s.SubjectsList.get(k);
							Point2D p = subject.getPointByIndex(i);
							double px = p.x()*scaleTriangle+scaleTriangle/2;
							//double py = p.y()*heightTriangle+heightTriangle*2/3;
							double py = p.y()*scaleTriangle+heightTriangle*2/3;
							double[] colorP = getColor(p);					// get color for current subject
							
							Circle c = new Circle(px,py,1);					// make and style circle
							c.setStrokeType(StrokeType.OUTSIDE);
							c.setStrokeWidth(2);
							//c.setFill(Color.BLACK);
							c.setFill(Color.rgb(0,0,0, 0.25));
							c.setStroke(Color.rgb((int) colorP[0], (int) colorP[1], (int) colorP[2], 0.25));
							subjectCircles.add(c);
						}
					}
				}
				
				dataCanvas.getChildren().addAll(subjectCircles);
				
				// 2. Make AFA from all subjects over duration of section
				Point2D cp = s.getSectionAFA(_begin, _end, filterList);
				double afaX = cp.x()*scaleTriangle+scaleTriangle/2;
				//double afaY = cp.y()*heightTriangle+heightTriangle*2/3;
				double afaY = cp.y()*scaleTriangle+heightTriangle*2/3;
				double[] colorAFA = getColor(cp);
				Circle circleAFA = new Circle(afaX,afaY,7);			// make, style and position Circle for AFA
				circleAFA.setStrokeWidth(1);
				circleAFA.setFill(Color.rgb((int) colorAFA[0], (int) colorAFA[1], (int) colorAFA[2]));
				circleAFA.setStroke(Color.WHITE);
				dataCanvas.getChildren().add(circleAFA);
	}
		
}


