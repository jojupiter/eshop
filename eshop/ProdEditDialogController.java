/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Categorie;
import classe.Produit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rufus
 */
public class ProdEditDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label codePro;
    @FXML
    private TextField nomPro;
    @FXML
    private TextField prix, prix_vente;
    @FXML
    private TextField qte;
    @FXML
    private TextArea description;
    @FXML
    private TextField idFour;
    @FXML
    private CheckBox actif;
    @FXML
    private ComboBox<Categorie> catCombo;
    
    private Stage dialogStage;
    private Produit produit;
    private boolean okClicked = false;
    
    public static TableView<Produit> prodview = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setDialogStage (Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    
    public void setProduit(Produit produit){
        this.produit = produit;
        codePro.setText(Integer.toString(produit.getCodePro()));
        nomPro.setText(produit.getNomPro());
        prix.setText(Float.toString(produit.getPrixAchat()));
        prix_vente.setText(Float.toString(produit.getPrix()));
        qte.setText(Integer.toString(produit.getQte()));
        qte.setEditable(false);
        qte.setDisable(true);
        description.setText(produit.getDescription());
        idFour.setText(Integer.toString(produit.getCodeFour()));
        actif.setText(Boolean.toString(produit.getActif()));
        if(produit.getActif())
            actif.setSelected(true);
        
        catCombo.setItems(FXCollections.observableArrayList(Categorie.getAll()));
        int i=0;
        for(Categorie f : catCombo.getItems()){
            if(f.getIdCa() == produit.getCategorie().getIdCa()){
                catCombo.getSelectionModel().select(i);
                break;
            }
            i++;
        }
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
         if(codePro.getText() == null || codePro.getText().length() == 0){
             errorMessage += "Ce n'est pas un produit valide";
         }
         if (errorMessage.length() == 0){
             return true;
         } else {
             //Afficher le message d'erreur
             Alert alert = new Alert(Alert.AlertType.ERROR);
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
            produit.setCodePro(Integer.parseInt(codePro.getText()));
            produit.setNomPro(nomPro.getText());
            produit.setPrixAchat(Float.parseFloat(prix.getText()));
            produit.setPrix(Float.parseFloat(prix_vente.getText()));
            produit.setQte(Integer.parseInt(qte.getText()));
            produit.setDescription(description.getText());
            produit.setCodeFour(Integer.parseInt(idFour.getText()));
            produit.setActif(actif.isSelected());
            produit.setCategorie(catCombo.getValue());
            
            produit.update();
            okClicked = true;
            dialogStage.close();
            prodview.refresh();
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}