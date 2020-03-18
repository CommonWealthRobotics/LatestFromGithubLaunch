package LatestFromGithubLaunch;
/**
 * Sample Skeleton for 'ui.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LatestFromGithubLaunchUI {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="progress"
    private ProgressBar progress; // Value injected by FXMLLoader

    @FXML // fx:id="previousVersion"
    private Label previousVersion; // Value injected by FXMLLoader

    @FXML // fx:id="currentVersion"
    private Label currentVersion; // Value injected by FXMLLoader

    @FXML
    void onNo(ActionEvent event) {
    	System.out.println("No path");
    }

    @FXML
    void onYes(ActionEvent event) {
    	System.out.println("Yes path");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert progress != null : "fx:id=\"progress\" was not injected: check your FXML file 'ui.fxml'.";
        assert previousVersion != null : "fx:id=\"previousVersion\" was not injected: check your FXML file 'ui.fxml'.";
        assert currentVersion != null : "fx:id=\"currentVersion\" was not injected: check your FXML file 'ui.fxml'.";

    }
}
