package medis.MEDISanalysis;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import math.geom2d.Point2D;
import java.util.ArrayList;

public class timelineSDOA extends timeline{
    ArrayList<Line> lines;
    ArrayList<Double> linieLengths;
    double originY =  120;

    float minSizeThreshold = 1f;
    Color baseColor = Color.rgb(255, 255, 255);
    Color hightlightColor = Color.rgb(181, 74, 74);
    Line baseLine;

    timelineSDOA(Sample _s, int _id, int _initialTimeCode){
        super(_s, _id, _initialTimeCode);
        timelineType = "SDOA";
        lines = new ArrayList<Line>();
        linieLengths = new ArrayList<Double>();
        baseLine = new Line();
        drawScale(0, s.getShortestDataset());
        drawTimeline(0, s.getShortestDataset());
    }

    @Override
    public void clearTimeline() {
        lines.clear();
        linieLengths.clear();
        dataLayer.getChildren().clear();
    }

    @Override
    public void updateTimeline() {

        double lineWidth = zoomFactor;
        if (lineWidth < minSizeThreshold) {lineWidth = minSizeThreshold;}
        double len;

        for (int i = 0; i < lines.size(); i++) {
            len = linieLengths.get(i);
            Line l = lines.get(i);
            l.setStartY(originY+len*yScale);
            l.setEndY(originY-len*yScale);
            l.setStartX(i*stepSize*clusterInterval+0.5);
            l.setEndX(i*stepSize*clusterInterval+0.5);
            l.setStrokeWidth(lineWidth);
            if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd())
                l.setOpacity(1);
            else l.setOpacity(0.2);
        }

        baseLine.setEndX(lines.size() * zoomFactor * 2);
        layerContainer.setPrefWidth(lines.size()*zoomFactor*2);						// Adjust the width of the container

    }

    @Override
    public void drawTimeline(int _begin, int _end) {								// function to draw the standard timeline
        clearTimeline();
        //zoomFactor = 2;
        int rangeLength = _end - _begin;											// General definitions
        //stepSize = zoomFactor*2;
        stepSize = zoomFactor*clusterInterval*2;
        // layerContainer.setPrefWidth(rangeLength * stepSize);
        double lHeight;
        double lineWidth = zoomFactor;
        if (lineWidth < minSizeThreshold) {lineWidth = minSizeThreshold;}

        for (int i = _begin; i < _end; i = i + 1) {									// Definitions specific for timeline "Average Focus of Attention"
            lHeight = s.getSDOA(i, filterListSubjects) *200;							// get line height as current Deviation of Attention
            linieLengths.add(lHeight);
            Point2D currentAFA = s.getAFA(i, filterListSubjects);					// get Vector of current Average Focus of Attention
            //double[] c = this.getColor(currentAFA);								// get color of current AFA
            Color thisColor = this.getColorObject(currentAFA);
            Double currentX = i*stepSize+0.5;
            Line line = new Line(currentX, originY+lHeight*yScale, currentX, originY-lHeight*yScale); // make lines
            line.getProperties().put("timeCode", i);
            line.getProperties().put("color", thisColor);

            line.setStroke(thisColor);			// set color for line
            if (i > s.getDataOffsetBegin() && i < s.getDataOffsetEnd())
                line.setOpacity(1);
            else line.setOpacity(0.2);

            line.setStrokeWidth(lineWidth);
            //dataLayer.setPrefWidth(currentX);
            lines.add(line);
        }

        makeRollovers(lines, hightlightColor);										// add rollover and clickability


        baseLine.setStartX(0);														// make a baseline at the origin (the center) of the timeline
        baseLine.setStartY(originY);
        baseLine.setEndX(rangeLength * zoomFactor * 2);
        baseLine.setEndY(originY);
        baseLine.setStroke(Color.WHITE);
        baseLine.setStrokeWidth(2.0);

        layerContainer.setPrefWidth(lines.size()*zoomFactor*2);						// Adjust the width of the container
        System.out.println(zoomFactor);
        dataLayer.getChildren().addAll(lines);										// add all line Nodes to parent Pane
        dataLayer.getChildren().addAll(baseLine);
        dataLayer.setPrefWidth(rangeLength*zoomFactor*2);
    }

}

