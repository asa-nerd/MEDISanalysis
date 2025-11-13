package medis.MEDISanalysis;

import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.geometry.Insets;
//import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
//import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Line;
//import javafx.scene.text.Font;
import javafx.stage.Stage;
//import meadisAnalysis.Sample;
//import meadisAnalysis.Subject;
//import meadisAnalysis.VisualizerSpatial;
//import javafx.scene.control.cell.PropertyValueFactory;

//import java.io.File;
//import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URI;

public class GUI {
	static Stage primaryStage;
	static VBox mainStageContainer;
	static HBox topContainer;
	static HBox bottomContainer;
	VBox topLeftContainer;
	static VBox topMiddleContainer;
	GridPane topRightContainer;
	
	static Timer globalTimer;
	static long timerCounter;
	static boolean timerPaused = false;
	static boolean globalIsPlaying = false;
	
	static VisualizerList visList;
	static TemporalDisplay tempDisplay;
	static VisualizerSpatial visSpat;
	static VisualizerTemporal visTemp;
	static VisualizerVideo visVid;
	static naviPanel mainNavipanel;	
	
	static Sample s;
	
	static Scene videoScene;
	static Stage videoStage;
	
	static URI videoURL;
	
	GUI(Stage _primaryStage, Sample _s){
		s = _s;
		primaryStage = _primaryStage;
		
		
		mainStageContainer = new VBox();
		topContainer = new HBox();
		bottomContainer = new HBox();
		topLeftContainer = new VBox();
		topMiddleContainer = new VBox();
		topRightContainer = new GridPane();
		HBox.setHgrow(topMiddleContainer, Priority.ALWAYS);
		
		topContainer.getChildren().addAll(topLeftContainer, topMiddleContainer, topRightContainer);		
		
		HBox.setMargin(topLeftContainer, new Insets( 0, 15, 0, 0 ) );
	    HBox.setMargin(topMiddleContainer, new Insets( 0, 15, 0, 0 ) );
	    
	    // External Window: Video Visualizer
	 	// --------------------------------------
	    visVid = new VisualizerVideo();
	    
		// Bottom: Temporal Visualizer
	 	// --------------------------------------
	    visTemp = new VisualizerTemporal(s);										// initialize visualizer 
	    ScrollPane t = visTemp.getTemporalContainer();
	    bottomContainer.prefWidthProperty().bind(primaryStage.widthProperty());
	    t.prefWidthProperty().bind(bottomContainer.widthProperty());
	    t.prefHeightProperty().bind(bottomContainer.heightProperty());
	    bottomContainer.getChildren().add(t);
			    
			    
		// Top-Left: Main Navigation
		// --------------------------------------
		
	    mainNavipanel = new naviPanel(s);
	    VBox mB = mainNavipanel.getNaviContainer();
	    topLeftContainer.getChildren().add(mB);
	       
			 
	    // Top-Middle: Data Table
		// --------------------------------------
	    visList = new VisualizerList();
	    tempDisplay = new TemporalDisplay();
	    
	    topMiddleContainer.getChildren().addAll(visList.getVisualizerListContainer(), tempDisplay.getTemporalDisplayContainer());
	    
			    
		// Top-Right: Spatial Visualizer
		// --------------------------------------
	    visSpat = new VisualizerSpatial(s);
	    VBox spatialConatiner = visSpat.getSpatialContainer();
	    topRightContainer.getChildren().add(spatialConatiner);

	    // Menu Bar
	    // --------------------------------------
	    MenuBarFX mbfx = new MenuBarFX(primaryStage);
	    VBox mbfxContainer = mbfx.getMainMenu();
	    
	    // Set whole GUI Scene
	    // --------------------------------------
	    mainStageContainer.getChildren().addAll(mbfxContainer, topContainer, bottomContainer);
	    topContainer.setPadding(new Insets(20, 20, 10, 20));
	    bottomContainer.setPadding(new Insets(10, 20, 20, 20));
	    bottomContainer.setMinHeight(400);  
	}
	

	public VBox getLayout() {
		return mainStageContainer;
	}
	
	public static void startPlayback() {
    	if (globalIsPlaying != true && visSpat.thisSpatializer != null) {
   	    	globalTimer = new Timer();
   	    	if (timerPaused == true) {
   	    		timerPaused = false;
   	    	}else {
   	    		timerCounter = 0;
   	    	}
   		    TimerTask task = new TimerTask(){
   	   	        public void run(){ 
	   	   	      Platform.runLater(() -> {					// runLater() is necessary for threading, eventually replace with JavaFX timeline
	   	   	    		
	   	   	    	    if (timerCounter < s.getShortestDataset()) {
		   	   	    	    visTemp.movePlaybackLines((int) timerCounter);
		   	   	            tempDisplay.updateTimerDisplay((int) timerCounter);
		   	   	    	    visSpat.drawSampleVectorPlayback((int) timerCounter); 
		   	   	    	    timerCounter ++;
	   	   	    	    } else {
	   	   	    	    	stopPlayback();
	   	   	    	    }
	              });
   	   	        }		   	   	        
   	   	    };
	   	    globalTimer.scheduleAtFixedRate(task, 0, 500l);
	   	    globalIsPlaying = true;
	   	    if (visVid.mediaView != null) {
	   	    	visVid.playVideo();
	   	    }
	    	} else {
	    		
	    	}
    	
    }
	
	public static void pausePlayback() {
		if (globalIsPlaying == true) {
   	    	timerPaused = true;
   	    	globalIsPlaying= false;
   	    	globalTimer.cancel();
   	    	globalTimer.purge();
   	    	if (visVid.mediaView != null) {
	   	    	visVid.pauseVideo();
	   	    }	   	    	
	    }		
	}

	public static void stopPlayback() {
		if (globalIsPlaying == true || timerPaused == true) {
   	    	timerCounter = 0;
   	    	timerPaused = false;
   	    	globalTimer.cancel();
   	    	globalTimer.purge();
   	    	globalIsPlaying= false;
   	    	visTemp.movePlaybackLines((int) timerCounter);
            tempDisplay.updateTimerDisplay((int) timerCounter);
    	    visSpat.drawSampleVectorPlayback((int) timerCounter);
  	    	    
   	    	if (visVid.mediaView != null) {
	   	    	visVid.stopVideo();
	   	    }	
		} 
	}
	
	public static void setTimerCounter(int _t) {
		timerCounter = _t;
		visTemp.movePlaybackLines((int) timerCounter);
		tempDisplay.updateTimerDisplay((int) timerCounter);
	    //visSpat.drawSampleVector((int) timerCounter);	// TODO 
	    if (visVid.mediaView != null) {
   	    	visVid.jumpTo(_t);
   	    }	
	}
	
	public static int getTimerCounter() {
		return (int) timerCounter;
	}
	
	public static void makeVideoWindow(URI _f) {
		Group g = visVid.getVisualizerListContainer();
		if (videoScene == null) {
			videoScene = new Scene(g, 640, 360);
		}
		if(videoStage == null) {
			videoStage = new Stage();
		}
		videoScene.setFill(Color.rgb(36,46,64));
		videoStage.setScene(videoScene);
		videoStage.show();
		videoStage.setAlwaysOnTop(true);
		visVid.initVideo(_f);
	}
	
	public static void clearVideo() {
		if(videoStage != null) {
			videoStage.close();
			visVid.clearVideoPlayer();
		}  	 	
	}
	
	public static void setVideoStartOffset(int _t) {
		visVid.setStartOffset(_t);
		visVid.stopVideo();
	}
	public static void setDataBeginOffset(int _t) {
		s.setDataOffsetBegin(_t);
		visTemp.redrawAllTimelines();
		System.out.println((int) s.offsetEnd);
		s.getSubjectList().forEach((sub) -> {
			sub.updateData((int) s.offsetBegin, (int) s.offsetEnd);
		});
		s.makeSampleActivityTotalAverage();
		visList.updateTable();
	}
	public static void setDataEndOffset(int _t) {
		s.setDataOffsetEnd(_t);
		visTemp.redrawAllTimelines();
		s.getSubjectList().forEach((sub) -> {
			sub.updateData((int) s.offsetBegin, (int) s.offsetEnd);
		});
		s.makeSampleActivityTotalAverage();
		visList.updateTable();
	}
	
	public static void updateSampleTable() {
		visList.clearTable();						// first erase all entries from table
		s.getSubjectList().forEach((s) -> {			// loop through subjects list
			visList.addSubject(s);					// and add each subject
		});
	}
}
