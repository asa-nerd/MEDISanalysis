package medis.MEDISanalysis;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class timelineBookmarks extends timeline{
	ArrayList<Circle> dots;
	ArrayList<Point> points;
	ArrayList<Rectangle> rectangles;
	ArrayList<Bookmark> allBookmarks;
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
		rectangles = new ArrayList<Rectangle>();

		allBookmarks = new ArrayList<Bookmark>();
		baseLine = new Line();
        drawScale(0, s.getShortestDataset());
        drawTimeline(0, s.getShortestDataset());

		rangeLength = s.getShortestDataset() - 0;
	}
	
	@Override
	public void clearTimeline() {
		System.out.println("clear timeline");
		dots.clear();
		dataLayer.getChildren().clear();
	}
	
	@Override
	public void updateTimeline() {
		double circleRadius;
	    int xpos;
	    stepSize = zoomFactor*2;
		layerContainer.setPrefWidth(rangeLength * zoomFactor * 2);    // Adjust the width of the container

		if (clusterInterval <= 1) {
			originY =  120;
			updateBaseline(originY);
			dataLayer.getChildren().removeAll(rectangles);
			dataLayer.getChildren().removeAll(dots);
			dataLayer.getChildren().addAll(dots);

			for (int i = 0; i < dots.size(); i++) {
				circleRadius = zoomFactor * yScale * 20;
				if (circleRadius < minSizeThreshold) {
					circleRadius = minSizeThreshold;
				}
				Circle c = dots.get(i);
				xpos = (int) c.getProperties().get("timeCode");
				c.setCenterY(originY);
				c.setCenterX(xpos * stepSize + 0.5);
				c.setRadius(circleRadius);
				c.setOpacity(1);
			}
		}else{
			originY =  235;
			updateBaseline(originY);
			dataLayer.getChildren().removeAll(dots);
            dataLayer.getChildren().removeAll(rectangles);
			rectangles.clear();
			allBookmarks.clear();

			long timeSeriesLength = s.getDatasetLength();
			int sectionAmount = (int) Math.ceil((double) timeSeriesLength / clusterInterval);
			int[] sectionsValuesArray = new int[sectionAmount];
			Arrays.fill(sectionsValuesArray, 0);

            // first get all bookmarks of the sample
			for(int i = 0; i< s.sampleSize; i++) {
				if (filterListSubjects.get(i)) {
					Subject sub = s.getSubject(i);
					ArrayList<Bookmark> subjectBookmarks = sub.getAllBookmarks();
					allBookmarks.addAll(subjectBookmarks);
				}
			}
			// Sort bookmarks into sections according to the chosen section length
			int currentSection = 0;
			for(int l = 0; l< timeSeriesLength; l = (int) (l + clusterInterval))  {
				 for(int m = 0; m< allBookmarks.size(); m++) {
					Bookmark cb = allBookmarks.get(m);
					long ts = cb.getTimeStamp();
					 int tp = cb.getTimePos();
					if (tp > l && tp < l+clusterInterval) {
						if (currentSection < sectionsValuesArray.length) { // Prevent index overflow
							sectionsValuesArray[currentSection]++;
						}
					}
				 }
				 currentSection++;
			}

			// draw bar chart with the height of the bar depending on the amount of bookmarks in each section
			for(int i = 0; i< sectionAmount; i++) {
				int amountOFBookmarks = sectionsValuesArray[i];
				double rectWidth = clusterInterval*stepSize;
				if (i == sectionAmount-1) {
					rectWidth = timeSeriesLength % clusterInterval;
					if (rectWidth == 0) {
						rectWidth = clusterInterval;
					}
				}
				//Rectangle r = new Rectangle((i*clusterInterval*stepSize)+0.5, originY, clusterInterval, -amountOFBookmarks);
				Rectangle r = new Rectangle((i*clusterInterval*stepSize)+0.5, originY-amountOFBookmarks*2, rectWidth, amountOFBookmarks*2);
				r.setFill(baseColor);
				r.getProperties().put("sectionNumber", i);
				r.getProperties().put("color", baseColor);
				r.setStrokeWidth(0);
				r.setOpacity(0.8);
				rectangles.add(r);
			}
			dataLayer.getChildren().addAll(rectangles);
		}
	}

	private void updateBaseline(double _y){
		dataLayer.getChildren().remove(baseLine);
		baseLine.setStartX(0);													// make a baseline at the origin (the center) of the timeline
		baseLine.setStartY(_y);
		baseLine.setEndX(rangeLength * zoomFactor * 2);
		baseLine.setEndY(_y);
		baseLine.setStroke(Color.WHITE);
		baseLine.setStrokeWidth(1.0);
		dataLayer.getChildren().addAll(baseLine);
	}

	@Override
	public void drawTimeline(int _begin, int _end) {			
	    //rangeLength = _end - _begin;												// General definitions
	    stepSize = zoomFactor*2;
	    layerContainer.setPrefWidth(rangeLength * stepSize);						//highlight color is red

	    double circleRadius;

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
					dots.add(circle);
				}
			}
		}
		makeRolloversDots(dots, hightlightColor);
		updateBaseline(originY);
		dataLayer.getChildren().addAll(dots);
		dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
	}		
}
