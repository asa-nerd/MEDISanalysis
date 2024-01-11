package medis.MEDISanalysis;

import java.net.URI;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.util.Duration;

public class VisualizerVideo {
	Group g;
	Media media;
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	StackPane vbox;
	Duration startOffset;
	
	VisualizerVideo(){
		startOffset = Duration.millis(0);
		g = new Group();
		vbox = new StackPane();
		vbox.setAlignment(Pos.BASELINE_CENTER);
		g.getChildren().add(vbox);
	}
	
	public void setStartOffset(int _t) {
		startOffset = Duration.millis(_t);
		Duration d = mediaPlayer.getStartTime();
		Duration so = d.add(startOffset);
		mediaPlayer.seek(so);
		System.out.print(so);
		GUI.timerCounter = 0;
	    GUI.timerPaused = false;
	    if(GUI.globalIsPlaying == true) {
	    	GUI.globalTimer.cancel();
		    GUI.globalTimer.purge();
		    GUI.globalIsPlaying = false;
	    }
	    
	    GUI.visTemp.movePlaybackLines((int) GUI.timerCounter);
        GUI.tempDisplay.updateTimerDisplay((int) GUI.timerCounter);
	    GUI.visSpat.drawSampleVectorPlayback((int) GUI.timerCounter);
	    	    
	    if (mediaView != null) {
	    	stopVideo();
   	    }
	}
	
	public Group getVisualizerListContainer() {
		return g;
	}
	
	public void playVideo() {
		mediaPlayer.play();
	}
	
	public void pauseVideo() {
		Duration nt = Duration.millis(GUI.timerCounter*500);
		Duration so = nt.add(startOffset);
		mediaPlayer.seek(so);										// make sure that the video is not between two timecodes
		mediaPlayer.pause();										// video then trails of timecode on restart
																	// so position it on current measure timecode
		
	}
	public void stopVideo() {
		Duration d = mediaPlayer.getStartTime();
		Duration so = d.add(startOffset);
		mediaPlayer.seek(so);
		mediaPlayer.pause();
		System.out.print(so);
	}
	
	public void jumpTo(int _t) {
		if (_t > 0) {
			long newTime = _t*500;
			Duration nt = Duration.millis(newTime);
			Duration so = nt.add(startOffset);
			System.out.println(newTime);
			mediaPlayer.seek(so);
		}
	}
	
	public void initVideo(URI u) {
		String n = u.toString();
		media = new Media(n);
	    mediaPlayer = new MediaPlayer(media);  
	    mediaView = new MediaView (mediaPlayer);
	    mediaView.fitWidthProperty().bind(GUI.videoStage.widthProperty());
	    vbox.getChildren().add(mediaView);
	    
	}
	
	public void clearVideoPlayer() {
		startOffset = Duration.millis(0);
		vbox.getChildren().clear();
	}
}
