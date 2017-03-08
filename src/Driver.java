import javafx.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Driver extends Application{

	public void start(Stage stage) throws Exception {
		stage.setTitle("Boggle");
		Button btn = new Button();
		
		btn.setText("Click bait");
		btn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Hello World");
				
			}
			
		});
		
		StackPane root = new StackPane();
	}
	
	public static void main(String[] args){
		launch(args);
	}


}
