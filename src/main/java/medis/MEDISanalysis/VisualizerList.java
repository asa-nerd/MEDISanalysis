package medis.MEDISanalysis;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class VisualizerList {
	Group g;
	HBox container;
	TableView<Subject> table;
	TableColumn <Subject, Long> tableColumnId;
	TableColumn <Subject, String> tableColumnRecordName;
	TableColumn <Subject, Long> tableColumnAge;
	TableColumn <Subject, Double> tableColumnGender;
	TableColumn <Subject, String> tableColumnEducation;
	TableColumn <Subject, Double> tableColumnactivityTotal;
	TableColumn <Subject, Double> tableColumnSubjectActivityCount;
	TableColumn <Subject, Double> tableColumnSubjectActivityAverage;
	
	VisualizerList(){
		g = new Group();
		container = new HBox();
		table = new TableView<>();
		table.setPlaceholder(new Label("Please load JSON data and a video file."));
		table.setEditable(true);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		tableColumnId = new TableColumn<Subject, Long>("ID");
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnRecordName = new TableColumn<Subject, String>("Record Name");
		tableColumnRecordName.setCellValueFactory(new PropertyValueFactory<>("recordName"));
		tableColumnAge = new TableColumn<Subject, Long>("Age");
		tableColumnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		tableColumnGender = new TableColumn<Subject, Double>("Gender");
		tableColumnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		tableColumnGender.setCellFactory(tc -> new TableCell<Subject, Double>() {
			@Override
			protected void updateItem(Double value, boolean empty) {
				super.updateItem(value, empty);
				if (empty) {
					setText(null);
				} else {
					setText(String.format("%.0f", value)); // Limiting to 0 decimal places
				}
			}
		});
		tableColumnEducation =  new TableColumn<Subject, String>("Education");
		tableColumnEducation.setCellValueFactory(new PropertyValueFactory<>("education"));
		tableColumnactivityTotal  = new TableColumn<Subject, Double>("∑ Distance");
		tableColumnactivityTotal.setCellFactory(tc -> new TableCell<Subject, Double>() {
			@Override
			protected void updateItem(Double value, boolean empty) {
				super.updateItem(value, empty);
				if (empty) {
					setText(null);
				} else {
					setText(String.format("%.3f", value)); // Limiting to 3 decimal places
				}
			}
		});
		tableColumnactivityTotal.setCellValueFactory(new PropertyValueFactory<>("subjectActivitySum"));
		tableColumnSubjectActivityCount  = new TableColumn<Subject, Double>("# Activities");
		tableColumnSubjectActivityCount.setCellValueFactory(new PropertyValueFactory<>("subjectActivityCount"));
		tableColumnSubjectActivityCount.setCellFactory(tc -> new TableCell<Subject, Double>() {
			@Override
			protected void updateItem(Double value, boolean empty) {
				super.updateItem(value, empty);
				if (empty) {
					setText(null);
				} else {
					// Set text with formatted value
					setText(String.format("%.0f", value)); // Limiting to 0 decimal places
				}
			}
		});
		tableColumnSubjectActivityAverage  = new TableColumn<Subject, Double>("ø Distance");
		tableColumnSubjectActivityAverage.setCellValueFactory(new PropertyValueFactory<>("subjectActivityAverage"));
		tableColumnSubjectActivityAverage.setCellFactory(tc -> new TableCell<Subject, Double>() {
			@Override
			protected void updateItem(Double value, boolean empty) {
				super.updateItem(value, empty);
				if (empty) {
					setText(null);
				} else {
					// Set text with formatted value
					setText(String.format("%.3f", value)); // Limiting to 3 decimal places
				}
			}
		});

		tableColumnRecordName.setSortable(false);
		tableColumnId.setPrefWidth(40);
		tableColumnRecordName.setPrefWidth(140);
		tableColumnAge.setPrefWidth(40);
		tableColumnEducation.setPrefWidth(180);
		tableColumnEducation.setEditable(true);
		
		tableColumnEducation.setSortable(false);
		tableColumnEducation.setStyle( "-fx-alignment: CENTER-LEFT;");

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

