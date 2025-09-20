package medis.MEDISanalysis;

//import static meadisAnalysis.VisualizerTemporal.getColor;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import math.geom2d.Point2D;

import java.util.ArrayList;

public class spatializer1dBookmarks extends spatializer{

	int scaleChart = 400;
	int originX = 350/2;
	int originY = 326/2;

	spatializer1dBookmarks(Sample _s){
		super(_s);
        drawChart();
	}
	
	@Override
	public void drawChart() {
		/*Line dimensionX = new Line(originX-scaleChart/2, originY, originX+scaleChart/2, originY);
		dimensionX.setStroke(baseColor);
		dimensionX.setOpacity(0.5);
		dimensionX.setStrokeWidth(3);
		Line origin = new Line(originX,originY-10,originX,originY+10);	
		origin.setStroke(baseColor);
		origin.setOpacity(0.5);
		origin.setStrokeWidth(1);
		chartCanvas.getChildren().addAll(dimensionX, origin);

		 */
	}
	
	@Override
	public void drawSampleVector(int _t, ArrayList<Boolean> filterList){
		/*clearDataCanvas();
		
		Point2D Average = s.getAFA(_t, filterList);				// get Average
		double afaX = Average.x()*scaleChart+originX;							
		Circle circleAverage = new Circle(afaX,originY,7);			// make, style and position Circle for AFA
		circleAverage.setStrokeWidth(1);
		circleAverage.setFill(baseColor);
		circleAverage.setStroke(baseColor);
				
		for (int k = 0; k < s.SubjectsList.size(); k++) {		// loop all subjects
			if (filterList.get(k) == Boolean.TRUE) {
				Subject subject = s.SubjectsList.get(k);
				Point2D p = subject.getPointByIndex(_t);
				double px = p.x()*scaleChart+originX;				
				Circle c = new Circle(px,originY,1);					// make and style circle
				c.setStrokeType(StrokeType.OUTSIDE);
				c.setStrokeWidth(2);
				c.setFill(Color.BLACK);
				c.setStroke(baseColor);
				subjectCircles.add(c);
			}
		}
		dataCanvas.getChildren().addAll(subjectCircles);
		dataCanvas.getChildren().add(circleAverage);

		 */
	}
	
	@Override
	public void drawSampleIDs(int _t, ArrayList<Boolean> filterList){
		/*subjectIDs.clear();
		for (int k = 0; k < s.SubjectsList.size(); k++) {	// loop all subjects
			if (filterList.get(k) == Boolean.TRUE) {
				Subject subject = s.SubjectsList.get(k);
				Point2D p = subject.getPointByIndex(_t);
				double px = p.x()*scaleChart+originX;
				Text t = new Text();
				t.setFill(baseColor);
				t.setStyle("-fx-font: 8 Lato;");
				t.setText(String.valueOf(k+1));
				t.setX(px); 
				t.setY(originY+20);
				subjectIDs.add(t);
			}
		}
		dataCanvas.getChildren().addAll(subjectIDs);

		 */
	}
	
	@Override
	public void drawSection(int _begin, int _end, ArrayList<Boolean> filterList) {
		/*clearDataCanvas();
		
		for (int i = _begin; i < _end; i++) {						// 1. Make measuring points for all subjects in section
			for (int k = 0; k < s.SubjectsList.size(); k++) {	
				if (filterList.get(k) == Boolean.TRUE) {
					Subject subject = s.SubjectsList.get(k);
					Point2D p = subject.getPointByIndex(i);
					double px = p.x()*scaleChart+scaleChart/2;						
					Circle c = new Circle(px,originY,1);			// make and style circle
					c.setStrokeType(StrokeType.OUTSIDE);
					c.setStrokeWidth(2);
					c.setFill(Color.rgb(0,0,0, 0.25));
					c.setStroke(baseColor);
					subjectCircles.add(c);
				}
			}
		}
		
		dataCanvas.getChildren().addAll(subjectCircles);
		
		
		Point2D cp = s.getSectionAFA(_begin, _end, filterList);		// 2. Make AFA from all subjects over duration of section
		double afaX = cp.x()*scaleChart+scaleChart/2;
		Circle circleAFA = new Circle(afaX,originY,7);				// make, style and position Circle for AFA
		circleAFA.setStrokeWidth(1);
		circleAFA.setFill(baseColor);
		circleAFA.setStroke(Color.WHITE);
		dataCanvas.getChildren().add(circleAFA);
		*/
	}


		
}


