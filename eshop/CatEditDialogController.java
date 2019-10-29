/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Categorie;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rufus
 */
public class CatEditDialogController implements Initializable {

    public static TableView<Categorie> catview;

    /**
     * Initializes the controller class.
     */
    @FXML 
    private Label idCat;
    @FXML 
    private TextField nameField; 
    private Stage dialogStage;
    private Categorie categorie;
    private boolean okClicked = false;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    //Mise en place du stage 
    public void setDialogStage (Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
    * Définir la personne comme éditable dans la boîte de dialogue
    * @param categorie
    */
    public void setCategorie (Categorie categorie) {
        this.categorie = categorie;

        idCat.setText(Integer.toString(categorie.getIdCa()));
        nameField.setText(categorie.getNomCa());
    } 

    public boolean isOkClicked() {
        return okClicked;
    }
        /**
    * Vérification des Input
    * @return true les input sont valides
     */
     private boolean isInputValid() {
         String errorMessage="";
         if(nameField.getText() == null || nameField.getText().length() == 0){
             errorMessage += "Ce n'est pas une catégorie valide";
         }
         if (errorMessage.length() == 0){
             return true;
         } else {
             //Afficher le message d'erreur
             Alert alert = new Alert(AlertType.ERROR);
             alert.initOwner(dialogStage);
             alert.setTitle("Champs invalides");
             alert.setHeaderText("Corriger les champs invalides");
             alert.setContentText(errorMessage);

             alert.showAndWait();

             return false;
         }
     }
     
    @FXML
    private void handleOk() {
        if(isInputValid()) {
            categorie.setNomCa(nameField.getText());
            categorie.update();
            okClicked = true;
            dialogStage.close();
            catview.refresh();
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

}
