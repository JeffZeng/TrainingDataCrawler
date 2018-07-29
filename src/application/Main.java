package application;
	
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	private Stage primaryStage = null;
	BorderPane rootLayout = null;
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Training data crawler");
			initRootLayout();
			showViewController("/view/ViewController.fxml", primaryStage);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void initRootLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		//loader.setResources(ResourceBundle.getBundle("/bundles"));
		loader.setLocation(Main.class.getResource("/view/RootLayout.fxml"));
		rootLayout =(BorderPane) loader.load();
		Scene scene = new Scene(rootLayout,400,400);
		scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	void showViewController(String path, Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(path));
		
		rootLayout.setCenter(loader.load());
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
