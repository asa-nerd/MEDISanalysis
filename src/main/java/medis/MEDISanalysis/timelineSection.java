package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import math.geom2d.Point2D;

public class timelineSection {
	Sample s;
	double startTimeCode;
	double endTimeCode;
	double sectionLength;
	double stepSize;
	ArrayList<Boolean> filterListSubjects;
	Rectangle sectionAverage;
	double sectionAverageHeight; 
	
	double startXScreen;					// Variables for dragging interaction
    double startXScrollPane;
    double yScale;
    
	Label endLabel;
	Rectangle background;
	Rectangle grip;
	Color markerColor = Color.rgb(230,220,100,0.2);
	Color gripColor = Color.rgb(230,220,100);
	Color markerHightlightColor = Color.rgb(181, 74, 74, 0.2);	
	Color gripHightlightColor = Color.rgb(181, 74, 74);	
	Group g = new Group();
	
	timelineSection(Sample _s, double _startTimeCode, double _endTimeCode, double _stepSize, ArrayList<Boolean> _filterListSubjects){
		s = _s;
		startTimeCode = _startTimeCode;
		endTimeCode = _endTimeCode;
		sectionLength = endTimeCode - startTimeCode;
		stepSize = _stepSize;
		filterListSubjects = _filterListSubjects;
		endLabel = new Label(String.valueOf(endTimeCode));
		endLabel.getStyleClass().add("label");
		endLabel.relocate(sectionLength*stepSize-40, 0);
		background = new Rectangle(0,0, sectionLength*stepSize, 190);
		background.setFill(markerColor);
		background.getStyleClass().add("background");
		grip = new Rectangle(sectionLength*stepSize-2.5, 0, 5, 190);
		grip.getStyleClass().add("grip");
		grip.setFill(gripColor);
		sectionAverage = new Rectangle(0,120,sectionLength*stepSize, 20);
		sectionAverage.setStroke(gripColor);
		sectionAverage.setVisible(false);
		g.getChildren().addAll(background, sectionAverage, grip, endLabel);
		g.getStyleClass().add("section");
		g.relocate(startTimeCode*stepSize, 25);
		
		grip.setOnMousePressed(evt ->{
        	startXScreen = evt.getScreenX();
        	startXScrollPane = g.getBoundsInParent().getMaxX()-5;
        });
		grip.setOnMouseDragged(evt -> {
        	double distance = evt.getScreenX() - startXScreen;
        	double newXPixelPos = startXScrollPane + distance;
        	double currentDragEndCode = Math.round(newXPixelPos/stepSize);
			
        	if (currentDragEndCode >= startTimeCode + 1 && currentDragEndCode < GUI.s.getShortestDataset()) {
        		endTimeCode = Math.round(newXPixelPos/stepSize);
	        	endLabel.setText(String.valueOf(endTimeCode));
	        	sectionLength = endTimeCode - startTimeCode;
	        	background.setWidth(sectionLength*stepSize);
	        	endLabel.relocate(sectionLength*stepSize-40, 0);
	        	grip.relocate(sectionLength*stepSize, 0);
	        	updateSectionAverage(startTimeCode, endTimeCode, filterListSubjects);
	        	GUI.visSpat.drawSection((int) startTimeCode, (int) endTimeCode, filterListSubjects);
	        	//GUI.visSpat.drawSection((int) startTimeCode, (int) endTimeCode); // TODO
        	}
        });
	}
	
	public void setYScale(double _yScale) {
		yScale = _yScale;
	}
	
	public void updateScaledSectionHeight() {
		double scaledHeight = sectionAverageHeight*200*2*yScale;
		sectionAverage.setHeight(scaledHeight); // all lines are currently scaled by 200. *2 is needed because the lines go up and down from the center of the timeline (*2).
		sectionAverage.relocate(0, 95-(scaledHeight/2));
	}
		
	private void updateSectionAverage(double _start, double _end, ArrayList<Boolean> _filterListSubjects) {
		Point2D aos = s.getSectionAFA((int) _start,  (int) _end, _filterListSubjects);
		Color c = s.getColorObject(aos);
		sectionAverageHeight = s.getSectionDOA((int) _start,  (int) _end, _filterListSubjects);
		double scaledHeight = sectionAverageHeight*200*2*yScale;
		
		sectionAverage.setWidth(sectionLength*stepSize);
		sectionAverage.setFill(c);
		sectionAverage.setHeight(scaledHeight); // all lines are currently scaled by 200. *2 is needed because the lines go up and down from the center of the timeline (*2).
		sectionAverage.relocate(0, 95-(scaledHeight/2));
	}
	
	public Group getSectionNode() {
		return g;
	}
	
	public void updateSectionLength(double _endTimeCode) {
		endTimeCode = _endTimeCode;
		endLabel.setText(String.valueOf(endTimeCode));
    	sectionLength = endTimeCode - startTimeCode;
    	background.setWidth(sectionLength*stepSize);
    	endLabel.relocate(sectionLength*stepSize-40, 0);
    	grip.relocate(sectionLength*stepSize, 0);
		updateSectionAverage(startTimeCode, endTimeCode, filterListSubjects);
	}
	
	public void updateSectionPos(double _stepSizeNew) {
		g.relocate(startTimeCode*_stepSizeNew, 20);
		grip.relocate(sectionLength*_stepSizeNew-2.5, 0);
		endLabel.relocate(sectionLength*_stepSizeNew-40, 180);
		background.setWidth(sectionLength*_stepSizeNew);
	}
	
	public void moveSection(double _newTimeCode) {
		
		startTimeCode = _newTimeCode;
		g.relocate(startTimeCode*stepSize, 25);	
		endTimeCode = startTimeCode+sectionLength;
		endLabel.setText(String.valueOf(endTimeCode));
		updateSectionAverage(startTimeCode, endTimeCode, filterListSubjects);
	}
	
	public void synchronizeStepSize(double _stepSizeNew) {
		stepSize = _stepSizeNew;
	}
	
	public double getStartTimeCode() {
		return startTimeCode;
	}
	public double getEndTimeCode() {
		return endTimeCode;
	}
}
