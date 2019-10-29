/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Facture;
import classe.Lignefacture;
import classe.Produit;
import classe.Utilities;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nivekiba
 */
public class CommandeController implements Initializable {

    @FXML
    private Spinner<Integer> qteSpin;
    @FXML
    private Spinner<Double> remiseSpin;
    @FXML
    private Label prixLabel, montantLabel, montantTotal, codeLabel, nomLabel;
    @FXML
    private Button addLigne, removeLigne, saveFac, prodSelect;
    @FXML
    private TextField telText;
    @FXML
    private TableView<Lignefacture> ligneList;
    @FXML
    private TableColumn<Lignefacture, String> prodCol, prixUnitCol, prixTotalCol, qteCol;
    @FXML
    private ImageView prodView;
    @FXML
    private JFXDatePicker dateE;
    
    double montant = 0.0;
    private int qteProd;
    public static Produit prod = null;
    public static TableView<Facture> factview = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        montantLabel.setText("0.0 FCFA");
        montantTotal.setText("0.0 FCFA");
        
        SpinnerValueFactory fr = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 150, 1);
        qteSpin.setValueFactory(fr);
        qteSpin.setEditable(true);
        
        SpinnerValueFactory fe = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 80);
        remiseSpin.setValueFactory(fe);
        remiseSpin.setEditable(true);
        
        qteSpin.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    qteProd = Integer.parseInt(newValue);
                }
                catch(Exception f){}
            }
        });
        
        remiseSpin.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    montantTotal.setText( montant*(1.0 - Double.parseDouble(newValue)/100) + " FCFA" );
                } catch(Exception e){}
            }
        });
        
        prodCol.setCellValueFactory((p) -> { return new SimpleStringProperty(p.getValue().getProd().toString()); });
        prixUnitCol.setCellValueFactory((p) -> { return new SimpleStringProperty( p.getValue().getProd().getPrix()+" FCFA" ); });
        prixTotalCol.setCellValueFactory((p) -> { 
            return new SimpleStringProperty( p.getValue().getPrix() +" FCFA"); 
        });
        qteCol.setCellValueFactory((p)->{
            return new SimpleStringProperty(p.getValue().getQte()+"");
        });
    }    
    
    
    @FXML
    private void handleLigneAdd(ActionEvent ae){
        System.out.println("qte =====> "+qteProd);
        for(Lignefacture f : ligneList.getItems()){
            if(f.getProd().getCodePro() == prod.getCodePro()){
                if(f.getQte()+qteProd <= f.getProd().getQte()){
                    f.setQte(f.getQte()+qteProd);
                    f.setPrix(f.getPrix() + f.getProd().getPrix()*qteProd);
                    montant += f.getProd().getPrix()*qteProd;
                    montantLabel.setText(montant+" FCFA");
                    montantTotal.setText((montant*(1-remiseSpin.getValue()/100))+" FCFA");
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Plus assez de ce produit en stock");
                    a.showAndWait();
                }
                ligneList.refresh();
                return;
            }
        }
        
        Lignefacture l = new Lignefacture();
        
        if(prod!=null)
            l.setProduit(prod);
        else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Veuillez choisir un produit");
            a.showAndWait();
            return;
        }
        if(qteSpin.getValue()<=l.getProd().getQte()){
            l.setPrix(l.getProd().getPrix()*qteProd);
            System.out.println("===> "+qteProd);
            l.setQte(qteProd);
            ligneList.getItems().add(l);
            
            montant += l.getPrix();
            montantLabel.setText(montant+" FCFA");
            montantTotal.setText((montant*(1-remiseSpin.getValue()/100))+" FCFA");
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Plus assez de ce produit en stock");
            a.showAndWait();
        }
    }
    @FXML
    private void handleLigneRemove(){
        int ind = ligneList.getSelectionModel().getSelectedIndex();
        if(ind == -1) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Veuillez choisir un produit");
            a.showAndWait();
        }else{
            montant -= ligneList.getItems().get(ind).getPrix();
            montantLabel.setText(montant+" FCFA");
            ligneList.getItems().remove(ind);
        }
    }
    @FXML
    private void handleSaveFac(ActionEvent ae) throws Exception{
        
        Facture f = new Facture();
        for(Lignefacture lf : ligneList.getItems()){
            f.getLignes().add(lf);
        }
        f.setMontant(montant);
        f.setDateFac(new Timestamp(new Date().getTime()));
        if(dateE.getValue() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Veiullez renseigner une date pour la commande");
            a.showAndWait();
            return;
        }
        f.setDtEnreg( Date.from(dateE.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) );
        f.setRemise(remiseSpin.getValue());
        f.setGest(appController.user_connect);
        if(!telText.getText().equals(""))
            f.setTel(telText.getText());
        else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Vous n'avez pas entrer de numero du client");
            a.showAndWait();
            f.setTel(telText.getText());
        }
        if(appController.user_connect.getTypeGest() != 3)
            f.save();
        else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Vous etes un administrateur, vous ne pouvez donc pas facturer");
            a.showAndWait();
            return;
        }
        
        ((Stage)((Button)ae.getSource()).getScene().getWindow()).close();
        factview.refresh();
        factview.setItems(FXCollections.observableArrayList(Facture.all()));
    }
    @FXML
    private void handleChooseProd(ActionEvent ae) throws Exception{
        Stage st = new Stage();
        st.setTitle("choix du produit");
        VBox bn = new VBox();
        JFXTextField search = new JFXTextField();
        search.setPromptText("Commencer a taper le nom du produit");
        
        TableView<Produit> tv = new TableView<>(FXCollections.observableArrayList(Produit.all()));
        TableColumn<Produit, String> tc1 = new TableColumn<>("Code du produit");
        TableColumn<Produit, String> tc2 = new TableColumn<>("Nom du produit");
        TableColumn<Produit, String> tc3 = new TableColumn<>("Prix du produit");
        TableColumn<Produit, String> tc4 = new TableColumn<>("Categorie du produit");
        tc1.setCellValueFactory((p)->{
            return new SimpleStringProperty(Utilities.idToStr(p.getValue().getCodePro()));
        });
        tc2.setCellValueFactory((p)->{
            return new SimpleStringProperty(p.getValue().getNomPro());
        });
        tc3.setCellValueFactory((p)->{
            return new SimpleStringProperty(p.getValue().getPrix()+" FCFA");
        });
        tc4.setCellValueFactory((p)->{
            return new SimpleStringProperty(p.getValue().getCategorie().getNomCa());
        });
        tv.getColumns().addAll(tc1, tc2, tc3, tc4);
        
        bn.getChildren().add(search);
        bn.getChildren().add(new Separator());
        bn.getChildren().add(tv);
        search.setStyle("-fx-padding: 10px;");
        tv.setStyle("-fx-padding: 10px;");
        bn.setStyle("-fx-padding: 10px;");
        
        Scene sc = new Scene(bn, 600, 400);
        st.setScene(sc);
        
        st.show();
        tv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    prod = tv.getSelectionModel().getSelectedItem();
                    st.close();
                    codeLabel.setText(Utilities.idToStr(prod.getCodePro()));
                    nomLabel.setText(prod.getNomPro());
                    prixLabel.setText(prod.getPrix()+" FCFA");
                    
                    String photolink = prod.getPhotos().get(0).getLienPhoto();
                    prodView.setImage(new Image("http://"+Utilities.SERVER_URL+"/images/"+photolink, 105, 105, true, true, true));
                }
            }
        });
        search.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    tv.setItems(FXCollections.observableArrayList(Produit.find(search.getText(), 0)));
                } catch (Exception ex) {
                    Logger.getLogger(CommandeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
