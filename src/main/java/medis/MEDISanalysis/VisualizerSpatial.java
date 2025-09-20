package medis.MEDISanalysis;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.ArrayList;

public class VisualizerSpatial {
	Sample s;
	
	VBox mainContainer;
	HBox controlsContainer;
	HBox canvasContainer;

	VBox left;
	VBox middle;
	VBox right;
	
	HBox leftBottom;
	VBox CheckBoxesLeft;
	VBox CheckBoxesRight;
	
	int scaleTriangle = 350;
	int heightTriangle = (int) Math.round(Math.sqrt(3)/2 * scaleTriangle);
	Color timerSignColor = Color.rgb(180,75,75);
	
	int currentTimeCode;
	
	ArrayList<Circle> subjectCircles;
	ArrayList<Line> subjectLines;
	ArrayList<Boolean> filterListSubjectsVisualizer;
	Circle circleAFA;
	CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;
	Button exportPdfButton, exportBitmapButton, exportJsonButton;
	
	spatializer thisSpatializer;
	boolean spatializerLoaded = false;
	boolean labelsActive = false;
	boolean clusterActive = false;
	boolean convexHullActive = false;
	boolean OverlayOneActive = false;
	boolean OverlayTwoActive = false;
	boolean deviationActive = false;
	
	VisualizerSpatial(Sample _s){
		s = _s;
		filterListSubjectsVisualizer = new ArrayList<Boolean>();
		
		mainContainer = new VBox();
		controlsContainer = new HBox();
		canvasContainer = new HBox();

		left = new VBox();
		middle = new VBox();
		right = new VBox();
		
		leftBottom = new HBox();
		
		CheckBoxesLeft = new VBox();
		CheckBoxesRight = new VBox();
		
		mainContainer.setPrefSize(554, 406);
		mainContainer.setMinSize(554, 406);
		mainContainer.setMaxSize(554, 406);

		controlsContainer.setPrefSize(554, 80);
		canvasContainer.setPrefSize(400, 326);
		controlsContainer.setPrefSize(554, 80);
		canvasContainer.setPrefSize(400, 326);
		canvasContainer.getStyleClass().add("temporal-display-triangle-container");
		controlsContainer.getStyleClass().add("temporal-display-container");	
		
		left.setPrefSize(184, 76);
		middle.setPrefSize(184, 76);
		right.setPrefSize(184, 76);
		
		
		HBox displayContainer = new HBox();
		HBox.setMargin(displayContainer, new Insets(8, 0, 0, 0));
		
		displayContainer.getChildren().addAll(CheckBoxesLeft, CheckBoxesRight);
		
		CheckBoxesLeft.setPrefSize(92, 76);
		CheckBoxesRight.setPrefSize(92, 76);
		
		left.getStyleClass().add("temporal-display-block");
		middle.getStyleClass().add("temporal-display-block");
		right.getStyleClass().add("temporal-display-block");
		
		controlsContainer.getChildren().addAll(left, middle, right);
		
		mainContainer.getChildren().addAll(canvasContainer, controlsContainer);

		Label leftLabel = new Label("Display");
		Label middleLabel = new Label("Export");
		Label rightLabel = new Label("Timelines / Filter");
		leftLabel.getStyleClass().add("labelnavi");
		middleLabel.getStyleClass().add("labelnavi");
		rightLabel.getStyleClass().add("labelnavi");
		
		HBox exportContainer = new HBox();
		VBox.setMargin(exportContainer, new Insets(12, 0, 0, 0));
		exportPdfButton = new Button();
		exportBitmapButton = new Button();
		exportJsonButton = new Button();
		exportPdfButton.getStyleClass().add("main-navi-button");
		exportBitmapButton.getStyleClass().add("main-navi-button");
		exportJsonButton.getStyleClass().add("main-navi-button");
		HBox.setMargin(exportPdfButton, new Insets(0, 8, 0, 0));
		HBox.setMargin(exportBitmapButton, new Insets(0, 8, 0, 0));
		HBox.setMargin(exportJsonButton, new Insets(0, 8, 0, 0));
		exportPdfButton.setPadding(Insets.EMPTY);
		exportBitmapButton.setPadding(Insets.EMPTY);
		exportJsonButton.setPadding(Insets.EMPTY);
		exportPdfButton.setPrefSize(31,31);
		exportBitmapButton.setPrefSize(31,31);
		exportJsonButton.setPrefSize(31,31);
		exportPdfButton.setGraphic(new ImageView("icons/export-1.png"));
		exportBitmapButton.setGraphic(new ImageView("icons/export-2.png"));
		exportJsonButton.setGraphic(new ImageView("icons/export-3.png"));
		exportContainer.getChildren().addAll(exportPdfButton, exportBitmapButton, exportJsonButton);
		
		exportPdfButton.setOnMousePressed(e ->{ 	
			PrinterJob job = PrinterJob.createPrinterJob();
            if(job != null){
         		job.showPrintDialog(Main.getPrimaryStage());
                boolean printed = job.printPage(canvasContainer);
                if (printed) {
                    job.endJob();
                }
            }
	    });
		
		//CheckBox cb1 = new CheckBox("Colors");
		cb2 = new CheckBox("ID Labels");
		cb5 = new CheckBox("Sections 1");
		cb6 = new CheckBox("Sections 2");
		cb7 = new CheckBox("Deviation");
		cb3 = new CheckBox("Cluster");
		cb4 = new CheckBox("Bag Plot");
		
		EventHandler<ActionEvent> cb2event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)  { if (cb2.isSelected())  	labelsActive = true;   else    	labelsActive = false;  
            drawSampleVectorPlayback(currentTimeCode);
            }
        };
        EventHandler<ActionEvent> cb3event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)  { if (cb3.isSelected())  	clusterActive = true;   else    	clusterActive = false;  
            drawSampleVectorPlayback(currentTimeCode);
            }
        };
        EventHandler<ActionEvent> cb4event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)  { if (cb4.isSelected())  	convexHullActive = true;   else    	convexHullActive = false;  
            drawSampleVectorPlayback(currentTimeCode);
            }
        };
        EventHandler<ActionEvent> cb5event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)  { 
            	if (cb5.isSelected()) {
            		OverlayOneActive = true;   
            		if (cb6.isSelected())
            			cb6.selectedProperty().set(false);
            			OverlayTwoActive = false;  
            	}else {
            		OverlayOneActive = false;
            		
            	}
            drawSampleVectorPlayback(currentTimeCode);
            }
        };
        EventHandler<ActionEvent> cb6event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)  { 
            	if (cb6.isSelected()) {
            		OverlayTwoActive = true;
            		if (cb5.isSelected())
            			cb5.selectedProperty().set(false);
            		  	OverlayOneActive = false;
            	}else {
            		OverlayTwoActive = false;  
            	}
            drawSampleVectorPlayback(currentTimeCode);
            }
        };
        EventHandler<ActionEvent> cb7event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)  { if (cb7.isSelected())  	deviationActive = true;   else    	deviationActive = false;  
            drawSampleVectorPlayback(currentTimeCode);
            }
        };
        cb2.setOnAction(cb2event);
        cb3.setOnAction(cb3event);
        cb4.setOnAction(cb4event);
        cb5.setOnAction(cb5event);
        cb6.setOnAction(cb6event);
        cb7.setOnAction(cb7event);
        

        
        labelsActive = true;
        OverlayOneActive = true;
		
        CheckBoxesLeft.getChildren().addAll(cb2, cb5, cb6);
        CheckBoxesRight.getChildren().addAll(cb7, cb3, cb4);
		
		left.getChildren().addAll(leftLabel, displayContainer);
		middle.getChildren().addAll(middleLabel, exportContainer);
		right.getChildren().addAll(rightLabel);
		
		deactivateButtons();
 		
	}
	
	public void activateButtons(String timelineType){
		switch (timelineType){
			case "2DTRIANGULAR":
				cb2.setDisable(false);
				cb5.setDisable(false);
				cb6.setDisable(false);
				cb7.setDisable(false);
				exportPdfButton.setDisable(false);
				cb2.selectedProperty().set(true);
				cb5.selectedProperty().set(true);
				break;
			case "1DCARTESIAN":
				cb2.setDisable(false);
				//cb5.setDisable(false);
				//cb6.setDisable(false);
				cb7.setDisable(false);
				exportPdfButton.setDisable(false);
				cb2.selectedProperty().set(true);
				break;
		}

	}
	
	public void deactivateButtons(){
		cb2.setDisable(true);
		cb3.setDisable(true);
		cb4.setDisable(true);
		cb5.setDisable(true);
		cb6.setDisable(true);
		cb7.setDisable(true);
		exportPdfButton.setDisable(true);
		exportBitmapButton.setDisable(true);
		exportJsonButton.setDisable(true);
		exportPdfButton.setDisable(true);
	}
	
	public void makeSpatializerElement(Sample _s, String _type) {
		
		switch(_type) {
			case "1DCARTESIAN":
				thisSpatializer = new spatializer1dCartesian(_s);
				canvasContainer.getChildren().add(thisSpatializer.getSpatializer());
				spatializerLoaded = true;
				drawSampleVectorPlayback(currentTimeCode);
				break;
			case "1DBOOKMARKS":
				thisSpatializer = new spatializer1dBookmarks(_s);
				canvasContainer.getChildren().add(thisSpatializer.getSpatializer());
				spatializerLoaded = true;
				drawSampleVectorPlayback(currentTimeCode);
				break;
			case "2DTRIANGULAR":
				thisSpatializer = new spatializer2dTriangular(_s);
				canvasContainer.getChildren().add(thisSpatializer.getSpatializer());
				spatializerLoaded = true;
				drawSampleVectorPlayback(currentTimeCode);
				break;

		}			
	}
	
	public VBox getSpatialContainer() {
		return mainContainer;
	}
	
	public void makeFilterListSubjects() {
		for(int i = 0; i < s.SubjectsList.size(); i++) {
			filterListSubjectsVisualizer.add(Boolean.TRUE);
		}
	}
		
	public void updateFilterListSubjects(int _t, ArrayList<Boolean> _filterListSubjects) {
		filterListSubjectsVisualizer = _filterListSubjects;
		if (spatializerLoaded) {
			thisSpatializer.drawSampleVector(_t, _filterListSubjects);
		}
	}
	
	public void clearSpatial() {
		thisSpatializer.clearDataCanvas();
		thisSpatializer = null;
	}

	public void removeSpatializer() {
		canvasContainer.getChildren().clear();
		System.out.println("Try to delete");
		thisSpatializer = null;
	}
	
	public void drawSampleVectorPlayback(int _t) {
		currentTimeCode = _t;


		thisSpatializer.drawSampleVector(_t, filterListSubjectsVisualizer);
		if (convexHullActive)
			thisSpatializer.drawConvexHUll(_t, filterListSubjectsVisualizer);
		if (labelsActive)
			thisSpatializer.drawSampleIDs(_t, filterListSubjectsVisualizer);
		if (OverlayOneActive)
			thisSpatializer.drawOverlayOne();
		if (OverlayTwoActive)
			thisSpatializer.drawOverlayTwo();
		if (deviationActive)
			thisSpatializer.drawDeviation(_t, filterListSubjectsVisualizer);
	}
	
	public void drawSection(int _begin, int _end, ArrayList<Boolean> filterList) {									// empty the data Pane 
		thisSpatializer.drawSection(_begin, _end, filterList);
	}
		
	public void drawSampleVector(int _t, ArrayList<Boolean> filterList){
		thisSpatializer.drawSampleVector(_t, filterList);
	}	
}
