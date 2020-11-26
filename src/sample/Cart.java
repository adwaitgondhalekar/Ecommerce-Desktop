package sample;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class Cart
{
    public static void cart(Stage primaryStage) throws Exception
    {
        ListView listView = new ListView();

        listView.getItems().add("Item 1");
        listView.getItems().add("Item 2");
        listView.getItems().add("Item 3");

        HBox hbox = new HBox(listView);

      //  GridPane root_signup = new GridPane();
      //  root_signup.add(root_signup,0,0);

        Scene nscene =new Scene(hbox, 800, 600);
        primaryStage.setScene(nscene);
        primaryStage.setMaximized(true);
        nscene.setRoot(hbox);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}