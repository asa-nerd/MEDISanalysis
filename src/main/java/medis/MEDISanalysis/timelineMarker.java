package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class timelineMarker {
	Sample s;
	Line markerLine;
	int id;
	double stepSize;
	double xPosTimeCode;
	double sectionStartTC;
	double sectionEndTC;
	boolean hasSection;
	boolean showSectionInSpatial = false;
	
	Polygon MarkerTriangle = new Polygon();
	Rectangle r;
	Label l = new Label();
	Group g = new Group();
	
	timelineSection section;
	ArrayList<Boolean> filterListSubjects;
	
	Color markerColor = Color.rgb(230,220,100);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	double startXScreen;					// Variables for dragging interaction
    double startXScrollPane;
    double yScale;
	
	timelineMarker(Sample _s, double _xPos, int _id, double _stepSize, ArrayList<Boolean> _filterListSubjects) {
		s = _s;
		xPosTimeCode = _xPos;
		id = _id;
		stepSize = _stepSize;
		filterListSubjects = _filterListSubjects;
		hasSection = false;
		
		Polygon MarkerTriangle = new Polygon();
		MarkerTriangle.getPoints().addAll(0.0, 0.0, -5.0,-5.0, +5.0,-5.0);
		MarkerTriangle.setFill(markerColor);
		r = new Rectangle(0,200,30,220);
		r.setFill(markerColor);
		l.setTextFill(Color.BLACK);
		l.relocate(0, 200);
		l.getStyleClass().add("label");
		l.setText(String.valueOf((int) xPosTimeCode));
		markerLine = new Line(0,0,0,200);
		markerLine.setStroke(markerColor);
		g.getChildren().addAll(MarkerTriangle,markerLine, r, l);
		
		g.getStyleClass().add("marker");
        g.relocate(xPosTimeCode*_stepSize-5, 15);


        g.setOnMousePressed(evt ->{
        	startXScreen = evt.getScreenX();
        	startXScrollPane = g.getBoundsInParent().getMinX();
			if (section != null) {
				GUI.visSpat.drawSection((int) section.getStartTimeCode(), (int) (section.getEndTimeCode()), filterListSubjects);
			}
        });
        g.setOnMouseDragged(evt -> {
        	double distance = evt.getScreenX() - startXScreen;
        	double newXPixelPos = startXScrollPane + distance;
        	xPosTimeCode = Math.round(newXPixelPos/stepSize);
        	if (xPosTimeCode < 0) {
        		xPosTimeCode = 0;
        	}
        	if (xPosTimeCode > GUI.s.getShortestDataset()) {
        		xPosTimeCode = GUI.s.getShortestDataset();
        	}

        	l.setText(String.valueOf((int) xPosTimeCode));
        	g.relocate(xPosTimeCode*stepSize-5, 15);
        	if (section != null) {

        		section.synchronizeStepSize(stepSize);
        		section.moveSection(xPosTimeCode);
        		//if (showSectionInSpatial == true) {
					//System.out.println("move");
        			GUI.visSpat.drawSection((int) section.getStartTimeCode(), (int) (section.getEndTimeCode()), filterListSubjects);
        		//}
        	}
        	
        });
	}
	
	
	
	public Group getMarkerNode() {
		return g;
	}
	
	//public void setSection(double _startTC, double _endTC) {
	public void setSection(timelineSection _ts) {
		section = _ts;
		section.moveSection(xPosTimeCode);
		hasSection = true;
	}
	
	public boolean hasSection() {
		return hasSection;
	}
	
	public timelineSection getSection() {
		return section;
	}
	
	public void clearSection() {
		section = null;
		hasSection = false;
	}
	
	public double getSectionStartTC() {
		return sectionStartTC;
	}
	
	public double getSectionEndTC() {
		return sectionEndTC;
		
	}
	
	public void makeSectionContextMenu(ContextMenu _contextMenu) {
		l.setContextMenu(_contextMenu); 
	}
	
	public void synchronizeStepSize(double _stepSizeNew) {
		stepSize = _stepSizeNew;
		if (section != null) {
			section.synchronizeStepSize(_stepSizeNew);
		}
	}
	
	public void updateMarkerPos(double _stepSizeNew) {
		g.relocate(xPosTimeCode*_stepSizeNew-5, 20);
		stepSize = _stepSizeNew;
	}
	
	public double getMarkerTimeCode() {
		return xPosTimeCode;
	}
		
}