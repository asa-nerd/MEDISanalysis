package medis.MEDISanalysis;

import java.io.File;
import java.net.URI;

import javafx.event.EventHandler;
//import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class naviPanel {
	
	VBox mainContainer;
	VBox project;
	VBox timelines;
	VBox video;
	
	Text projectTitle, timelinesLabel, videoLabel;
	Text recordingName, datasetLength, intervalDuration, durationSeconds;
	Sample s;
	
	TextField videoOffsetNumber, offsetBeginNumber, offsetEndNumber;
	
	Button playButton, pauseButton, stopButton, VisAFAButton, VisActivityButton, VisSubAttentionButton, VisSubActivityButton, Bookmarks_Button, Cartesian1dButton, Cartesian1dAverageButton, Cartesian1dActivityButton;
	Button setVideoOffset, setOffsetBegin,setOffsetEnd;
	Button loadVideoButton, openVideoWindowButton;
	
	naviPanel(Sample _s){
		s = _s;
		
		mainContainer = new VBox();
		project = new VBox();
		timelines = new VBox();
		video = new VBox();
		mainContainer.getChildren().addAll(project, timelines, video);
		
		mainContainer.getStyleClass().add("mainnavi");
		project.getStyleClass().add("project");
		timelines.getStyleClass().add("timelines");
		video.getStyleClass().add("video");
		
		project.setMinSize(213, 110); // 163
		project.setMaxSize(213, 110);
		project.setPrefSize(213, 110);
		
		timelines.setMinSize(213, 213); //160
		timelines.setMaxSize(213, 213);
		timelines.setPrefSize(213, 213);
		
		video.setMinSize(213, 76);
		video.setMaxSize(213, 76);
		video.setPrefSize(213, 76);		
			 
		projectTitle = new Text("Project Title");
		recordingName = new Text("Recording: ––");
		datasetLength = new Text("Length Dataset: ––");
		intervalDuration= new Text("Interval (ms): ––");
		durationSeconds = new Text("Duration (s): ––");
		
		VBox.setMargin(projectTitle, new Insets(8, 0, 8, 8));
		VBox.setMargin(recordingName, new Insets(0, 0, 4, 8));
		VBox.setMargin(datasetLength, new Insets(0, 0, 4, 8));
		VBox.setMargin(intervalDuration, new Insets(0, 0, 4, 8));
		VBox.setMargin(durationSeconds, new Insets(0, 0, 4, 8));
		
		projectTitle.getStyleClass().add("main-navi-record-title");
		recordingName.getStyleClass().add("main-navi-info-label");
		datasetLength.getStyleClass().add("main-navi-info-label");
		intervalDuration.getStyleClass().add("main-navi-info-label");
		durationSeconds.getStyleClass().add("main-navi-info-label");
		
		project.getChildren().addAll(projectTitle, recordingName, datasetLength, intervalDuration, durationSeconds);
		
		 loadVideoButton = new Button();
		 openVideoWindowButton = new Button();
		 playButton = new Button();
	     pauseButton = new Button();
	     stopButton = new Button();
	    
	    Tooltip t1 = new Tooltip("Load Video");
	    Tooltip t2 = new Tooltip("Show Video Window");
	    Tooltip t3 = new Tooltip("Play Video");
	    Tooltip t4 = new Tooltip("Pause Video");
	    Tooltip t5 = new Tooltip("Stop Video");
	    t1.getStyleClass().add("tooltip"); 
	    t2.getStyleClass().add("tooltip"); 
	    t3.getStyleClass().add("tooltip"); 
	    t4.getStyleClass().add("tooltip"); 
	    t5.getStyleClass().add("tooltip"); 
	    loadVideoButton.setTooltip(t1);
	    openVideoWindowButton.setTooltip(t2);
	    playButton.setTooltip(t3);
	    pauseButton.setTooltip(t4);
	    stopButton.setTooltip(t5);
	    //HBox buttonRow1 = new HBox();
	    
	     VisAFAButton = new Button();
	     VisActivityButton = new Button();
	     VisSubActivityButton = new Button();
	     VisSubAttentionButton = new Button();
	    
	     Bookmarks_Button = new Button();	// Bookmarks button
	     Cartesian1dButton = new Button();	// 2D-Attention button
	     Cartesian1dAverageButton = new Button();
	     Cartesian1dActivityButton = new Button();
	    
	    Tooltip t6 = new Tooltip("Timeline \n 2D Ternary \n Average Focus of Attention");
	    Tooltip t7 = new Tooltip("Timeline \n 2D Ternary \n Average Activity");
	    Tooltip t8 = new Tooltip("Timeline \n 2D Ternary \n Subjects Activity");
	    Tooltip t9 = new Tooltip("Timeline \n 2D Ternary \n Subjects Focus of Attention");
	
	    t6.getStyleClass().add("tooltip"); 
	    t7.getStyleClass().add("tooltip"); 
	    t8.getStyleClass().add("tooltip"); 
	    t9.getStyleClass().add("tooltip");
	    
	    Tooltip t10 = new Tooltip("Timeline \n 1D Linear \n Experience Marks");
	    Tooltip t11 = new Tooltip("Timeline \n 1D Linear \n Average Focus of Attention");
	    Tooltip t12 = new Tooltip("Timeline \n 1D Linear \n Subjects Focus of Attention");
	    
	    t10.getStyleClass().add("tooltip"); 
	    t11.getStyleClass().add("tooltip"); 
	    t12.getStyleClass().add("tooltip"); 
	    //t13.getStyleClass().add("tooltip");
	    
	    VisAFAButton.setTooltip(t6);
	    VisActivityButton.setTooltip(t7);
	    VisSubActivityButton.setTooltip(t8);
	    VisSubAttentionButton.setTooltip(t9);
	    
	    Bookmarks_Button.setTooltip(t10);
	    Cartesian1dButton.setTooltip(t11);
	    Cartesian1dAverageButton.setTooltip(t12);
	    //Cartesian1dActivityButton.setTooltip(t13);
	    
	    
	    videoOffsetNumber = new TextField("0");
	    setVideoOffset = new Button("Set Video Offset");
	    setVideoOffset.getStyleClass().add("main-navi-button"); 
	    HBox.setMargin(videoOffsetNumber, new Insets(0, 8, 0, 0));
	    HBox.setMargin(setVideoOffset, new Insets(0, 8, 0, 0));
	    videoOffsetNumber.setPrefSize(70,31);
	    setVideoOffset.setPrefSize(70,31);
	    	    
	    offsetBeginNumber = new TextField("0");
	    setOffsetBegin = new Button("Set Offset Begin");
	    setOffsetBegin.getStyleClass().add("main-navi-button"); 
	    HBox.setMargin(offsetBeginNumber, new Insets(0, 8, 0, 0));
	    HBox.setMargin(setOffsetBegin, new Insets(0, 8, 0, 0));
	    offsetBeginNumber.setPrefSize(70,31);
	    setOffsetBegin.setPrefSize(70,31);
	    
	    offsetEndNumber = new TextField("0");
	    setOffsetEnd = new Button("Set Offset End");
	    setOffsetEnd.getStyleClass().add("main-navi-button"); 
	    HBox.setMargin(offsetEndNumber, new Insets(0, 8, 0, 0));
	    HBox.setMargin(setOffsetEnd, new Insets(0, 8, 0, 0));
	    offsetEndNumber.setPrefSize(70,31);
	    setOffsetEnd.setPrefSize(70,31);
	    
		Label timelinesLabel = new Label("Timelines");
		Label videoLabel = new Label("Playback");
		timelinesLabel.getStyleClass().add("labelnavi");
		videoLabel.getStyleClass().add("labelnavi");
		VBox.setMargin(timelinesLabel, new Insets(11, 0, 0, 9));
		VBox.setMargin(videoLabel, new Insets(11, 0, 0, 9));
		
		VBox.setMargin(video, new Insets(6, 0, 0, 0));
		
		VisAFAButton.getStyleClass().add("main-navi-button");
		VisActivityButton.getStyleClass().add("main-navi-button");
		VisSubActivityButton.getStyleClass().add("main-navi-button");
		VisSubAttentionButton.getStyleClass().add("main-navi-button");		
		
		loadVideoButton.getStyleClass().add("main-navi-button");
		openVideoWindowButton.getStyleClass().add("main-navi-button");
		playButton.getStyleClass().add("main-navi-button");
	    pauseButton.getStyleClass().add("main-navi-button");
	    stopButton.getStyleClass().add("main-navi-button");   
	    
	    Bookmarks_Button.getStyleClass().add("main-navi-button");  
	    Cartesian1dButton.getStyleClass().add("main-navi-button");  
	    Cartesian1dAverageButton.getStyleClass().add("main-navi-button");  
	    Cartesian1dActivityButton.getStyleClass().add("main-navi-button");  
	   
	    HBox.setMargin(VisAFAButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(VisActivityButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(VisSubActivityButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(VisSubAttentionButton, new Insets(0, 8, 0, 0));
	    
	    HBox.setMargin(Bookmarks_Button, new Insets(0, 8, 0, 0));
	    HBox.setMargin(Cartesian1dButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(Cartesian1dAverageButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(Cartesian1dActivityButton, new Insets(0, 8, 0, 0));
	    
	    HBox.setMargin(loadVideoButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(openVideoWindowButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(playButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(pauseButton, new Insets(0, 8, 0, 0));
	    HBox.setMargin(stopButton, new Insets(0, 8, 0, 0));
	    
	    VisAFAButton.setPadding(Insets.EMPTY);
	    VisActivityButton.setPadding(Insets.EMPTY);
	    VisSubActivityButton.setPadding(Insets.EMPTY);
	    VisSubAttentionButton.setPadding(Insets.EMPTY);
	    Bookmarks_Button.setPadding(Insets.EMPTY);
	    Cartesian1dButton.setPadding(Insets.EMPTY);
	    Cartesian1dAverageButton.setPadding(Insets.EMPTY);
	    Cartesian1dActivityButton.setPadding(Insets.EMPTY);
	    
	    VisAFAButton.setPrefSize(31,31);
	    VisActivityButton.setPrefSize(31,31);
	    VisSubActivityButton.setPrefSize(31,31);
	    VisSubAttentionButton.setPrefSize(31,31);
	    
	    Bookmarks_Button.setPrefSize(31,31);
	    Cartesian1dButton.setPrefSize(31,31);
	    Cartesian1dAverageButton.setPrefSize(31,31);
	    Cartesian1dActivityButton.setPrefSize(31,31);
	    
	    loadVideoButton.setPrefSize(31,31);
	    openVideoWindowButton.setPrefSize(31,31);
	    playButton.setPrefSize(31,31);
	    pauseButton.setPrefSize(31,31);
	    stopButton.setPrefSize(31,31);
	    
	    VisAFAButton.setGraphic(new ImageView("icons/Timeline-1-Button.png"));
	    VisActivityButton.setGraphic(new ImageView("icons/Timeline-2-Button.png"));
	    VisSubActivityButton.setGraphic(new ImageView("icons/Timeline-3-Button.png"));
	    VisSubAttentionButton.setGraphic(new ImageView("icons/Timeline-4-Button.png"));
	    Bookmarks_Button.setGraphic(new ImageView("icons/Timeline-5-Button.png"));
	    Cartesian1dButton.setGraphic(new ImageView("icons/Timeline-6-Button.png"));
	    Cartesian1dAverageButton.setGraphic(new ImageView("icons/Timeline-7-Button.png"));
	    
	    loadVideoButton.setGraphic(new ImageView("icons/loadVideoButton.png"));
	    openVideoWindowButton.setGraphic(new ImageView("icons/openVideoWindowButton.png"));
	    playButton.setGraphic(new ImageView("icons/playButton.png"));
	    pauseButton.setGraphic(new ImageView("icons/pauseButton.png"));
	    stopButton.setGraphic(new ImageView("icons/stopButton.png"));
	     
	    loadVideoButton.setOnMousePressed(e ->{ 	
	    	 FileChooser fileChooser = new FileChooser();							// Open File Chooser
	         fileChooser.setTitle("Load Video");
	         File f = fileChooser.showOpenDialog(GUI.primaryStage);
	         if (f != null) {
	        	 URI uri;
		 		 uri = f.toURI();
	        	 GUI.makeVideoWindow(uri);
	        	 openVideoWindowButton.setDisable(false);
	     	     videoOffsetNumber.setDisable(false);
	         } 
	    });
	    
	    openVideoWindowButton.setOnMousePressed(e ->{ 
	    	GUI.makeVideoWindow(GUI.videoURL);
	    });
	    playButton.setOnMousePressed(e ->{ 		if (s.sampleSize>0) GUI.startPlayback(); 	});	    
	    pauseButton.setOnMousePressed(e ->{ 	if (s.sampleSize>0) GUI.pausePlayback();	});
	    stopButton.setOnMousePressed(e ->{ 		if (s.sampleSize>0) GUI.stopPlayback();  	});
	    
	    VisAFAButton.setOnMousePressed(e ->{ 			
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "AFA"); 
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "2DTRIANGULAR");}
	    	}
	    });	   
	    VisActivityButton.setOnMousePressed(e ->{ 		
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "ACTIVITY"); 	
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "2DTRIANGULAR");}
	    	}
	    });
	    VisSubActivityButton.setOnMousePressed(e ->{	
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "SUBJECTACTIVITY");
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "2DTRIANGULAR");}
	    	}
	    });
	    VisSubAttentionButton.setOnMousePressed(e ->{ 	
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "SUBJECTATTENTION");
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "2DTRIANGULAR");}
	    	}
	    });
	    Bookmarks_Button.setOnMousePressed(e ->{ 		
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "BOOKMARKS"); 
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "1DCARTESIAN");}
	    	}
	    });
	    Cartesian1dButton.setOnMousePressed(e ->{ 		
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "1DCARTESIAN"); 
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "1DCARTESIAN");}
	    	}
	    });
	    Cartesian1dAverageButton.setOnMousePressed(e ->{ 
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "1DCARTESIAN-AVERAGE"); 
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "1DCARTESIAN");}
	    	}
	    });
	    Cartesian1dActivityButton.setOnMousePressed(e ->{ 
	    	if (s.sampleSize>0) {
	    		GUI.visTemp.makeTimelineElement(s, "1DCARTESIAN-ACTIVITY"); 
	    		if (!GUI.visSpat.spatializerLoaded) {GUI.visSpat.makeSpatializerElement(s, "1DCARTESIAN");}
	    	}
	    });
	    
	    // Button & Return-Key Events for the Offset Fields
	    setVideoOffset.setOnMousePressed(e ->{ 
	    	Integer value1 = Integer.valueOf(videoOffsetNumber.getText());
	    	GUI.setVideoStartOffset(value1);
	    });
	    
	    videoOffsetNumber.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent k) {
	            if (k.getCode().equals(KeyCode.ENTER)) {
	            	Integer value1 = Integer.valueOf(videoOffsetNumber.getText());
	    	    	GUI.setVideoStartOffset(value1);
	            }
	        }
	    });
	    
	    setOffsetBegin.setOnMousePressed(e ->{ 
	    	Integer value1 = Integer.valueOf(offsetBeginNumber.getText());
	    	GUI.setDataBeginOffset(value1);
	    });
	    offsetBeginNumber.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent k) {
	            if (k.getCode().equals(KeyCode.ENTER)) {
	            	Integer value1 = Integer.valueOf(offsetBeginNumber.getText());
	    	    	GUI.setDataBeginOffset(value1);
	            }
	        }
	    });
	    
	    setOffsetEnd.setOnMousePressed(e ->{ 
	    	Integer value1 = Integer.valueOf(offsetEndNumber.getText());
	    	GUI.setDataEndOffset(value1);
	    });
	    offsetEndNumber.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent k) {
	            if (k.getCode().equals(KeyCode.ENTER)) {
	            	Integer value1 = Integer.valueOf(offsetEndNumber.getText());
	    	    	GUI.setDataEndOffset(value1);
	            }
	        }
	    });
	    
	    
	    deactivateButtons();
	    
	    HBox buttonTimelines = new HBox(VisAFAButton, VisActivityButton, VisSubActivityButton, VisSubAttentionButton);
	    HBox button_2_Timelines = new HBox(Bookmarks_Button, Cartesian1dButton, Cartesian1dAverageButton, Cartesian1dActivityButton);
	    HBox buttonVideoOffset = new HBox(videoOffsetNumber, setVideoOffset);
	    HBox buttonBeginOffset = new HBox(offsetBeginNumber, setOffsetBegin);
	    HBox buttonEndOffset = new HBox(offsetEndNumber, setOffsetEnd);
	   
	    HBox buttonRowVideo = new HBox(loadVideoButton, openVideoWindowButton, playButton, pauseButton, stopButton);
	    VBox.setMargin(buttonTimelines, new Insets(8, 0, 0, 9));
	    VBox.setMargin(button_2_Timelines, new Insets(8, 0, 0, 9));
	    VBox.setMargin(buttonRowVideo, new Insets(8, 0, 0, 9));
	    VBox.setMargin(buttonVideoOffset, new Insets(8, 0, 0, 9));
	    VBox.setMargin(buttonBeginOffset, new Insets(8, 0, 0, 9));
	    VBox.setMargin(buttonEndOffset, new Insets(8, 0, 0, 9));
	    timelines.getChildren().addAll(timelinesLabel, buttonTimelines, button_2_Timelines, buttonVideoOffset, buttonBeginOffset, buttonEndOffset);
		video.getChildren().addAll(videoLabel, buttonRowVideo);
	}
	
	public void activateButtons(){
		playButton.setDisable(false);
	    pauseButton.setDisable(false);
	    stopButton.setDisable(false);
		VisAFAButton.setDisable(false);
	    VisActivityButton.setDisable(false);
	    VisSubActivityButton.setDisable(false);
	    VisSubAttentionButton.setDisable(false);
	    setVideoOffset.setDisable(false);
	    setOffsetBegin.setDisable(false);
	    setOffsetEnd.setDisable(false);
	    offsetEndNumber.setDisable(false);
	    offsetBeginNumber.setDisable(false);
	}
	
	public void deactivateButtons(){
		playButton.setDisable(true);
	    pauseButton.setDisable(true);
	    stopButton.setDisable(true);
	    
	    openVideoWindowButton.setDisable(true);
	    
	    VisAFAButton.setDisable(true);
	    VisActivityButton.setDisable(true);
	    VisSubActivityButton.setDisable(true);
	    VisSubAttentionButton.setDisable(true);
	    
	    Bookmarks_Button.setDisable(true);
	    Cartesian1dButton.setDisable(true);
	    Cartesian1dAverageButton.setDisable(true);
	    Cartesian1dActivityButton.setDisable(true);
	    
	    setVideoOffset.setDisable(true);
	    setOffsetBegin.setDisable(true);
	    setOffsetEnd.setDisable(true);
	    
	    offsetEndNumber.setDisable(true);
	    offsetBeginNumber.setDisable(true);
	    videoOffsetNumber.setDisable(true);
	    
	    Bookmarks_Button.setVisible(false);
	    Cartesian1dButton.setVisible(false);
	    Cartesian1dAverageButton.setVisible(false);
	    Cartesian1dActivityButton.setVisible(false);
	    
	    Bookmarks_Button.setManaged(false);
	    Cartesian1dButton.setManaged(false);
	    Cartesian1dAverageButton.setManaged(false);
	    Cartesian1dActivityButton.setManaged(false);	    
	}
	
	public VBox getNaviContainer() {
		return mainContainer;
	}
	
	public void setProjectInfos(long _datasetLength, long _interval, double _duration, String _projectTitle) {
		projectTitle.setText(_projectTitle);
		datasetLength .setText("Dataset Length: "+_datasetLength);
		intervalDuration.setText("Interval (ms): "+_interval);
		durationSeconds.setText("Duration (s): " + _duration);
	}
	
	public void clearProjectInfos() {
		projectTitle.setText("no active project");
		datasetLength .setText("Dataset Length: –––");
		intervalDuration.setText("Interval (ms): –––");
		durationSeconds.setText("Duration (s): ––" );
	}
	
	public void clearVideoDataOffsets() {
		GUI.mainNavipanel.videoOffsetNumber.setText("0");
   	 	GUI.mainNavipanel.offsetBeginNumber.setText("0");
   	 	GUI.mainNavipanel.offsetEndNumber.setText("0");
	}
	
	
}
