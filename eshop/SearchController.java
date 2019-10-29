/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Categorie;
import classe.Produit;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nivekiba
 */
public class SearchController implements Initializable {
    
    @FXML
    private ComboBox<Categorie> catCombo;
    @FXML
    private ComboBox<String> sigCombo, sigCombo2;
    @FXML
    private TextField prixText, qteText;
    
    public static TableView<Produit> prods = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        catCombo.setItems(FXCollections.observableArrayList(Categorie.getAll()));
        ArrayList<String> signs = new ArrayList<>();
        signs.add("<");
        signs.add("=");
        signs.add(">");
        sigCombo.setItems(FXCollections.observableArrayList(signs));
        sigCombo2.setItems(FXCollections.observableArrayList(signs));
    }    
    
    @FXML
    private void handleSearch() throws Exception{
        ArrayList<Produit> ps = Produit.all();
        ArrayList<Produit> toRemove = new ArrayList<>();
        
        for(Produit p : ps){
            if(catCombo.getValue() != null){
                if(p.getCategorie().getIdCa() != catCombo.getValue().getIdCa())
                    toRemove.add(p);
            }
            if(!prixText.getText().toString().equals("")){
                if(sigCombo.getSelectionModel().getSelectedIndex() == 0){
                    if(p.getPrix() >= Integer.parseInt(prixText.getText()))
                        toRemove.add(p);
                }else if(sigCombo.getSelectionModel().getSelectedIndex() == 1){
                    if(p.getPrix() != Integer.parseInt(prixText.getText()))
                        toRemove.add(p);
                }else if(sigCombo.getSelectionModel().getSelectedIndex() == 2){
                    if(p.getPrix() <= Integer.parseInt(prixText.getText()))
                        toRemove.add(p);
                }
            }
            if(!qteText.getText().toString().equals("")){
                System.out.println("===");
                if(sigCombo2.getSelectionModel().getSelectedIndex() == 0){
                    if(p.getQte() >= Integer.parseInt(qteText.getText()))
                        toRemove.add(p);
                }else if(sigCombo2.getSelectionModel().getSelectedIndex() == 1){
                    if(p.getQte() != Integer.parseInt(qteText.getText()))
                        toRemove.add(p);
                }else if(sigCombo2.getSelectionModel().getSelectedIndex() == 2){
                    if(p.getQte() <= Integer.parseInt(qteText.getText()))
                        toRemove.add(p);
                }
            }
        }
        
        for(Produit y : toRemove)
            ps.remove(y);
        
        prods.setItems(FXCollections.observableArrayList(ps));
        ((Stage)qteText.getScene().getWindow()).close();
    }
    
}
