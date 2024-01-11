package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class timeline1dCartesian extends timeline{
	ArrayList<Circle> dots;
	//ArrayList<Line> lines;
	ArrayList<Point> points;
	ArrayList<Double> linieLengths;
	double originY =  120;
	int rangeLength;
	
	int minSizeThreshold = 2;
	Color baseColor = Color.rgb(255, 255, 255, 0.2);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	timeline1dCartesian(Sample _s, int _id, int _initialTimeCode){
		super(_s, _id, _initialTimeCode);    
		timelineType = "1DCartesian";
		dots = new ArrayList<Circle>();
        //lines = new ArrayList<Line>();
        points = new ArrayList<Point>();
        linieLengths = new ArrayList<Double>();
        drawScale(0, s.getShortestDataset());
        drawTimeline(0, s.getShortestDataset());
        drawOriginLine(originY);
	}
	
	@Override
	public void clearTimeline() {
		dots.clear();
		//lines.clear();
		linieLengths.clear();
		dataLayer.getChildren().clear();
	}
	
	@Override
	public void updateTimeline() {
		double circleRadius;
		double len;
	    int xpos;
		stepSize = zoomFactor*2;
		
		for (int i = 0; i < dots.size(); i++) {
			circleRadius = zoomFactor/2;
			if (circleRadius < minSizeThreshold) {circleRadius = minSizeThreshold;}
			len = linieLengths.get(i);
			Circle c = dots.get(i);
			xpos = (int) c.getProperties().get("timeCode");
			c.setCenterY(originY-len*yScale);
			c.setCenterX(xpos*stepSize+0.5);
			c.setRadius(circleRadius);
			if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
				c.setOpacity(1);
			 	else c.setOpacity(0.2);
		}
		layerContainer.setPrefWidth(dots.size()*zoomFactor*2);						// Adjust the width of the container
	
	}
	
	@Override
	public void drawTimeline(int _begin, int _end) {								// function to draw the standard timeline
		
	    int rangeLength = _end - _begin;											// General definitions
	    stepSize = zoomFactor*2;
	    layerContainer.setPrefWidth(rangeLength * stepSize);
	    
	    double lHeight;
	    double circleRadius;
	    //double lineWidth;
	    
		for (int i = _begin; i < _end; i = i + 1) {									// Definitions specific for timeline "Average Focus of Attention"
			points = s.getCurrentMeasurePoints(i, filterListSubjects);				// get all measure points of sample
			for (int j=0; j<points.size(); j++) {									// loop the measure points we got
				Point p = points.get(j);
				lHeight = p.x *100;											// get line height as current Shift of Attention
				linieLengths.add(lHeight);
				circleRadius = zoomFactor/2;
				if (circleRadius < minSizeThreshold) {circleRadius = minSizeThreshold;}		
				Circle circle = new Circle(i*stepSize+0.5, originY-lHeight, circleRadius);
				circle.setFill(baseColor);
				circle.getProperties().put("timeCode", i);
				circle.getProperties().put("color", baseColor);
				circle.setStrokeWidth(0);
				if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
					circle.setOpacity(1);
				 	else circle.setOpacity(0.2);
				dots.add(circle);
			
			}
		}
		
		makeRolloversDots(dots, hightlightColor);										// add rollover and clickability
		dataLayer.getChildren().addAll(dots);										// add all line Nodes to parent Pane
		dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
	}		
}
