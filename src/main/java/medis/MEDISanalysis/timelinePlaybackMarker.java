package medis.MEDISanalysis;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class timelinePlaybackMarker {
		double pixelPos;							// Variable to track the Position of the Playback Head
		int timeCodePos;							// stores the current position of playback in timecode position of data
		double stepSize = 2;
		
		double startXScreen;						// Variables for dragging interaction
	    double startXScrollPane;
	    
		Line playbackLine;
		Rectangle r;
		Label l = new Label("0");
		Group g = new Group();
		Color markerColor = Color.rgb(180,75,75);
		
	timelinePlaybackMarker(){
		pixelPos = 0;
		timeCodePos = 0;
		
		playbackLine = new Line(0,0,0,250);
        r = new Rectangle(0,0,40,20);
        r.setFill(markerColor);
        r.relocate(0, 5);
		playbackLine.setStroke(markerColor);
		l.relocate(2, 7);
		l.getStyleClass().add("label");
        g.getChildren().addAll(playbackLine, r, l);
        g.getStyleClass().add("playbackmarker");
        g.relocate(0, 20);
        
        g.setOnMouseEntered(e ->{ 
        	playbackLine.setStroke(Color.WHITE);
    		r.setFill(Color.WHITE);
    		l.setTextFill(markerColor);
        });
        g.setOnMouseExited(e ->{ 
        	playbackLine.setStroke(markerColor);
    		r.setFill(markerColor);
    		l.setTextFill(Color.WHITE);
        });
        g.setOnMousePressed(evt ->{
        	startXScreen = evt.getScreenX();
        	startXScrollPane = g.getBoundsInParent().getMinX();
        });
        g.setOnMouseDragged(evt -> {
        	double distance = evt.getScreenX() - startXScreen;
        	double newXPixelPos = startXScrollPane + distance;
        	timeCodePos = (int) Math.round(newXPixelPos/stepSize);
        	if (timeCodePos < 0) {
        		timeCodePos = 0;
        	}
        	if (timeCodePos >= GUI.s.getDatasetLength()-2) {
        		timeCodePos = (int) GUI.s.getDatasetLength()-2;
        	}
        	GUI.setTimerCounter(timeCodePos);
        	GUI.visTemp.setMainTimer(timeCodePos);
        	GUI.visSpat.drawSampleVectorPlayback((int) timeCodePos); 
        	if (GUI.visVid.mediaView != null) {
	   	    	GUI.visVid.jumpTo((int) GUI.timerCounter);
	   	    }	
        	l.setText(String.valueOf(timeCodePos));
        	g.relocate(timeCodePos*stepSize-(stepSize/4), 20);
        });     
	}
	
	public Group getMarkerNode() {
		return g;
	}
	public void synchronizeStepSize(double _stepSizeNew) {
		stepSize = _stepSizeNew;
	}
	
	public void updateTimerPos(int _t) {
		timeCodePos = _t;
		String txt = String.valueOf(_t);
		l.setText(txt);
	}
	
	public int getTimerPos() {
		return timeCodePos;
	}
	
	public void moveTo(double posX) {
		g.relocate(posX, 20);	
		pixelPos = posX;
	}
}
