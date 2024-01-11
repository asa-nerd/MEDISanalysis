package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class timeline1dCartesianAverage extends timeline{
	ArrayList<Circle> dots;
	ArrayList<Line> lines;
	ArrayList<Point> points;
	ArrayList<Double> linieLengths;
	double originY =  120;
	int rangeLength;
	
	int minSizeThreshold = 1;
	Color baseColor = Color.rgb(255, 255, 255);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	timeline1dCartesianAverage(Sample _s, int _id, int _initialTimeCode){
		super(_s, _id, _initialTimeCode);    
		timelineType = "1DCARTESIANAVERAGE";
		dots = new ArrayList<Circle>();
        lines = new ArrayList<Line>();
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
		double lineWidth;
		double len;
	    int xpos;
	    stepSize = zoomFactor*2;
		
		for (int i = 0; i < dots.size(); i++) {
			circleRadius = zoomFactor/4;
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
			
			Line l = lines.get(i);
			xpos = (int) l.getProperties().get("timeCode");
			l.setStartY(originY);
			l.setEndY(originY-len*yScale);
			l.setStartX(xpos*stepSize+0.5);
			l.setEndX(xpos*stepSize+0.5);
			lineWidth = circleRadius/2;
			l.setStrokeWidth(lineWidth);
			if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
				l.setOpacity(1);
			 	else l.setOpacity(0.2);
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
	    double lineWidth;
	    
	    double currentAvg;
	    double lastY;
	    
		for (int i = _begin; i < _end; i = i + 1) {									// Definitions specific for timeline "Average Focus of Attention"
			currentAvg = s.get1dCartesianAverage(i, filterListSubjects);				// get all measure points of sample
			lHeight = currentAvg *100;	
			linieLengths.add(lHeight);
			circleRadius = zoomFactor/4;
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
			
			
			Line line = new Line(i*stepSize+0.5, originY, i*stepSize+0.5, originY-lHeight);
			line.getProperties().put("timeCode", i);
			line.getProperties().put("color", baseColor);
			line.setStroke(baseColor);		// set color for line
			lineWidth = circleRadius/2;
			line.setStrokeWidth(lineWidth);	
			if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
				line.setOpacity(1);
			 	else line.setOpacity(0.2);
			lines.add(line);		
		}
		
		makeRolloversDots(dots, hightlightColor);										// add rollover and clickability
		dataLayer.getChildren().addAll(lines);
		dataLayer.getChildren().addAll(dots);										// add all line Nodes to parent Pane
		dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
	}		
}
