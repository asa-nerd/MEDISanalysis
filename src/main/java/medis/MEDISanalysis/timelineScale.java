package medis.MEDISanalysis;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class timelineScale {
	Pane gLabels;
	Pane gLines;
	Pane bg;
	HBox container;
	Rectangle bgRect;
	
	StackPane h;
	
	int sampleDataLength;
	int firstVisibleTimeCode;
	int lastVisibleTimeCode;
	int labelTimeCodeInterval = 10;
	
	ArrayList<Label> labelList;
	ArrayList<Line> lineList;
	
	timelineScale(int _sampleDataLength){
		sampleDataLength = _sampleDataLength;
		gLabels = new Pane();
		gLines = new Pane();
		bg = new Pane();
		container = new HBox();
		h = new StackPane();
		
		labelList = new ArrayList<Label>();
		lineList = new ArrayList<Line>();
		
		bgRect = new Rectangle(0,0,1200,20);
		
		bgRect.setFill(Color.rgb(255, 255, 255,0.5));
		
		container.getStyleClass().add("scale");
		for(int i = 0; i < sampleDataLength; i +=labelTimeCodeInterval) {
			Label l = new Label(String.valueOf(i));
			Line li = new Line(0,15,0,20);
			l.getStyleClass().add("label");
			li.setStroke(Color.rgb(255, 255, 255));
			labelList.add(l);
			lineList.add(li);
		}
		gLabels.getChildren().addAll(labelList);
		gLines.getChildren().addAll(lineList);
		bg.getChildren().add(bgRect);
		h.getChildren().addAll(bg, gLabels, gLines);	
	}
	
	public StackPane getScaleNode() {
		return h;
	}
	
	public void setWidth(double _width) {
		bgRect.setWidth(_width);
	}
	
	public void updateScale(double _width, double _stepSize) {
		bgRect.setWidth(_width);
		labelList.forEach((l) -> {l.setVisible(false);});
		lineList.forEach((l) -> {l.setVisible(false);});
		
		int viewStep = 0;
		if (_stepSize < 1) {
			viewStep = 4;
		}else if(_stepSize >= 1 && _stepSize < 3) {
			viewStep = 2;			
		}else if(_stepSize >= 3) {
			viewStep = 1;			
		}
		
		for(int i = 0; i < labelList.size(); i += viewStep) {
			Label l = labelList.get(i);
			Line li = lineList.get(i);
			l.setVisible(true);
			li.setVisible(true);
			l.setTranslateX(i*_stepSize*labelTimeCodeInterval);
			//li.setTranslateX(i*_stepSize*labelTimeCodeInterval);
			li.setStartX(i*_stepSize*labelTimeCodeInterval);
			li.setEndX(i*_stepSize*labelTimeCodeInterval);
		}
		
	}
}
