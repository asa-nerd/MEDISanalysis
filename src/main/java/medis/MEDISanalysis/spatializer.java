package medis.MEDISanalysis;

import java.util.ArrayList;


import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import math.geom2d.Point2D;

public class spatializer {
	Sample s;
	StackPane canvasContainer;
	Pane chartCanvas;
	Pane dataCanvas;
	Pane calculationCanvas;
	
	ArrayList<Circle> subjectCircles;
	ArrayList<Text> subjectIDs;
	ArrayList<Line> subjectLines;
	ArrayList<Point2D> convexHullPoints;
	ArrayList<Boolean> filterListSubjectsVisualizer;
	
	Color baseColor = Color.rgb(255, 255, 255, 1);
	Color hightlightColor = Color.rgb(181, 74, 74);	
	
	spatializer(Sample _s){
		s = _s;
		subjectCircles = new ArrayList<Circle>();
        subjectLines = new ArrayList<Line>();
        subjectIDs = new ArrayList<Text>();
        convexHullPoints = new ArrayList<Point2D>();
        filterListSubjectsVisualizer = new ArrayList<Boolean>();
        
        canvasContainer =  new StackPane();
        chartCanvas = new Pane();
        dataCanvas = new Pane();
        calculationCanvas = new Pane();
        canvasContainer.getChildren().addAll(chartCanvas, dataCanvas, calculationCanvas);
       
	}
	
	public StackPane getSpatializer() {
		return canvasContainer;
	}
	
	public void drawChart() {
		
	}
	
	public void drawSection(int _begin, int _end, ArrayList<Boolean> filterList) {
	
	}
	
	public void drawSampleVector(int _t, ArrayList<Boolean> filterList){
		
	}
	
	public void drawSampleIDs(int _t, ArrayList<Boolean> filterList){
		
	}
	
	public void drawConvexHUll(int _t, ArrayList<Boolean> filterList){
		
	}
	
	public void drawCluster(int _t, ArrayList<Boolean> filterList){
		
	}
	
	public void drawOverlayOne() {
		
	}
	
	public void drawOverlayTwo() {
		
	}
	public void drawDeviation(int _t, ArrayList<Boolean> filterList) {
		
	}
	public void clearDataCanvas() {
		dataCanvas.getChildren().clear();
		subjectCircles.clear();
		subjectLines.clear();
	}
}
