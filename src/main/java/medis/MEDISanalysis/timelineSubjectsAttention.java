package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import math.geom2d.Point2D;

public class timelineSubjectsAttention extends timeline{
	ArrayList<ArrayList<Line>> SubjectLines;				// ArrayList to hold the Line-Arraylist for each Subject
	float minSizeThreshold = 1.5f;
	Color baseColor = Color.rgb(255, 255, 255);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	timelineSubjectsAttention(Sample _s, int _id, int _initialTimeCode){
		super(_s, _id, _initialTimeCode);
		timelineType = "SUBJECTATTENTION";
		SubjectLines = new ArrayList<ArrayList<Line>>();
		
		for(Subject sub : _s.SubjectsList) {
			ArrayList<Line> lines = new ArrayList<Line>();
			SubjectLines.add(lines);
		}
		drawScale(0, s.getShortestDataset());
		drawTimeline(0, s.getShortestDataset());
	}
	
	@Override
	public void updateTimeline() {
		
		double lineWidth = zoomFactor;
	    if (lineWidth < minSizeThreshold) {lineWidth = minSizeThreshold;}
	    double len;
	    
		for (int k = 0; k < s.SubjectsList.size(); k ++) {
		   	Subject sub = s.getSubject(k);
		   	ArrayList<Line> subjectLines = SubjectLines.get(k);
			for (int i = 0; i < subjectLines.size(); i++) {
				Line l = subjectLines.get(i);
				l.setStartX(i*stepSize+0.5);
				l.setEndX(i*stepSize+0.5);
				l.setStrokeWidth(lineWidth);
				if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
					l.setOpacity(1);
				 	else l.setOpacity(0.2);
			}
			layerContainer.setPrefWidth(subjectLines.size()*zoomFactor*2);
		}
	}
	
	@Override
	public void drawTimeline(int _begin, int _end) {
		
	   // General definitions
	   int rangeLength = _end - _begin;
	   double originX =  40;
	   stepSize = zoomFactor*2;
	   layerContainer.setPrefWidth(rangeLength * stepSize);
	   

	   double lHeight;
	   double lineWidth = zoomFactor;
	   if (lineWidth < minSizeThreshold) {lineWidth = minSizeThreshold;}

	   // Definitions specific for timeline "Subjects Activity"
	   for (int k = 0; k < s.SubjectsList.size(); k ++) {
		   	Subject sub = s.getSubject(k);
		   	ArrayList<Line> subjectLines = SubjectLines.get(k);
		    for (int i = _begin; i < _end; i = i + 1) {
		    	//double lHeight = sub.getActivity(i)*200;												// get line height as current Activity
		    	Point2D currentA = sub.getPointByIndex(i);												// get measuring point of subject													// get color of measuring point
		    	Color thisColor = this.getColorObject(currentA);
		    	Line line = new Line(i*stepSize+0.5, originX, i*stepSize+0.5, originX-5);		 		// make lines
				line.setStroke(thisColor);																// set color for line
				line.setStrokeWidth(lineWidth);
				line.getProperties().put("timeCode", i);
				line.getProperties().put("color", thisColor);
				if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd()) 
					line.setOpacity(1);
				 	else line.setOpacity(0.2);
				subjectLines.add(line);
		    }
		    // add Rollover and Clickability
			makeRollovers(subjectLines, hightlightColor);
		    dataLayer.getChildren().addAll(subjectLines);				// add all line Nodes to parent Pane
		    originX += 20;
	   }
	   dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
		
	}
}
