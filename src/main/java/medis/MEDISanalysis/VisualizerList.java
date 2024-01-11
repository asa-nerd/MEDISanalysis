package medis.MEDISanalysis;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class VisualizerList {
	Group g;
	HBox container;
	TableView table;
	TableColumn <Long, Subject> tableColumnId;
	TableColumn <String, Subject> tableColumnRecordName;
	TableColumn <Long, Subject> tableColumnAge;
	TableColumn <Double, Subject> tableColumnGender;
	TableColumn <String, Subject> tableColumnEducation;
	TableColumn <Double, Subject> tableColumnactivityTotal;
	TableColumn <Double, Subject> tableColumnSubjectActivityCount;
	TableColumn <Double, Subject> tableColumnSubjectActivityAverage;
	
	VisualizerList(){
		g = new Group();
		container = new HBox();
		table = new TableView();
		table.setPlaceholder(new Label("Please load sample data and and a video-file."));
		table.setEditable(true);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		tableColumnId = new TableColumn<>("ID");
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnRecordName = new TableColumn<>("Record Name");
		tableColumnRecordName.setCellValueFactory(new PropertyValueFactory<>("recordName"));
		tableColumnAge = new TableColumn<>("Age");
		tableColumnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		tableColumnGender = new TableColumn<>("Gender");
		tableColumnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		tableColumnEducation =  new TableColumn<>("Education");
		tableColumnEducation.setCellValueFactory(new PropertyValueFactory<>("education"));
		tableColumnactivityTotal  = new TableColumn<>("∑ Distance");
		tableColumnactivityTotal.setCellValueFactory(new PropertyValueFactory<>("subjectActivitySum"));
		tableColumnSubjectActivityCount  = new TableColumn<>("# Activities");
		tableColumnSubjectActivityCount.setCellValueFactory(new PropertyValueFactory<>("subjectActivityCount"));
		tableColumnSubjectActivityAverage  = new TableColumn<>("ø Distance");
		tableColumnSubjectActivityAverage.setCellValueFactory(new PropertyValueFactory<>("subjectActivityAverage"));
		tableColumnRecordName.setSortable(false);
		tableColumnId.setPrefWidth(40);
		tableColumnRecordName.setPrefWidth(140);
		tableColumnAge.setPrefWidth(40);
		tableColumnEducation.setPrefWidth(180);
		tableColumnEducation.setEditable(true);
		
		tableColumnEducation.setSortable(false);
		tableColumnactivityTotal.setPrefWidth(60);
		tableColumnSubjectActivityCount.setPrefWidth(60);
		tableColumnSubjectActivityAverage.setPrefWidth(60);
		
		
		table.getColumns().addAll(tableColumnId, tableColumnGender, tableColumnAge, tableColumnEducation, tableColumnRecordName, tableColumnactivityTotal, tableColumnSubjectActivityCount, tableColumnSubjectActivityAverage);
		table.setPrefHeight(326);
		table.setMaxHeight(326);
		container.prefWidthProperty().bind(GUI.topMiddleContainer.widthProperty());
		table.prefWidthProperty().bind(container.widthProperty());
		container.getChildren().add(table);
		g.getChildren().add(container);
		g.getStyleClass().add("tablecontainer");
	}
	
	public HBox getVisualizerListContainer() {
		return container;
	}
	
	public void clearTable() {
		table.getItems().clear();
	}
	
	public void updateTable() {
		table.refresh();
	}
	
	public void addSubject(Subject _s) {
		table.getItems().add(_s);
	}	
}
