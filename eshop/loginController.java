/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Gestionnaire;
import classe.Utilities;
import com.jfoenix.controls.JFXDecorator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Nivekiba
 */
public class loginController implements Initializable {
    
    public Stage ownStage;
    private static double xoff = 0, yoff =0;
    
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, URISyntaxException {
        boolean connected = false;
        Gestionnaire g = null;
        if(username.getText().equals(Utilities.ADMIN_USER) && password.getText().equals(Utilities.ADMIN_PASS)){
            g = new Gestionnaire();
            g.setNomGest("Administrator");
            g.setTypeGest(3);
            connected = true;
        } else {
            g = Gestionnaire.connect(username.getText(), password.getText());
            if(g == null) connected = false;
            else{
                connected = true;
            }
        }
        
        if(connected){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("app.fxml"));
            
            AnchorPane page = (AnchorPane) loader.load();
            page.setPrefWidth(page.getPrefWidth()*1.05);
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            JFXDecorator deco = new JFXDecorator(dialogStage, page);
            Scene scene = new Scene(deco);
            dialogStage.setScene(scene);
            dialogStage.show();
            
            // Set the person into the controller.
             appController app = loader.getController();
             app.ownStage = dialogStage;
             appController.user_connect = g;
             app.setName();
             scene.getStylesheets().add(getClass().getResource("css.css").toExternalForm());
             this.ownStage.close();
             
             scene.setOnMousePressed((MouseEvent event1) -> {
                 xoff = event1.getSceneX();
                 yoff = event1.getSceneY();
            });
            scene.setOnMouseDragged((MouseEvent event1) -> {
                dialogStage.setX(event1.getScreenX() - xoff);
                dialogStage.setY(event1.getScreenY() - yoff);
            });
             
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle(null);
            a.setContentText("Mauvais login");
            a.showAndWait();
        }
    }
    
    @FXML
    private void handleExit(){
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
