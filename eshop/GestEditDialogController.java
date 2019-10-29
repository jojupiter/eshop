/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Gestionnaire;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rufus
 */
public class GestEditDialogController implements Initializable {

    @FXML
    private Label idGest;
    @FXML
    private TextField nomGest;
    @FXML
    private ComboBox<String> typeGest;
    @FXML
    private TextField login;
    @FXML
    private TextField pwd;
    @FXML
    private CheckBox actif;
    private Stage dialogStage;
    private boolean okClicked = false;
    private Gestionnaire gestionnaire;
    
    public static TableView<Gestionnaire> gestview = null;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setGestionnaire(Gestionnaire gestionnaire) {
        this.gestionnaire = gestionnaire;
        idGest.setText(Integer.toString(gestionnaire.getIdGest()));
        nomGest.setText(gestionnaire.getNomGest());
        typeGest.getItems().add("magasinier");
        typeGest.getItems().add("caissiere");
        typeGest.getItems().add("gestionnaire");
        typeGest.getSelectionModel().select(gestionnaire.getTypeGest());
        login.setText(gestionnaire.getLogin());
        pwd.setText(gestionnaire.getPwd());
        actif.setSelected(gestionnaire.getActif());
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (idGest.getText() == "" || nomGest.getText() == "" || nomGest.getText() == "" || login.getText() == ""
                || pwd.getText() == "") {
            errorMessage += "Un des champs est vide";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
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
        if (isInputValid()) {
            try {
                gestionnaire.setIdGest(Integer.valueOf(idGest.getText()));
                gestionnaire.setNomGest(nomGest.getText());
                gestionnaire.setTypeGest(typeGest.getSelectionModel().getSelectedIndex());
                gestionnaire.setLogin(login.getText());
                gestionnaire.setPwd(pwd.getText());
                gestionnaire.setActif(actif.isSelected());
                okClicked = true;
                gestionnaire.update();
                dialogStage.close();
                gestview.refresh();
            } catch (SQLException ex) {
                Logger.getLogger(GestEditDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    @FXML 
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
