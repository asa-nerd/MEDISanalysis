//  ------------------------------------------------------------
//
//  MEDISanalysis V 0.95
//  Andreas Pirchner, 2020-2024
//
//  ------------------------------------------------------------

package medis.MEDISanalysis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application{

    private static Stage pStage;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("MEDISanalysis 0.95");
        primaryStage.initStyle(StageStyle.DECORATED);
        Sample sample = new Sample();
        GUI gui = new GUI(primaryStage, sample);
        VBox root = gui.getLayout();
        Scene scene = new Scene(root, 1400, 900);
        //scene.getStylesheets().add("/Ressources/stylesheet.css");
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        pStage = primaryStage;
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }
}