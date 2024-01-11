package medis.MEDISanalysis;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TemporalDisplay {
	HBox mainContainer; 
	HBox left;
	HBox middle;
	HBox right;
	VBox tcBox;
	VBox sBox;
	Label tc;
	Label s;
	Text tcText;
	Text sText;
	ImageView iconSeconds;
	ImageView iconTimecode;
	
	Duration timerDuration;
	
	
	TemporalDisplay(){
		iconSeconds = new ImageView("file:gfx/icons/timeDisplay-1.png");
		iconTimecode = new ImageView("file:gfx/icons/timeDisplay-2.png");
		
		mainContainer = new HBox();
		left = new HBox();
		middle = new HBox();
		right = new HBox();
		tcBox = new VBox();
		sBox = new VBox();
		
		tc = new Label("Timecode");
		s = new Label("Real time");
		
		tcText = new Text("0");
		sText = new Text("0");
		
		mainContainer.setPrefSize(554, 80);
		left.setPrefSize(184, 76);
		middle.setPrefSize(184, 76);
		right.setPrefSize(184, 76);
		mainContainer.getStyleClass().add("temporal-display-container");
		left.getStyleClass().add("temporal-display-block");
		middle.getStyleClass().add("temporal-display-block");
		right.getStyleClass().add("temporal-display-block");
		
		
		tcBox.getStyleClass().add("temporal-display-label-block");
		sBox.getStyleClass().add("temporal-display-label-block");
		tc.getStyleClass().add("labelnavi");
		s.getStyleClass().add("labelnavi");
		tcText.getStyleClass().add("temporal-display-text");
		sText.getStyleClass().add("temporal-display-text");
		tcText.setFill(Color.WHITE);
		sText.setFill(Color.WHITE);

		
		tcBox.getChildren().addAll(tc, tcText);
		sBox.getChildren().addAll(s, sText);
		
		
		left.getChildren().addAll(iconSeconds, tcBox);
		middle.getChildren().addAll(iconTimecode, sBox);
		
		mainContainer.getChildren().addAll(left, middle, right);
	}
	
	public HBox getTemporalDisplayContainer() {
		return mainContainer;
	}
	
	public void updateTimerDisplay(int _t) {
		String txt = String.valueOf(_t);
		tcText.setText(txt);
		
		timerDuration= new Duration(_t*500);
		String sec = String.valueOf(timerDuration.toSeconds());
		sText.setText(sec);
	}
}
