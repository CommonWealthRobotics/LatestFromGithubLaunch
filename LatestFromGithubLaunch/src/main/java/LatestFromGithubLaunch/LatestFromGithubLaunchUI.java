package LatestFromGithubLaunch;
/**
 * Sample Skeleton for 'ui.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class LatestFromGithubLaunchUI {
	
	public static String [] args;

	public static Stage stage;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="progress"
    private ProgressBar progress; // Value injected by FXMLLoader

    @FXML // fx:id="previousVersion"
    private Label previousVersion; // Value injected by FXMLLoader
    @FXML // fx:id="previousVersion"
    private Label binary; // Value injected by FXMLLoader
    @FXML // fx:id="currentVersion"
    private Label currentVersion; // Value injected by FXMLLoader

    @FXML // fx:id="yesButton"
    private Button yesButton; // Value injected by FXMLLoader

    @FXML // fx:id="noButton"
    private Button noButton; // Value injected by FXMLLoader
    
    @FXML
    void onNo(ActionEvent event) {
    	System.out.println("No path");
    	launchApplication();
    }

    @FXML
    void onYes(ActionEvent event) {
    	System.out.println("Yes path");
    	yesButton.setDisable(true);
    	noButton.setDisable(true);
    	new Thread(()->{
    		for (double i=0;i<1;i+=0.05) {
    			try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			double progressNow = i;
    			Platform.runLater(()->{
    				progress.setProgress(progressNow);
    			});
    		}
    		launchApplication();
    	}).start();
    }
    
    public void launchApplication(){
    	Platform.runLater(()->stage.close());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert progress != null : "fx:id=\"progress\" was not injected: check your FXML file 'ui.fxml'.";
        assert previousVersion != null : "fx:id=\"previousVersion\" was not injected: check your FXML file 'ui.fxml'.";
        assert currentVersion != null : "fx:id=\"currentVersion\" was not injected: check your FXML file 'ui.fxml'.";
        binary.setText(args[0]+"\n"+args[1]+"\n"+args[2]);
        
    }
}
