package medis.MEDISanalysis;


import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuBarFX extends MenuBar {

    MenuBar mb;
    Menu fileMenu, editMenu, controlMenu, analyzeMenu, layoutMenu, helpMenu, t1, t2, t3, t4, t5;
    MenuItem f1, f2, f3, f4, f5, f6, f7, c1, c2, c3, t0, t11, t12, t13, t41, t42, t43, t44, t45, l0, l1, h0;
    VBox vb;

    MenuBarFX(Stage stage) {
        MenuBar mb = new MenuBar();
        fileMenu = new Menu("File");
        //editMenu = new Menu("Edit");
        controlMenu = new Menu("Control");
        analyzeMenu = new Menu("Timelines");
        layoutMenu = new Menu("Layout");
        helpMenu = new Menu("Help");

        f1 = new MenuItem("New Project");
        f2 = new MenuItem("Load Project");
        f3 = new MenuItem("Save Project");
        f4 = new MenuItem("Load Sample Data");
        f5 = new MenuItem("Load Video");
        f6 = new MenuItem("About");
        f7 = new MenuItem("Quit");

        c1 = new MenuItem("Play");
        c2 = new MenuItem("Pause");
        c3 = new MenuItem("Stop");

        t0 = new MenuItem("Bookmarks");
        t1 = new Menu("Cartesian 1D");
        t2 = new Menu("Cartesian 2D");
        t3 = new Menu("Cartesian 3D");
        t4 = new Menu("Ternary 2D");
        t5 = new Menu("Ternary 3D");
        t11 = new MenuItem("Experience Marks");
        t12 = new MenuItem("Individual Focus of Attention");
        t13 = new MenuItem("Average Focus of Attention (Group)");
        t41 = new MenuItem("Individual Focus of Attention");
        t42 = new MenuItem("Individual Activity");
        t43 = new MenuItem("Average Focus of Attention (Group)");
        t44 = new MenuItem("Sum of Activity (Group)");
        t45 = new MenuItem("Standard Deviation of Attention");

        l0 = new MenuItem("Standard Layout");
        l1 = new MenuItem("Timeline Layout");

        h0 = new MenuItem("Online Help");

        t1.getItems().addAll(t11, t12, new SeparatorMenuItem(), t13);
        t4.getItems().addAll(t41, t42, new SeparatorMenuItem(), t43, t44, t45);

        fileMenu.getItems().addAll(f1, f2, f3, new SeparatorMenuItem(), f4, f5, new SeparatorMenuItem(), f6, f7);
        controlMenu.getItems().addAll(c1, c2, c3);
        analyzeMenu.getItems().addAll(t0, t1, t2, t3, t4, t5); //, a1, a2, new SeparatorMenuItem(), a3, a4);
        layoutMenu.getItems().addAll(l0, l1);
        helpMenu.getItems().add(h0);

        mb.getMenus().addAll(fileMenu, controlMenu, analyzeMenu, layoutMenu, helpMenu);
        vb = new VBox(mb);

        // Disable unsupported menu items and timeline items

        //f2.setDisable(true);
        //f3.setDisable(true);
        //f6.setDisable(true);
        t0.setDisable(true);
        t1.setDisable(true);
        t2.setDisable(true);
        t3.setDisable(true);
        t4.setDisable(true);
        t5.setDisable(true);
        //h0.setDisable(true);

        // Define the actions of the menu items
        c1.setOnAction(e -> {
            GUI.startPlayback();
        });                // Control – Play
        c2.setOnAction(e -> {
            GUI.pausePlayback();
        });                // Control – Pause
        c3.setOnAction(e -> {
            GUI.stopPlayback();
        });                // Control – Stop
        t0.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "BOOKMARKS");
        });
        t11.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "BOOKMARKSCARTESIAN");
        });
        t12.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "1DCARTESIAN");
        });
        t13.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "1DCARTESIAN-AVERAGE");
        });
        t41.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "SUBJECTACTIVITY");
        });
        t42.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "SUBJECTATTENTION");
        });
        t43.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "AFA");
        });                    // Visualize
        t44.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "ACTIVITY");
        });
        t45.setOnAction(e -> {
            GUI.visTemp.makeTimelineElement(GUI.s, "STANDARDDEVIATION");
        });
        h0.setOnAction(e -> {                                    // Help Menu
            WebView webView = new WebView();
            webView.getEngine().load("http://www.andreaspirchner.com");
            webView.setMinSize(840, 720);
            webView.setPrefSize(840, 720);
            Group g = new Group(webView);
            Scene helpScene = new Scene(g, 840, 720);
            Stage helpStage = new Stage();
            helpStage.setResizable(false);
            helpStage.setScene(helpScene);
            helpStage.show();
        });
        f1.setOnAction(new EventHandler<ActionEvent>() {                            // New Project Function
            public void handle(ActionEvent event) {
                GUI.s.clearSample();
                if (GUI.visTemp != null) {
                    GUI.visTemp.clearTimelines();
                }
                if (GUI.visSpat.thisSpatializer != null) {
                    GUI.visSpat.clearSpatial();
                    GUI.visSpat.removeSpatializer();
                    GUI.visSpat.spatializerLoaded = false;
                    GUI.visSpat.deactivateButtons();
                }
                GUI.mainNavipanel.clearProjectInfos();
                GUI.mainNavipanel.clearVideoDataOffsets();
                GUI.mainNavipanel.deactivateButtons();
                GUI.clearVideo();
                GUI.updateSampleTable();

                t1.setDisable(true);
                t2.setDisable(true);
                t3.setDisable(true);
                t4.setDisable(true);
                t5.setDisable(true);
            }
        });
        f2.setOnAction(new EventHandler<ActionEvent>() {                            // Load Project Function
            public void handle(ActionEvent event) {
                try (
                        FileInputStream fileIn = new FileInputStream("employee.ser");
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                ) {
                    GUI.mainNavipanel = (naviPanel)in.readObject();
                    //System.out.println(e.name);
                    //System.out.println(e.age);
                } catch (IOException i) {
                    System.out.println(i.getMessage());
                } catch (ClassNotFoundException e1) {
                    System.out.println(e1.getMessage());
                }
	        	 /*
	        	 GUI.s.clearSample();
	        	 GUI.visTemp.clearTimelines();
	        	 GUI.visSpat.clearSpatial();
	        	 GUI.updateSampleTable();
	        	 
	        	 FileChooser fileChooser = new FileChooser();							// Open File Chooser
		           fileChooser.setTitle("Load Subject's Data");
		           File file = fileChooser.showOpenDialog(stage);
		           JSONParser parser = new JSONParser();
		           JSONObject jsonData = new JSONObject();
		           
		           if (file != null) {
		        	   try (Reader reader = new FileReader(file)) {	        		   // Read selected file
		        		   try {
		        			   jsonData = (JSONObject) parser.parse(reader);				// Parse JSON
		        			   JSONArray subjects = (JSONArray) jsonData.get("subjects");
		        			   for (int i = 0; i < subjects.size(); i++) {					// 1. load and make subjects
									 JSONArray sub = (JSONArray) subjects.get(i); 
									 Subject subject = new Subject(sub);					// Make new Subject	
									 GUI.s.addSubject(subject);								// add Subject to Sample							 
									 GUI.updateSampleTable();								// update the Sample Table in GUI
		        			   }
		        			   GUI.visSpat.makeFilterListSubjects();
		        			   JSONArray timelines = (JSONArray) jsonData.get("timelines");  // 2. load and make timelines
		        			   for (int i = 0; i < timelines.size(); i++) {
		        				   JSONObject tl = (JSONObject) timelines.get(i); 
		        				   String tlType = (String) tl.get("type");
		        				   GUI.visTemp.makeTimelineElement(GUI.s, tlType);
		        				   
		        				   JSONArray markers = (JSONArray) tl.get("markers");  		// 2.1 make Markers for timeline
		        				   if (markers.size()< 0) {
		        					   for (int k = 0; k < markers.size(); k++) {
			        					   JSONObject ma = (JSONObject) markers.get(k); 
			        					   double timecode = (double) ma.get("timecode");
			        					   timeline t = GUI.visTemp.timelines.get(i);
			        					   //t.makeTimelineMarker(timecode, k, t.stepSize);
			        					   Boolean section = (Boolean) ma.get("section");		// 2.2 make Sections	  
			        					   if (section == true) {
			        						   double sectionEnd = (double) ma.get("section-end");
			        						   t.makeSection(t.markerList.get(k), sectionEnd);
			        					   }
			        				   }
		        				   }
		        				  
		        				   
		        			   }

		        		   } catch (ParseException e) {
								e.printStackTrace();
		        		   }
		        		   
		        	   }catch (IOException e) {
		        	   }		        	   
		           }
		           */

            }
        });
        f3.setOnAction(new EventHandler<ActionEvent>() {                            // Save Project Function
            public void handle(ActionEvent event) {
                try {
                    FileOutputStream fileOut = new FileOutputStream("employee.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(GUI.mainNavipanel);
                    out.close();
                    fileOut.close();
                    System.out.println("Serialized data is saved in employee.ser");
                } catch (IOException i) {
                    i.printStackTrace();
                }
	        	 /*
	        	 FileChooser fileChooser = new FileChooser();
	             fileChooser.setTitle("Save");
	             fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
	             File file = fileChooser.showSaveDialog(stage);
	             JSONObject projecttData = new JSONObject();
	             final JSONArray subjectArray = new JSONArray();
	             final JSONArray timelineArray = new JSONArray();
	             
	             GUI.s.SubjectsList.forEach((s) ->{ subjectArray.add(s.JSONData); });
	             
	             GUI.visTemp.timelines.forEach((tl) -> {
	            	 JSONObject timelineData = tl.getTimeLineDataJSON();
	            	 timelineArray.add(timelineData);
	             });

	             projecttData.put("timelines", timelineArray);
	             projecttData.put("subjects", subjectArray);
	             
	             String subjectDataString = projecttData.toString();
	             if (file != null) {
	                 saveTextToFile(subjectDataString, file);
	             }
	             */
            }
        });
        f4.setOnAction(new EventHandler<ActionEvent>() {                            // Load Sample Data Menu Function
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();                            // Open File Chooser
                fileChooser.setTitle("Load Subject's Data");
                List<File> fileList = fileChooser.showOpenMultipleDialog(stage);
                JSONParser parser = new JSONParser();
                JSONArray jsonData = new JSONArray();
                boolean firstFile = true;
                String firstDataModel = "";
                long firstDataDimensions = 0;

                if (fileList != null) {
                    for (File fs : fileList) {                                            // loop through all selected files
                        try (Reader reader = new FileReader(fs)) {                        // Read selected file
                            try {
                                jsonData = (JSONArray) parser.parse(reader);                // Parse JSON
                                Subject s = new Subject(jsonData);                        // Make new Subject
                                GUI.s.addSubject(s);                                        // add Subject to Sample
                                GUI.updateSampleTable();                                    // update the Sample Table in GUI
                                if (firstFile) {
                                    firstDataModel = s.dataModel;
                                    firstDataDimensions = s.dataDimensions;
                                    firstFile = false;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                        }
                    }
                    System.out.println("Data Model: "+firstDataModel);
                    if (firstDataModel.equals("Bookmarks") && firstDataDimensions == 1) {
                        GUI.visSpat.makeFilterListSubjects();
                        if (!GUI.visSpat.spatializerLoaded) {
                            //GUI.visSpat.makeSpatializerElement(GUI.s, "BOOKMARKS");
                        }
                        //GUI.visSpat.activateButtons("BOOKMARKS");
                        GUI.mainNavipanel.activateButtons(firstDataModel, firstDataDimensions);
                        t0.setDisable(false);
                    }
                    if (firstDataModel.equals("Cartesian") && firstDataDimensions == 1) {
                        GUI.visSpat.makeFilterListSubjects();
                        if (!GUI.visSpat.spatializerLoaded) {
                            GUI.visSpat.makeSpatializerElement(GUI.s, "1DCARTESIAN");
                        }
                        GUI.visSpat.activateButtons("1DCARTESIAN");
                        GUI.mainNavipanel.activateButtons(firstDataModel, firstDataDimensions);
                        t0.setDisable(false);
                        t1.setDisable(false);
                    }
                    if (firstDataModel.equals("Ternary") && firstDataDimensions == 2) {

                        GUI.visSpat.makeFilterListSubjects();
                        if (!GUI.visSpat.spatializerLoaded) {
                            GUI.visSpat.makeSpatializerElement(GUI.s, "2DTRIANGULAR");
                        }
                        GUI.visSpat.activateButtons("2DTRIANGULAR");
                        GUI.mainNavipanel.activateButtons(firstDataModel, firstDataDimensions);
                        t4.setDisable(false);
                   }
                }
            }
        });
        f5.setOnAction(new EventHandler<ActionEvent>() {                                // Load Video Function
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();                            // Open File Chooser
                fileChooser.setTitle("Load Video");
                File f = fileChooser.showOpenDialog(stage);
                if (f != null) {
                    URI uri;
                    uri = f.toURI();
                    GUI.videoURL = uri;
                    GUI.mainNavipanel.openVideoWindowButton.setDisable(false);
                    GUI.mainNavipanel.videoOffsetNumber.setDisable(false);
                    GUI.makeVideoWindow(uri);
                }
            }
        });
        // About Function
        f6.setOnAction(e -> {
            Image image = new Image("about-img.png");
            ImageView imageView = new ImageView(image);

            Group g = new Group(imageView);
            Text t = new Text(10, 60, "© Andreas Pirchner 2020–2024");
            VBox v = new VBox(g, t);
            VBox.setMargin(g, new Insets(36, 0, 36, 36));
            VBox.setMargin(t, new Insets(36, 0, 0, 36));
            v.setMinSize(640, 640);
            v.setPrefSize(640, 640);
            Scene aboutScene = new Scene(v, 640, 640);
            Stage aboutStage = new Stage();
            aboutStage.initStyle(StageStyle.TRANSPARENT);
            aboutStage.setResizable(false);
            aboutStage.setScene(aboutScene);
            aboutStage.show();

            aboutScene.setOnMousePressed((m) -> {
                aboutStage.close();
            });

        });
        f7.setOnAction(e -> {
            System.exit(0);
        });                                // QUit Function
        l0.setOnAction(e -> {
            GUI.topContainer.setVisible(true);
            GUI.topContainer.setManaged(true);
            GUI.bottomContainer.setPrefHeight(800);

        });
        l1.setOnAction(e -> {
            GUI.topContainer.setVisible(false);
            GUI.topContainer.setManaged(false);
        });

        // Define keyboard shortcuts for menu items
        f1.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        f2.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        f3.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        f4.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        f5.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        f7.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        c1.setAccelerator(KeyCombination.keyCombination("Ctrl+Space"));
        c2.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+Space"));
        c3.setAccelerator(KeyCombination.keyCombination("Alt+Space"));
        l0.setAccelerator(KeyCombination.keyCombination("Ctrl+1"));
        l1.setAccelerator(KeyCombination.keyCombination("Ctrl+2"));
        h0.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+H"));

    }

    public VBox getMainMenu() {
        return vb;
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {

        }
    }
}
