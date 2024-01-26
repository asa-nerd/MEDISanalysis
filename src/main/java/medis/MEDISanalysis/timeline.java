package medis.MEDISanalysis;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import math.geom2d.Point2D;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

public class timeline {
	
	HBox mainContainer;
	VBox guiContainer;
	Pane visualContainer;
	ScrollPane scrollContainer;
	StackPane layerContainer;
	Pane scaleLayer;
	Pane dataLayer; 
	Pane sectionLayer;
	Pane markerLayer;
	Pane playbackLayer;
	
	VBox buttonPanelRight;
	Group buttonCloseTL;
	Group buttonMoveTL;
	Group labelTL;
	
	Sample s;
	int id;
	
	double zoomFactor = 1;
	double yScale = 1;
	double clusterInterval = 1;
	double stepSize = zoomFactor*2;
	double minStepSizeZoom;									// used for the zoom slider
	double maxStepSizeZoom;
	int zoomFirstTimeCode;
	int zoomLastTimeCode;
	
	Slider zoomXSlider;
	Slider zoomYSlider;
	Slider clusterIntervalSlider;
	Label zoomXLabel;
	Label zoomYLabel;
	Label clusterIntervalLabel;
	
	
	timelinePlaybackMarker playMarker;
	ArrayList<Line> gridLines;
	ArrayList<timelineMarker> markerList;
	int markerCounter = 0;

	timelineScale scale;
	
	String timelineType;
	ArrayList<Boolean> filterListSubjects;
	 
	timeline(Sample _s, int _id, int _initialTimeCode){
		s = _s;
		id = _id;
		scale = new timelineScale(s.getShortestDataset());
		//scale.updateScale(layerContainer.getPrefWidth(), stepSize);
		zoomFactor = 1;
		gridLines = new ArrayList<Line>();
		markerList = new ArrayList<timelineMarker>();
		playMarker = new timelinePlaybackMarker();
		
		filterListSubjects = new ArrayList<Boolean>();
		
		mainContainer = new HBox();
		guiContainer = new VBox();
		visualContainer = new Pane();
		scrollContainer = new ScrollPane();
		layerContainer = new StackPane();
		scaleLayer = new Pane();
		dataLayer = new Pane();
		sectionLayer = new Pane();
		markerLayer = new Pane();
		playbackLayer = new Pane();
		scaleLayer.setStyle("-fx-background-color: rgba(255,255,255, 0.1);");
		
		scaleLayer.setPickOnBounds(false);										// Make all Layers of Timeline transparent for MouseClicks
		sectionLayer.setPickOnBounds(false);
		markerLayer.setPickOnBounds(false);
		playbackLayer.setPickOnBounds(false);
				
		mainContainer.getStyleClass().add("timeline");
		
		buttonPanelRight = new VBox();
		
		VBox.setMargin(mainContainer,new Insets(10,0,10,0));
		HBox.setMargin(guiContainer,new Insets(5,0,5,0));
		HBox.setMargin(visualContainer,new Insets(5,0,5,0));
		HBox.setMargin(buttonPanelRight,new Insets(5,0,5,0));
		
		mainContainer.prefWidthProperty().bind(GUI.bottomContainer.widthProperty());
		
		visualContainer.setPrefSize(1200, 220);
		
		HBox.setHgrow(visualContainer, Priority.ALWAYS);
		scrollContainer.prefWidthProperty().bind(visualContainer.widthProperty());
		scrollContainer.setPrefHeight(250);
		
		layerContainer.setPrefSize(200, 237);
		layerContainer.prefHeightProperty().set(237);
		//layerContainer.setMaxHeight(220);
		scaleLayer.setPrefHeight(237);
		
		playbackLayer.getChildren().add(playMarker.getMarkerNode());					// Add Playback Marker Node
		playbackLayer.getChildren().add(scale.getScaleNode());							// Add Scale Marker Node

		layerContainer.getChildren().addAll(scaleLayer, dataLayer, markerLayer, sectionLayer, playbackLayer);
		scrollContainer.setContent(layerContainer);
		visualContainer.getChildren().add(scrollContainer);		
		
		HBox.setMargin(guiContainer,new Insets(5,0,5,0));
		//buttonPanelRight.getStyleClass().add("buttonPanelRight");
		buttonCloseTL = new Group();
		buttonCloseTL.getStyleClass().add("rbutton");
		Rectangle bcbg = new Rectangle(0,0,21,21);
		bcbg.setFill(Color.WHITE);
		buttonCloseTL.setOnMouseEntered(e ->{ bcbg.setFill(Color.rgb(112,112,112)); });
		buttonCloseTL.setOnMouseExited(e ->{ bcbg.setFill(Color.WHITE); });
		buttonCloseTL.setOnMousePressed(e ->{
			GUI.visTemp.discardTimeline(id); 
			//GUI.visSpat.removeMenu(id);
		});
		
		VBox.setMargin(buttonCloseTL,new Insets(0,0,6,6));
		Line l1 = new Line(6,6,15,15);
		Line l2 = new Line(6,15,15,6);
		buttonCloseTL.getChildren().addAll(bcbg, l1, l2);
		
		buttonMoveTL = new Group();
		Rectangle bmbg = new Rectangle(0,0,21,21);	
		bmbg.setFill(Color.WHITE);
		//bmbg.getStyleClass().add("rbutton");
		buttonMoveTL.setOnMouseEntered(e ->{ bmbg.setFill(Color.rgb(112,112,112)); });
		buttonMoveTL.setOnMouseExited(e ->{ bmbg.setFill(Color.WHITE); });
		VBox.setMargin(buttonMoveTL,new Insets(0,0,6,6));
		Line bm1 = new Line(3,5.5,17,5.5);
		Line bm2 = new Line(3,10.5,17,10.5);
		Line bm3 = new Line(3,15.5,17,15.5);
		buttonMoveTL.getChildren().addAll(bmbg, bm1, bm2, bm3);
		
		Text t = new Text();
		Rectangle lbg = new Rectangle(0,0,21,21);	
		lbg.setFill(Color.WHITE);	
		labelTL = new Group(lbg, t);
		VBox.setMargin(labelTL,new Insets(0,0,6,6));
		buttonPanelRight.getChildren().addAll(buttonCloseTL, labelTL, buttonMoveTL);	
		
		mainContainer.getChildren().addAll(guiContainer, visualContainer, buttonPanelRight);
		
        MenuPullDown m1 = new MenuPullDown("Config");
		MenuItem c2=new MenuItem("Follow Playback");
		c2.setDisable(true);
		//MenuItem c3=new MenuItem("Discard Timeline");
		m1.getItems().add(c2);
		VBox.setMargin(m1,new Insets(0,0,6,8));
		
		
		MenuPullDown m3 = new MenuPullDown("Filter");
		
		for (int i = 0; i < s.SubjectsList.size(); i++) {
			Subject sub = s.SubjectsList.get(i);
			CheckMenuItem c = new CheckMenuItem("Person "+sub.getId());
			c.getProperties().put("id", i);
			c.setSelected(true);
			c.setOnAction((e) -> { 
				toggleFilterListSubjects((int) c.getProperties().get("id"));// update subjects list for visTemp 
				
				int currentPosition = playMarker.getTimerPos();
				GUI.visSpat.updateFilterListSubjects(currentPosition, filterListSubjects);	// send the updated list to visSpat
				clearTimeline();
				drawTimeline(0, s.getShortestDataset());
			});
			
			m3.getItems().add(c);
		}
		
		VBox.setMargin(m3,new Insets(6,0,6,8));
        
		MenuPullDown m4 = new MenuPullDown("Export");
		//MenuItem e1=new MenuItem("PNG");
		MenuItem e2=new MenuItem("PDF – Current View");
		MenuItem e3=new MenuItem("PDF - Timeline");
		MenuItem e4=new MenuItem("JSON");
		MenuItem e5=new MenuItem("Sound Wave");
		
		e4.setDisable(true);
		e5.setDisable(true);
		
		m4.getItems().addAll(e2,e3,e4,e5);
		VBox.setMargin(m4,new Insets(6,0,6,8));
		
		
		maxStepSizeZoom = 10;											// standard max zoom is 10
		double lData = s.getShortestDataset() * stepSize;				// visual width of the dataset stepSize is 2 initially
		double lView = visualContainer.getPrefWidth();					// visual width of Layer Container node

		minStepSizeZoom = (double) lView/ (double) lData;				// now calculate for zoom slider the minimum step size 	
																		// so all data fits nicely in the display when maximum zoomed out
		if (minStepSizeZoom > maxStepSizeZoom) {						// for short data sets we have to adjust the max (10) to not being smaller than the min
			maxStepSizeZoom = minStepSizeZoom*2;
		}
		
		minStepSizeZoom = 2;
		zoomFactor = minStepSizeZoom;
		stepSize = zoomFactor*2;
		
		
        VBox sliderContainer = new VBox();
        VBox.setMargin(sliderContainer,new Insets(12,6,6,8));
		zoomXSlider = new Slider(minStepSizeZoom,maxStepSizeZoom, minStepSizeZoom);
        zoomYSlider = new Slider(0.1, 5, 1);
        clusterIntervalSlider = new Slider(1, 100, 1);
        zoomXLabel = new Label("Zoom X");
        zoomYLabel = new Label("Zoom Y");
        clusterIntervalLabel = new Label("Cluster Interval");
        VBox.setMargin(zoomXLabel,new Insets(2,0,6,0));
        VBox.setMargin(zoomYLabel,new Insets(2,0,6,0));
        VBox.setMargin(clusterIntervalLabel,new Insets(2,0,6,0));
        clusterIntervalSlider.setDisable(true);
        zoomXSlider.setPrefSize(90, 20);
        zoomYSlider.setPrefSize(90, 20);
        clusterIntervalSlider.setPrefSize(90, 20);
        zoomXSlider.setMaxSize(90, 20);
        zoomYSlider.setMaxSize(90, 20);
        clusterIntervalSlider.setMaxSize(90, 20);
        zoomXLabel.getStyleClass().add("sliderlabel");
        zoomYLabel.getStyleClass().add("sliderlabel");
        clusterIntervalLabel.getStyleClass().add("sliderlabel");
        sliderContainer.getChildren().addAll( zoomXSlider, zoomXLabel, zoomYSlider, zoomYLabel, clusterIntervalSlider, clusterIntervalLabel);
        
        guiContainer.getChildren().addAll(m1,m3,m4,sliderContainer);

		/*e1.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
			File file = fileChooser.showSaveDialog(null);
			
		}});*/

		e2.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent event) {
			// Creating a PdfWriter
			String dest = "/Users/andreas/Desktop/sample.pdf";
            PdfWriter writer = null;
            try {
                writer = new PdfWriter(dest);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            // Creating a PdfDocument
			PdfDocument pdfDoc = new PdfDocument(writer);

			// Creating a Document
			Document document = new Document(pdfDoc);

			// Creating a new page
			PdfPage pdfPage = pdfDoc.addNewPage();

			// Creating a PdfCanvas object
			PdfCanvas canvas = new PdfCanvas(pdfPage);

			// Initial point of the line
			canvas.moveTo(100, 300);

			// Drawing the line
			canvas.lineTo(500, 300);

			// Closing the path stroke
			canvas.closePathStroke();

			// Closing the document
			document.close();
			System.out.println("PDF Created");

		   /*PrinterJob job = PrinterJob.createPrinterJob();
           if(job != null){
			   System.out.println("pdf");
        		job.showPrintDialog(Main.getPrimaryStage());
        		boolean printed = job.printPage(scrollContainer);
                if (printed) {
                    job.endJob();
                }else{
                    System.out.println("Print failed");
                }
           }*/
		}});
		
		e3.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent event) {
           PrinterJob job = PrinterJob.createPrinterJob();
           if(job != null){
        		job.showPrintDialog(Main.getPrimaryStage());
                boolean printed = job.printPage(dataLayer);
                if (printed) {
                    job.endJob();
                }else{
                    System.out.println("Print failed");
                }
           }
	    }});
		
		zoomFirstTimeCode = 0;
		zoomLastTimeCode = (int) Math.floor(1400/stepSize);
				//s.getShortestDataset();
		
		zoomXSlider.valueProperty().addListener((observable, oldValue, newValue) -> {	// Adding Listener to value property.
			double currentScrollPaneX = scrollContainer.getHvalue();
        	zoomFactor = (Double) newValue;
        	stepSize = zoomFactor*2;												// calculate new step size for this timeline
        	updateTimeline();														// Update all UI Elements according to new Zoom Level
        	updatePlaybackHead();
        	updateMarkers(stepSize);
        	updateSections(stepSize);
        	//scale.updateScale(layerContainer.getBoundsInParent().getWidth(), stepSize);
        	scale.updateScale(layerContainer.getPrefWidth(), stepSize);
        	//scale.updateScale(5000, stepSize);
        	updateScale();
        	scaleLayer.setPrefWidth(layerContainer.getBoundsInParent().getWidth());
        	playbackLayer.setPrefWidth(layerContainer.getBoundsInParent().getWidth());
        	//scaleLayer.setPrefWidth(5000);
        	scrollContainer.setHvalue(currentScrollPaneX);
		});
		
		zoomYSlider.valueProperty().addListener((observable, oldValue, newValue) -> {	// Adding Listener to value property.
			yScale = (Double) newValue;
			for(int i = 0; i < markerList.size(); i++) {
				timelineMarker tlm = markerList.get(i);
				tlm.section.setYScale(yScale);
				tlm.section.updateScaledSectionHeight();
			}
        	updateTimeline();
		});  
		
		clusterIntervalSlider.valueProperty().addListener((observable, oldValue, newValue) -> {	// Adding Listener to value property.
			clusterInterval = Math.round((Double) newValue);
			updateTimeline();
			System.out.println(clusterInterval);
		});  
		
		
		//layerContainer.prefWidthProperty().set(scrollContainer.getWidth());
		//layerContainer.prefWidthProperty().set(visualContainer.getBoundsInParent().getWidth());
		scale.setWidth(layerContainer.getBoundsInParent().getWidth());
		
		scale.updateScale(layerContainer.getPrefWidth(), stepSize);
		updatePlaybackHead();
		updatePlaybackTimer(_initialTimeCode);
		makeFilterListSubjects();
	}
	
	public HBox getTimeline() {
		return mainContainer;
	}
	
	public void setHighlight() {
		Color highlightColor = Color.rgb(230,220,100,1);
		//mainContainer.setFill(highlightColor);
		//mainContainer.setStyle("-fx-background-color: rgba(230,220,100,1);");
		mainContainer.setStyle("-fx-border-color: rgba(230,220,100,1);");
		
	}
	
	public void unHighlight() {
		//mainContainer.setStyle("-fx-background-color: rgba(230,220,100,0);");	
		mainContainer.setStyle("-fx-border-color: rgba(230,220,100,0);");
	}
	
	public void makeFilterListSubjects() {
		for(int i = 0; i < s.SubjectsList.size(); i++) {
			filterListSubjects.add(Boolean.TRUE);
		}
	}
	
	public ArrayList<Boolean> getFilterListSubjects() {
		return filterListSubjects;
	}
	
	public void toggleFilterListSubjects(int n) {
		if (filterListSubjects.get(n) == Boolean.TRUE) {
			filterListSubjects.set(n, Boolean.FALSE);
		}else {
			filterListSubjects.set(n, Boolean.TRUE);
		}
	}
	
	
	public JSONObject getTimeLineDataJSON() {
		JSONObject timelineData = new JSONObject();
		
		timelineData.put("type", timelineType);
		
		final JSONArray markersArray = new JSONArray();
		
		markerList.forEach((ma) -> {
			double tc = ma.getMarkerTimeCode();
			JSONObject markerdata = new JSONObject();
			markerdata.put("timecode", tc);
			if (ma.section != null) {
				markerdata.put("section", true);
				markerdata.put("section-end", ma.section.getEndTimeCode());
			}else {
				markerdata.put("section", false);
			}
			
			markersArray.add(markerdata);
		});
		timelineData.put("markers", markersArray);
		return timelineData;		
	}
	
	public void clearTimeline() {
		
	}

	public void updateTimeline() {
		
	}
	
	public void drawTimeline(int _begin, int _end) {
		
	}
	/*
	public Point2D makeSectionAverage(int _begin, int _end, ArrayList<Boolean> filterList) {
		Point2D p = new Point2D(0,0);
		return p;
	}
	*/
	public void makeRollovers(ArrayList<Line> list, Color _c) {
		list.forEach((l) -> {
			l.setOnMouseEntered(e ->{ l.setStroke(_c);	});
			l.setOnMouseExited(e ->{ 
				Color rgb = (Color) l.getProperties().get("color");
				l.setStroke(rgb);
				//double[] c = (double[]) l.getProperties().get("color");
        		//l.setStroke(Color.rgb((int) c[0], (int) c[1], (int) c[2]));
			});
			l.setOnMousePressed(e ->{ 
				int tc = (int) l.getProperties().get("timeCode");
				if (e.getButton() == MouseButton.SECONDARY || e.isControlDown()) {
					makeTimelineMarker(tc, markerCounter, stepSize, filterListSubjects);
                } else {
                	GUI.visTemp.setMainTimer(tc);
		        	GUI.visSpat.drawSampleVectorPlayback((int) tc); 
					GUI.setTimerCounter(tc);
                }	
				//makeTimelineMarker(tc, markerCounter, stepSize, filterListSubjects);
			});			
		});
	}
	
	public void makeRolloversDots(ArrayList<Circle> list, Color _c) {
		list.forEach((c) -> {
			c.setOnMouseEntered(e ->{ 
				c.setStroke(_c);
				c.setFill(_c);
			});
			c.setOnMouseExited(e ->{ 
				Color rgb = (Color) c.getProperties().get("color");
				//double[] rgb = (double[]) c.getProperties().get("color");
        		c.setStroke(rgb);
        		c.setFill(rgb);
			});
			c.setOnMousePressed(e ->{ 
				int tc = (int) c.getProperties().get("timeCode");
				if (e.getButton() == MouseButton.SECONDARY || e.isControlDown()) {
					makeTimelineMarker(tc, markerCounter, stepSize, filterListSubjects);
                } else {
                	GUI.visTemp.setMainTimer(tc);
		        	GUI.visSpat.drawSampleVectorPlayback((int) tc); 
					GUI.setTimerCounter(tc);
                }	
			});			
		});
	}
	
	public void makeTimelineMarker(double _xPos, int _id, double _stepSize, ArrayList<Boolean> _filterListSubjects) {
		timelineMarker tm = new timelineMarker(s, _xPos, _id, stepSize, _filterListSubjects);
		tm.yScale = yScale;
    	makeMarkerContextMenu(tm);
    	markerList.add(tm);
    	markerLayer.getChildren().add(tm.getMarkerNode());
    	markerCounter ++;
	}
	// Manage Scale 
	// --------------------------------------------------------------
	
	public void drawScale(int _begin, int _end) {
		for (int i = _begin; i < _end; i+= 20) {
			Line l = new Line(i*stepSize+0.5, 0, i*stepSize+0.5, 240);
			l.setStroke(Color.rgb(255,255,255, 0.2));
			l.setStrokeWidth(1);
			gridLines.add(l);
						
		}
		scaleLayer.getChildren().addAll(gridLines);
	}
	
	public void updateScale() {
		for (int i = 0; i < gridLines.size(); i++) {
			Line l = gridLines.get(i);
			l.setStartX(i*20*stepSize+0.5);
			l.setEndX(i*20*stepSize+0.5);
		}
	}
	
	public void drawOriginLine(/*int _begin, int _end, */double _y) {
		Line l = new Line(0,_y,1400,_y);
		l.setStroke(Color.rgb(181, 74, 74));
		l.setStrokeWidth(1);
		scaleLayer.getChildren().addAll(l);
	}
	
	// Playback Head 
	// --------------------------------------------------------------
	
	public void updatePlaybackHead() {
		playMarker.moveTo(playMarker.getTimerPos()*stepSize);
		playMarker.synchronizeStepSize(stepSize);
	}
	
	
	public void updatePlaybackTimer(int _t) {
		playMarker.updateTimerPos(_t);
		playMarker.moveTo((double)_t*stepSize);
	}
	
	// Marker functions
	// --------------------------------------------------------------
	
	public ArrayList<timelineMarker> getMarkerList(){
		return markerList;
	}
	
	
	public void updateMarkers(double stepSize) {
		markerList.forEach( (m) -> { m.updateMarkerPos(stepSize);});
	}
	
	public void deleteMarker(int _id) {
		final int id = _id;
		markerList.removeIf(m -> (m.id == id)); 
	}
	
	public double findNextMarkerPosTC(double _currentPos) {					// returns the TimeCode of the next Marker
		double nextPos = s.getShortestDataset();							// start with assuming that end of sample is end of section aka there is only one marker
		for(int i = 0; i < markerList.size(); i++) {
			timelineMarker m = markerList.get(i);
			double checkX = m.getMarkerTimeCode();
			if (checkX < nextPos && checkX > _currentPos) {
				nextPos = checkX;
			}
		}	
		return nextPos;
	}
	
	/*public ContextMenu getContextMenuNode() {
		return contextMenu;
	}*/
	
	public void makeMarkerContextMenu(timelineMarker _tm) {
		ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("MenuPullDown");
        MenuItem cm1 = new MenuItem("Make new Section"); 
        MenuItem cm2 = new MenuItem("Delete Section"); 
        MenuItem cm3 = new MenuItem("Average of Section"); 
        CheckMenuItem cm4 = new CheckMenuItem("Display Section in Spatial Visualizer");
        MenuItem cm5 = new MenuItem("Expand Section until next Marker");
        MenuItem cm6 = new MenuItem("Delete Marker");
        contextMenu.getItems().addAll(cm1,cm2,cm3, cm4, cm5, cm6); 
        
        cm2.setDisable(true);
        cm3.setDisable(true);
        cm4.setDisable(true);
        cm5.setDisable(true);
		_tm.makeSectionContextMenu(contextMenu);
		
		
		// Event Handlers for Context Menu (as Lambda Functions)
		cm1.setOnAction(e ->{ 										// Menu: Make new Section for this Marker
			if(_tm.hasSection != true) {
				double currentTC = _tm.getMarkerTimeCode();
				makeSection(_tm, currentTC, currentTC+100); 
				_tm.section.yScale = yScale;
				cm1.setDisable(true);
				cm2.setDisable(false);
		        cm3.setDisable(false);
		        cm4.setDisable(false);
		        cm5.setDisable(false);
			}									
		});
		cm2.setOnAction(e ->{ 
			if(_tm.hasSection == true) {
				markerLayer.getChildren().remove(_tm.getSection().getSectionNode());
				_tm.clearSection();
				cm1.setDisable(false);
				cm2.setDisable(true);
		        cm3.setDisable(true);
		        cm4.setDisable(true);
		        cm5.setDisable(true);
			}
		});
		cm3.setOnAction(e ->{ 
			System.out.println("Average of Section");	
			if (_tm.hasSection == true) {
				Point2D aos = s.getSectionAFA((int) _tm.section.getStartTimeCode(), (int) _tm.section.getEndTimeCode(), filterListSubjects);
				//Point2D aos = makeSectionAverage((int) _tm.section.getStartTimeCode(), (int) _tm.section.getEndTimeCode(), filterListSubjects);
				Color c = getColorObject(aos);
				double h = s.getSectionDOA((int) _tm.section.getStartTimeCode(), (int) _tm.section.getEndTimeCode(), filterListSubjects);
				_tm.section.sectionAverage.setFill(c);
				_tm.section.sectionAverage.setHeight(h*200*2); // all lines are currently scaled by 200. *2 is needed because the lines go up and down from the center of the timeline (*2).
				_tm.section.sectionAverage.relocate(0, 95-(h*200));  // y: originY is 120 but Marker-Section is shifted by 25 on the y --> so originY is 95
				_tm.section.sectionAverage.setVisible(true);
			}
		});
		cm4.setOnAction(e ->{ 	
			if (_tm.hasSection == true) {
				markerList.forEach( (m) -> { 						// unselect other sections
					ContextMenu c = m.l.getContextMenu();
					CheckMenuItem mi = (CheckMenuItem) c.getItems().get(3);
					m.showSectionInSpatial = false;
					
					if(m != _tm) { 
						mi.setSelected(false);
						//_tm.r.setFill(_tm.markerColor);
						//_tm.markerLine.setStroke(_tm.markerColor);
						timelineSection s = m.getSection();
						s.background.setFill(s.markerColor);
						s.grip.setFill(s.gripColor);
					}else { System.out.println("spared one"); }
				});
				
				if (((CheckMenuItem)e.getSource()).isSelected()){
					_tm.showSectionInSpatial = true;
					//_tm.r.setFill(_tm.hightlightColor);
					//_tm.markerLine.setStroke(_tm.hightlightColor);
					timelineSection s = _tm.getSection();
					s.background.setFill(s.markerHightlightColor);
					s.grip.setFill(s.gripHightlightColor);
					GUI.visSpat.clearSpatial();
					GUI.visSpat.drawSection((int) _tm.section.getStartTimeCode(), (int) _tm.section.getEndTimeCode(), filterListSubjects); 
					//System.out.println((int) _tm.section.getStartTimeCode()+","+(int) _tm.section.getEndTimeCode());
				}else {
					_tm.showSectionInSpatial = false;
					//_tm.r.setFill(_tm.markerColor);
					//_tm.markerLine.setStroke(_tm.markerColor);
					timelineSection s = _tm.getSection();
					s.background.setFill(s.markerColor);;
					s.grip.setFill(s.gripColor);
					GUI.visSpat.clearSpatial();
				}
			}
		});
		cm5.setOnAction(e ->{ 
			double searchStartPos = _tm.section.startTimeCode;
			double nexMarkerPos = findNextMarkerPosTC(searchStartPos);
			_tm.section.updateSectionLength(nexMarkerPos);
			});
		cm6.setOnAction(e ->{ 
			if(_tm.showSectionInSpatial == true) {
				GUI.visSpat.clearSpatial();
			}
			markerLayer.getChildren().remove(_tm.getMarkerNode());		// Remove Marker from Layer Node
			if (_tm.hasSection) {
				markerLayer.getChildren().remove(_tm.getSection().getSectionNode()); // Remove Selection from Layer Node 
			}
			
			markerList.removeIf(m -> (m.id == _tm.id)); 				// Remove from Marker ArrayList
		});
	}
	
	// Section functions
	// --------------------------------------------------------------
	
	public void makeSection(timelineMarker _tm, double _startTimeCode, double _endTimeCode) {
		//double startPos = _tm.getMarkerTimeCode();
		double startPos = _startTimeCode;
		//double endPos = findNextMarkerPosTC(startPos);
		double endPos = _endTimeCode;
		timelineSection section = new timelineSection(s, startPos, endPos, stepSize, filterListSubjects); 	
		_tm.setSection(section);
		markerLayer.getChildren().add(_tm.getSection().getSectionNode());
	}
	
	public void updateSections(double _newStepSize) {
		markerList.forEach((m) -> { 
			if (m.section != null) {
				m.section.updateSectionPos(_newStepSize); 
			}
		});
	}
	
	// Helper Functions to calculate and draw timeline
	
	public static double[] getColor(Point2D _p) {
		double[] rgb = {0,0,0};
		Point2D topCorner = new Point2D(0.0, -0.577350269189626);
		Point2D rightCorner = new Point2D(0.5, 0.288675134594813);
		Point2D leftCorner = new Point2D(-0.5, 0.288675134594813);
		Point2D currentPoint = _p;
		double propRed = distancePointLine(currentPoint, leftCorner, topCorner); 		// get red
		rgb[0] = mapValue(propRed, 0.0, 0.88/2, 0, 255); 								// map Value and save it to rgb array
		double propGreen = distancePointLine(currentPoint, topCorner, rightCorner); 	// get green
	    rgb[1] = mapValue(propGreen, 0, 0.88/2, 0, 255); 								// map Value and save it to rgb array
		double propBlue = distancePointLine(currentPoint, leftCorner, rightCorner);		// get blue
	    rgb[2] = mapValue(propBlue, 0, 0.88/2, 0, 255); 								// map Value and save it to rgb array				
		return rgb;
		
	}
	
	public static Color getColorObject(Point2D _p) {
		Color rgb;
		Point2D topCorner = new Point2D(0.0, -0.577350269189626);
		Point2D rightCorner = new Point2D(0.5, 0.288675134594813);
		Point2D leftCorner = new Point2D(-0.5, 0.288675134594813);
		Point2D currentPoint = _p;
		double propRed = distancePointLine(currentPoint, leftCorner, topCorner); 		// get red
		propRed = mapValue(propRed, 0.0, 0.88/2, 0, 255); 								// map Value and save it to rgb array
		double propGreen = distancePointLine(currentPoint, topCorner, rightCorner); 	// get green
		propGreen = mapValue(propGreen, 0, 0.88/2, 0, 255); 								// map Value and save it to rgb array
		double propBlue = distancePointLine(currentPoint, leftCorner, rightCorner);		// get blue
		propBlue = mapValue(propBlue, 0, 0.88/2, 0, 255); 								// map Value and save it to rgb array				
		rgb = Color.rgb((int) propRed, (int) propGreen, (int) propBlue);
		
		return rgb;
	}
	
	public static double distancePointLine(Point2D PV, Point2D LV1, Point2D LV2) {
		// PV = Point ; LV1 = Line Point 1 ; LV2 = Line Point 2
		//double dist = Math.abs((L2.y()-L1.y())*P.x()-(L2.x()-L1.x())*P.y()+L2.x()*L1.y()-L2.y()*L1.x()) / Math.sqrt(Math.sqrt((L2.y()-L1.y())+Math.sqrt(L2.x()-L1.x())));
										
	    Point2D slope = new Point2D (LV2.x() - LV1.x(), LV2.y() - LV1.y());				// slope of line
	    double lineLengthi = slope.x() * slope.x() + slope.y() * slope.y();     		// squared length of line;

	    Point2D s = new Point2D(PV.x() - LV1.x(), PV.y() - LV1.y());
		double ti = (s.x()* slope.x()+ s.y()*slope.y())/lineLengthi;
		Point2D p = new Point2D(slope.x() * ti, slope.y() * ti);						// crawl the line acoording to its slope to distance t
		Point2D projectionOnLine = new Point2D(LV1.x()+p.x(), LV1.y()+p.y());			// add the starting coordinates			
		Point2D subber = new Point2D(projectionOnLine.x()- PV.x(), projectionOnLine.y()- PV.y()); // now calculate the distance of the measuring point to the projected point on the line
		double dist = (float) Math.sqrt(subber.x() * subber.x() + subber.y() * subber.y());
		return dist;
	}
	
	public double map(double value, double istart, double istop, double ostart, double ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}
	
	public static double mapValue(double _value, double _valMin, double _valMax, double _destMin, double _destMax){
        double proportion = (_value - _valMin) / (_valMax - _valMin);  					// calculate the proportion between 0 and 1
        double scal = _destMax - _destMin;                             
        double result = (proportion * scal) + _valMin;                 					// now scale it according to the desired scale
        if (result > _destMax){result = _destMax;}                  					// coinstrain value to 0 - 255
        if (result < _destMin){result = _destMin;}
        return result;
    }
}
