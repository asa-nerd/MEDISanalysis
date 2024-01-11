package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class timelineActivity extends timeline{
	ArrayList<Line> lines;
	ArrayList<Double> linieLengths;
	double originY =  240;
	
	float minSizeThreshold = 1f;
	Color baseColor = Color.rgb(255, 255, 255);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	timelineActivity(Sample _s, int _id, int _initialTimeCode){
		super(_s, _id, _initialTimeCode);
		timelineType = "ACTIVITY";
		lines = new ArrayList<Line>();
		linieLengths = new ArrayList<Double>();
		drawScale(0, s.getShortestDataset());
		drawTimeline(0, s.getShortestDataset());
	}
	
	@Override
	public void clearTimeline() {
		lines.clear();
		linieLengths.clear();
		dataLayer.getChildren().clear();
	}
	
	@Override
	public void updateTimeline() {
		double lineWidth = zoomFactor;
	    if (lineWidth < minSizeThreshold) {lineWidth = minSizeThreshold;}
	    double len;
	    
		for (int i = 0; i < lines.size(); i++) {
			len = linieLengths.get(i);
			Line l = lines.get(i);
			l.setEndY(originY-len*yScale);
			l.setStartX(i*stepSize+0.5);
			l.setEndX(i*stepSize+0.5);
			l.setStrokeWidth(lineWidth);
			if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
				l.setOpacity(1);
			 	else l.setOpacity(0.2);
		}
		layerContainer.setPrefWidth(lines.size()*zoomFactor*2);
	}
	
	@Override
	public void drawTimeline(int _begin, int _end) {
		clearTimeline();
		// General definitions
	    int rangeLength = _end - _begin;
	    stepSize = zoomFactor*2;
	    layerContainer.setPrefWidth(rangeLength * stepSize);
	    
	    double lHeight;
	    double lineWidth = zoomFactor;
	    if (lineWidth < minSizeThreshold) {lineWidth = minSizeThreshold;}

	    // Definitions specific for timeline "Activity"
	    for (int i = _begin; i < _end; i = i + 1) {
	    	lHeight = s.getActivity(i, filterListSubjects)*200;					// get line height as current Deviation of Attention
			linieLengths.add(lHeight);
	    	Line line = new Line(i*stepSize+0.5, originY, i*stepSize+0.5, originY-lHeight*yScale); // make lines
	    	line.getProperties().put("timeCode", i);
			line.getProperties().put("color", baseColor);
			line.setStroke(baseColor);	// set color for line
	    	//line.setStroke(Color.rgb(255,255,255));
			if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
				line.setOpacity(1);
			 	else line.setOpacity(0.2);
			
			line.setStrokeWidth(lineWidth);
			lines.add(line);
	    }
	    
	    // add Rollover and Clickability
	 	makeRollovers(lines, hightlightColor);
			    
	    // Add objects to timeline
		dataLayer.getChildren().addAll(lines);				// add all line Nodes to parent Pane
		dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
		
	}
}
