package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;

public class timelineBookmarks extends timeline{
	ArrayList<Circle> dots;
	ArrayList<Line> lines;
	ArrayList<Point> points;
	ArrayList<Bookmark> bookmarks;
	ArrayList<Double> linieLengths;
	double originY =  120;
	int rangeLength;
	Line baseLine;
	int minSizeThreshold = 2;
	Color baseColor = Color.rgb(255, 255, 255);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	timelineBookmarks(Sample _s, int _id, int _initialTimeCode){
		super(_s, _id, _initialTimeCode);    
		timelineType = "BOOK";
		dots = new ArrayList<Circle>();
		points = new ArrayList<Point>();
		bookmarks = new ArrayList<Bookmark>();
		baseLine = new Line();
        drawScale(0, s.getShortestDataset());
        drawTimeline(0, s.getShortestDataset());
        drawOriginLine(originY);
	}
	
	@Override
	public void clearTimeline() {
		System.out.println("clear timeline");
		dots.clear();
		//lines.clear();
		//linieLengths.clear();
		dataLayer.getChildren().clear();
	}
	
	@Override
	public void updateTimeline() {
		double circleRadius;
	    double linesWidth;
	    double len;
	    int xpos;
	    stepSize = zoomFactor*2;
		
		for (int i = 0; i < dots.size(); i++) {
			circleRadius = zoomFactor*yScale*20;
			if (circleRadius < minSizeThreshold) {circleRadius = minSizeThreshold;}
			Circle c = dots.get(i);
			xpos = (int) c.getProperties().get("timeCode");
			c.setCenterY(originY);
			c.setCenterX(xpos*stepSize+0.5);
			c.setRadius(circleRadius);
			c.setOpacity(1);
			/*if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd())
				c.setOpacity(1);
			 	else c.setOpacity(0.2);*/
		}
		layerContainer.setPrefWidth(rangeLength*zoomFactor*2);	// Adjust the width of the container
	}
	
	@Override
	public void drawTimeline(int _begin, int _end) {			
	    rangeLength = _end - _begin;												// General definitions
	    stepSize = zoomFactor*2;
	    layerContainer.setPrefWidth(rangeLength * stepSize);
	    							//highlight color is red
	    double lHeight;
	    double circleRadius;
	    double lineWidth;

		//ArrayList<Boolean> getFilterListSubjects()
		for(int i = 0; i< s.sampleSize; i++){
			if (filterListSubjects.get(i) == true) {
				Subject sub = s.getSubject(i);
				ArrayList<Bookmark> subjectBookmarks = sub.getAllBookmarks();
				for (int k = 0; k < subjectBookmarks.size(); k++) {
					//System.out.println("looping Bookmarks"+k);
					Bookmark b = subjectBookmarks.get(k);
					long ts = b.getTimeStamp();
					int tp = b.getTimePos();

					circleRadius = zoomFactor * yScale * 20;
					if (circleRadius < minSizeThreshold) {
						circleRadius = minSizeThreshold;
					}
					Circle circle = new Circle(tp * stepSize + 0.5, originY, circleRadius);
					circle.setFill(baseColor);
					circle.getProperties().put("timeCode", tp);
					circle.getProperties().put("color", baseColor);
					circle.setStrokeWidth(0);
					circle.setOpacity(1);
					/*if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd())
						circle.setOpacity(1);
					else circle.setOpacity(0.2);*/
					dots.add(circle);
				}
			}
		}


		makeRolloversDots(dots, hightlightColor);								// add rollover and clickability
		baseLine.setStartX(0);													// make a baseline at the origin (the center) of the timeline
		baseLine.setStartY(originY);
		baseLine.setEndX(rangeLength * zoomFactor * 2);
		baseLine.setEndY(originY);
		baseLine.setStroke(Color.WHITE);
		baseLine.setStrokeWidth(1.0);

		dataLayer.getChildren().addAll(baseLine);

		//dataLayer.getChildren().addAll(lines);									// add all line Nodes to parent Pane
		dataLayer.getChildren().addAll(dots);									// add all circle Nodes to parent Pane
		dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
		
	}		
}
