/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import classe.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Nivekiba
 */
public class appController implements Initializable {
    
    public Stage ownStage;
    public static Gestionnaire user_connect;
    private Photo ph;
    private Eshop eshop;
    private Produit prodToStock;
    
    private ObservableList<Produit> prodData;
    private ObservableList<Categorie> catData;
    private ObservableList<ArrayList<String>> benefices;
    private ObservableList<GestionStock> stockData;
    private ObservableList<Gestionnaire> gestData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ProdEditDialogController.prodview = prodList;
        CatEditDialogController.catview = categorieList;
        CommandeController.factview = factureList;
        GestEditDialogController.gestview = usersList;
        try{
            prodData = FXCollections.observableArrayList(Produit.all());
            catData = FXCollections.observableArrayList(Categorie.getAll());
            stockData = FXCollections.observableArrayList(GestionStock.all());
            gestData = FXCollections.observableArrayList(Gestionnaire.getAll());;
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    @FXML
    private Button catBtn;
    @FXML
    private Button prodBtn;
    @FXML
    private Button stockBtn;
    @FXML
    private Button usersBtn, btnUser, btnProd, btnStock, configBtn, btnRecherAvanc, compBtn;
    @FXML
    private ImageView passCmd, viewFactureBtn;
    @FXML
    private AnchorPane categoriePane;
    @FXML
    private AnchorPane stockPane;
    @FXML
    private AnchorPane produitPane, usersPane, menuPane, cmdPane, mainPane, compPane;
    @FXML
    private TableView<Categorie> categorieList;
    @FXML
    private TableColumn<Categorie, Integer> idCatColumn;
    @FXML
    private TableColumn<Categorie, String> nomCatColumn;
    @FXML
    private TableView<Gestionnaire> usersList;
    @FXML
    private TableView<Produit> prodList;
    @FXML
    private TableView<Facture> factureList;
    @FXML
    private TableView<GestionStock> stockList;
    @FXML
    private TableColumn<Gestionnaire, String> nomGestColumn, typeGestColumn, userGestColumn, passGestColumn;
    @FXML
    private TableColumn<Produit, String> nomProdColumn, codeProdColumn, fourProdColumn, prixProdColumn, prixAchatProdColumn, qteProdColumn;
    @FXML
    private TableColumn<Produit, String> catProdColumn;
    @FXML
    private TableColumn<Produit, Image> photoProd;
    @FXML
    private TableColumn<Gestionnaire, Boolean> stateUserColumn;
    @FXML
    private TableColumn<GestionStock, String> typeStockCol, prodStockCol, qteStockCol, gestStockCol;
    @FXML
    private TableColumn<GestionStock, Date> dateStockCol;
    @FXML
    private TableColumn<Facture, Date> dateFacCol, dtEnregCol;
    @FXML
    private TableColumn<Facture, String> montantCol, montantFinalCol, typeFacCol, gestFacCol;
    @FXML
    private TableColumn<Facture, Double> remiseCol;
    @FXML
    private ComboBox<String> typeUserCombo, typeStockCombo;
    @FXML
    private ComboBox<Categorie> catCombo;
    @FXML
    private CheckBox actifCheck;
    @FXML
    private TextField nomUserText, userText, passUserText, nomProdText, prixProdText, prixAchatProdText, codeFourProdText, qteStockText, searchText, searchText1;
    @FXML
    private TextArea descProdText;
    @FXML
    private Label connected, codeLabel, nomLabel, codeLabelStock;
    @FXML
    private Button cmdBtn, benBtn, recapBtn, compteGestBtn, mouvBtn;
    @FXML
    private ImageView prodPhoto;
    @FXML
    private DatePicker dateStock, dateMin, dateMax;
    @FXML   
    private JFXButton catModify;
    @FXML
    private Pagination pageProd, pageStock, pageCat, pageGest, pageFact;
    @FXML
    private RadioButton jourRadio, moisRadio, anRadio;
    @FXML
    private TableView<ArrayList<String>> comptaList;
    @FXML
    private TableColumn<ArrayList<String>, String> categCompCol, articleCol, prixUCol, benCol, qteCol, benTotalCol;
    
    ArrayList<Facture> factures = new ArrayList<>();
    
    @FXML
    private void handleExit(){
        ownStage.close();
        Stage newStage = new Stage();
        Eshop e = new Eshop();
        try {
            e.start(newStage);
        } catch (Exception ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        @FXML
    private void handleEditGest() {
        Eshop eshop = new Eshop();
        Gestionnaire gestSelect = usersList.getSelectionModel().getSelectedItem();
        if (gestSelect != null) {
            boolean okClicked;
            okClicked = eshop.showGestEditDialog(gestSelect);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(ownStage);
            alert.setTitle("Aucune Sélection");
            alert.setHeaderText("Aucun gestionnaire sélectionné");
            alert.setContentText("Veuillez sélectionner un gestionnaire dans la table");

            alert.showAndWait();
        }
    }
    
    @FXML
    /* Méthode de modification de la catégorie */
    private void handleEditCat() {
        Eshop eshop = new Eshop();
        Categorie categorieSelect = categorieList.getSelectionModel().getSelectedItem();
        if (categorieSelect != null) {
            boolean okClicked;
            okClicked = eshop.showCatEditDialog(categorieSelect);
            
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(ownStage);
            alert.setTitle("Aucune Sélection");
            alert.setHeaderText("Aucune catégorie sélectionnée");
            alert.setContentText("Veuillez sélectionner une catégorie dans la table");

            alert.showAndWait();
        }
    } 
        @FXML
    private void handleEditProd() {
        Eshop eshop = new Eshop();
        Produit produitSelect = prodList.getSelectionModel().getSelectedItem();
        if (produitSelect != null) {
            boolean okClicked;
            okClicked = eshop.showProdEditDialog(produitSelect);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(ownStage);
            alert.setTitle("Aucune Sélection");
            alert.setHeaderText("Aucun Produit sélectionné");
            alert.setContentText("Veuillez sélectionner un produit dans la table");

            alert.showAndWait();
        }
    }
    @FXML
    private void handleMenuClick(ActionEvent ae) throws Exception {
        
        catCombo.setItems(FXCollections.observableArrayList());
        
        categoriePane.setVisible(false);
        stockPane.setVisible(false);
        produitPane.setVisible(false);
        usersPane.setVisible(false);
        cmdPane.setVisible(false);
        compPane.setVisible(false);
        mainPane.setVisible(false);
        
        catBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        prodBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        usersBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        stockBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        cmdBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        compBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        
        compBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        compteGestBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        recapBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        mouvBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        
        codeLabel.setText("");
        codeLabelStock.setText("choisir le produit");
        nomLabel.setText("");
        
        if(ae.getSource()==catBtn) {
            
            catBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            categoriePane.setVisible(true);
            AnchorPane form = (AnchorPane)categoriePane.getChildren().get(0);
            AnchorPane view = (AnchorPane)categoriePane.getChildren().get(1);
            
            TextField bn = (TextField)form.getChildren().get(1);
            Button b = (Button)form.getChildren().get(2);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Categorie c = new Categorie();
                    c.setNomCa(bn.getText());
                    bn.setText("");
                    c.save();
                    ObservableList<Categorie> cs = FXCollections.observableArrayList();
                    catData = FXCollections.observableArrayList(Categorie.getAll());
                    for(Categorie f : catData)
                        cs.add(f);
                    categorieList.setItems(cs);
                }
            });
            idCatColumn.setCellValueFactory( new PropertyValueFactory<Categorie,Integer>("idCa") );
            nomCatColumn.setCellValueFactory( new PropertyValueFactory<Categorie,String>("nomCa") );
            ObservableList<Categorie> cs = FXCollections.observableArrayList();
            for(Categorie f : catData)
                cs.add(f);
            categorieList.setItems( FXCollections.observableArrayList(cs.subList(0, Math.min(18, cs.size()) )) );
            
            pageCat.setPageCount((int) Math.ceil(((double)catData.size()/18)));
            pageCat.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageCat.getCurrentPageIndex();
                    categorieList.setItems( FXCollections.observableArrayList(cs.subList(18*(p-1), Math.min(18*p, cs.size()) )) );
                }
            });
            
        }else if(ae.getSource()==prodBtn) {
            prodData = FXCollections.observableArrayList(Produit.all());
            
            prodBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            produitPane.setVisible(true);
            for(Categorie c : Categorie.getAll())
                catCombo.getItems().add(c);
            
            nomProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            codeProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            prixProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            prixAchatProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            qteProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            fourProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            catProdColumn.prefWidthProperty().bind(prodList.widthProperty().add(-150).divide(7));
            
            nomProdColumn.setCellValueFactory( new PropertyValueFactory<>("nomPro"));
            codeProdColumn.setCellValueFactory(p -> {
                return new SimpleStringProperty(Utilities.idToStr(p.getValue().getCodePro()));
            });
            prixProdColumn.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().getPrix()+" FCFA");
            });
            prixAchatProdColumn.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().getPrixAchat()+" FCFA");
            });
            qteProdColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
            fourProdColumn.setCellValueFactory(new PropertyValueFactory<>("codeFour"));
            catProdColumn.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().getCategorie().getNomCa());
            });
            
            photoProd.setCellValueFactory((p)->{
                String photolink = p.getValue().getPhotos().get(0).getLienPhoto();
                return new SimpleObjectProperty<>(new Image("http://"+Utilities.SERVER_URL+"/images/"+photolink, 150, 150, true, true, true));
            });
            photoProd.setPrefWidth(150);
            
            photoProd.setCellFactory(new Callback<TableColumn<Produit, Image>, TableCell<Produit, Image>>() {
                @Override
                public TableCell<Produit, Image> call(TableColumn<Produit, Image> param) {
                    //Set up the ImageView
                    return new TableCell<Produit, Image>() {
                        public void updateItem(Image i, boolean empty) {
                            super.updateItem(i, empty);
                            setText(null);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            ImageView imageView = (i == null || empty) ? null : ImageViewBuilder.create().image(i).build();
                            setGraphic(imageView);
                        }
                    };
                }

            });
            
            ObservableList<Produit> prods = FXCollections.observableArrayList(prodData);
            
            prodList.setItems( FXCollections.observableArrayList(prods.subList(0, Math.min(10, prods.size()) )) );
            
            pageProd.setPageCount((int) Math.ceil(((double)Produit.all().size()/10)));
            pageProd.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageProd.getCurrentPageIndex();
                    prodList.setItems( FXCollections.observableArrayList( prods.subList(18*(p-1), Math.min(10*p, prods.size())) ) );
                }
            });
            
            btnProd.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Produit p = new Produit(0, nomProdText.getText(), Float.parseFloat(prixProdText.getText()), Float.parseFloat(prixAchatProdText.getText()), 0, descProdText.getText(), Integer.parseInt(codeFourProdText.getText()), actifCheck.isSelected(), catCombo.getValue());
                    nomProdText.setText("");
                    prixProdText.setText("");
                    descProdText.setText("");
                    codeFourProdText.setText("");
                    prodPhoto.setImage(new Image("images/icons8_Google_Images_100px.png"));
                    try {
                        p.save();
                        System.out.println("===> "+ph);
                        p.addPhoto(ph);
                        prodList.setItems(FXCollections.observableArrayList(Produit.all()));
                    } catch (Exception ex) {
                        Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            prodList.widthProperty().addListener((p)->{
               for(TableColumn tc : prodList.getColumns()){
                    tc.setMinWidth(prodList.getWidth()/prodList.getColumns().size());
                    tc.setMaxWidth(prodList.getWidth()/prodList.getColumns().size());
                } 
            });
            
            prodList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getClickCount()==2){
                        try {
                            ProdviewController.pro = prodList.getSelectionModel().getSelectedItem();
                            
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("prodview.fxml"));
                            AnchorPane page = (AnchorPane) loader.load();
                            
                            // Create the dialog Stage.
                            Stage dialogStage = new Stage();
                            dialogStage.setTitle("Infos produit");
                            JFXDecorator deco = new JFXDecorator(dialogStage, page);
                            Scene scene = new Scene(deco);
                            dialogStage.setScene(scene);
                            dialogStage.show();
                            
                            // Set the person into the controller.
                        } catch (IOException ex) {
                            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            
        }else if(ae.getSource()==stockBtn) {
            
            stockBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            stockPane.setVisible(true);
            
            qteStockText.lengthProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.intValue() > oldValue.intValue()) {
                        char ch = qteStockText.getText().charAt(oldValue.intValue());
                        // Check if the new character is the number or other's
                        if (!(ch >= '0' && ch <= '9' )) {
                            // if it's not number then just setText to previous one
                            qteStockText.setText(qteStockText.getText().substring(0,qteStockText.getText().length()-1)); 
                        }
                   }
                }
            });
            typeStockCombo.setItems(FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList("sortie", "entrée"))));
            
            typeStockCol.setCellValueFactory( (p)-> {
                return p.getValue().getOperation()==0?new SimpleStringProperty("sortie"):new SimpleStringProperty("entree");
            });
            qteStockCol.setCellValueFactory((p) -> { return new SimpleStringProperty(""+p.getValue().getQte()); });
            dateStockCol.setCellValueFactory( (p) -> { return new SimpleObjectProperty<>(p.getValue().getDateStock()); } );
            dateStockCol.setCellFactory(new Callback<TableColumn<GestionStock, Date>, TableCell<GestionStock, Date>>() {
                @Override
                public TableCell<GestionStock, Date> call(TableColumn<GestionStock, Date> param) {
                    return new TableCell<GestionStock, Date>() {
                        public void updateItem(Date i, boolean empty) {
                            setText(null);
                            super.updateItem(i, empty);
                            if(!empty && i!= null){
                                setText(new SimpleDateFormat("EE dd MMM yyyy").format(i));
                            }
                        }
                    };
                }
            });
            prodStockCol.setCellValueFactory( (p) -> { return new SimpleStringProperty(p.getValue().getProd().toString()); } );
            gestStockCol.setCellValueFactory( (p) -> { return new SimpleStringProperty(p.getValue().getGest().getNomGest()); } );
            
            btnStock.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(user_connect.getTypeGest()==3){
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setContentText("Vous etes un administrateur, pas le droit de facturer ou de gerer les stocks");
                        a.showAndWait();
                        return;
                    }
                        
                    Produit tmp = prodToStock;
                    int qteS = 0;
                    try{
                        qteS = Integer.parseInt(qteStockText.getText());
                    }catch(Exception e){
                        Alert a = new Alert(AlertType.ERROR);
                        a.setContentText("Veuillez specifier une quantité de produit pour l'operation");
                        a.showAndWait();
                        return;
                    }
                    qteStockText.setText("0");
                    
                    if( (typeStockCombo.getSelectionModel().getSelectedIndex()==0 && qteS <= tmp.getQte()) 
                            || typeStockCombo.getSelectionModel().getSelectedIndex()==1){
                        GestionStock g = new GestionStock();
                        g.setCodePro(tmp.getCodePro());
                        g.setDateStock( Date.from(dateStock.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) );
                        g.setIdGest(user_connect.getIdGest());
                        g.setOperation(typeStockCombo.getSelectionModel().getSelectedIndex());
                        g.setQte(qteS);
                        try {
                            g.save();
                            if(typeStockCombo.getSelectionModel().getSelectedIndex()==0)
                                tmp.setQte(tmp.getQte() - qteS);
                            else
                                tmp.setQte(tmp.getQte() + qteS);
                            tmp.update();
                            stockList.setItems(FXCollections.observableArrayList(GestionStock.all()));
                        } catch (Exception ex) {
                            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else{
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle(null);
                        a.setContentText("Il n'y a plus assez de produits a sortir\nQuantite en stock = "+tmp.getQte());
                        a.showAndWait();
                    }
                }
            });
            
            ObservableList<GestionStock> stocks = FXCollections.observableArrayList(GestionStock.all());
            stockList.setItems(stocks);
            stockList.setItems( FXCollections.observableArrayList(stocks.subList(0, Math.min(18, stocks.size()) )) );
            
            pageStock.setPageCount((int) Math.ceil(((double)GestionStock.all().size()/18)));
            pageStock.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageStock.getCurrentPageIndex();
                    stockList.setItems( FXCollections.observableArrayList( stocks.subList(18*(p-1), Math.min(18*p, stocks.size())) ) );
                }
            });
            
            stockList.widthProperty().addListener((p)->{
               for(TableColumn tc : stockList.getColumns()){
                    tc.setMinWidth(stockList.getWidth()/stockList.getColumns().size());
                    tc.setMaxWidth(stockList.getWidth()/stockList.getColumns().size());
                } 
            });
            
            for(TableColumn tc : stockList.getColumns()){
                 tc.setMinWidth(stockList.getWidth()/stockList.getColumns().size());
                 tc.setMaxWidth(stockList.getWidth()/stockList.getColumns().size());
             } 
            
        }else if(ae.getSource()==usersBtn) {
            usersBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            usersPane.setVisible(true);
            ObservableList<String> types = FXCollections.observableArrayList();
            types.add("magasinier");
            types.add("caissiere");
            types.add("gestionnaire");
            typeUserCombo.setItems(types);
            nomGestColumn.setCellValueFactory(new PropertyValueFactory<Gestionnaire, String>("nomGest"));
            userGestColumn.setCellValueFactory(new PropertyValueFactory<Gestionnaire, String>("login"));
            passGestColumn.setCellValueFactory(new PropertyValueFactory<Gestionnaire, String>("pwd"));
            
            typeGestColumn.setCellValueFactory(p -> {
                switch (((Gestionnaire)p.getValue()).getTypeGest()) {
                    case 0:
                        return new SimpleStringProperty("magasinier");
                    case 1:
                        return new SimpleStringProperty("caissiere");
                    case 2:
                        return new SimpleStringProperty("gestionnaire");
                    default:
                        break;
                }
                return new SimpleStringProperty("inconnu");
            });
            
            stateUserColumn.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getActif()));
            stateUserColumn.setCellFactory(CheckBoxTableCell.forTableColumn(stateUserColumn));
            stateUserColumn.setOnEditCommit(event -> System.out.println("uuuuuuuuuuuuuuuuuuuu"));
            
            stateUserColumn.setEditable(true);
            usersList.setEditable(true);
            
            btnUser.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Gestionnaire g = new Gestionnaire();
                        g.setNomGest(nomUserText.getText()); nomUserText.setText("");
                        g.setTypeGest(typeUserCombo.getSelectionModel().getSelectedIndex());
                        g.setLogin(userText.getText()); userText.setText("");
                        g.setPwd(passUserText.getText()); passUserText.setText("");
                        g.setActif(false);
                        g.save();
                        Bounds rootBounds = ownStage.getScene().getRoot().getLayoutBounds();
                        JFXPopup pop = new JFXPopup();
                        Label msg = new Label("Gestionnaire enregistrer");
                        msg.setStyle("-fx-background-color: green; -fx-text-fill:white; -fx-font-size: 20px");
                        JFXRippler r = new JFXRippler(msg);r.setRipplerFill(Color.WHITE);
                        pop.setPopupContent(r);
                        pop.show(usersPane, PopupVPosition.TOP, PopupHPosition.LEFT, rootBounds.getWidth() - 150, rootBounds.getHeight() - 100);
                        usersList.setItems(FXCollections.observableArrayList(Gestionnaire.getAll()));
                    } catch (SQLException ex) {
                        Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            ObservableList<Gestionnaire> gests = FXCollections.observableArrayList(Gestionnaire.getAll());
            usersList.setItems(gests);
            usersList.setItems( FXCollections.observableArrayList(gests.subList(0, Math.min(18, gests.size()) )) );
            
            pageGest.setPageCount((int) Math.ceil(((double)Gestionnaire.getAll().size()/18)));
            pageGest.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageGest.getCurrentPageIndex();
                    usersList.setItems( FXCollections.observableArrayList( gests.subList(18*(p-1), Math.min(18*p, gests.size())) ) );
                }
            });
        }else if(ae.getSource()==cmdBtn) {
            cmdBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            cmdPane.setVisible(true);
            
            factures = Facture.all();
            
            factureList.setItems(FXCollections.observableArrayList(Facture.all()));
            
            dateFacCol.setCellValueFactory((p)-> { return new SimpleObjectProperty<>(p.getValue().getDateFac()); });
            dateFacCol.setCellFactory(new Callback<TableColumn<Facture, Date>, TableCell<Facture, Date>>() {
                @Override
                public TableCell<Facture, Date> call(TableColumn<Facture, Date> param) {
                    return new TableCell<Facture, Date>() {
                        public void updateItem(Date i, boolean empty) {
                            setText(null);
                            super.updateItem(i, empty);
                            if(!empty && i!= null){
                                setText(new SimpleDateFormat("EE dd MMM yyyy").format(i));
                            }
                        }
                    };
                }
            });
            
            dtEnregCol.setCellValueFactory((p)-> { return new SimpleObjectProperty<>(p.getValue().getDtEnreg()); });
            dtEnregCol.setCellFactory(new Callback<TableColumn<Facture, Date>, TableCell<Facture, Date>>() {
                @Override
                public TableCell<Facture, Date> call(TableColumn<Facture, Date> param) {
                    return new TableCell<Facture, Date>() {
                        public void updateItem(Date i, boolean empty) {
                            setText(null);
                            super.updateItem(i, empty);
                            if(!empty && i!= null){
                                setText(new SimpleDateFormat("EE dd MMM yyyy").format(i));
                            }
                        }
                    };
                }
            });
            
            pageFact.setPageCount((int) Math.ceil(((double)Gestionnaire.getAll().size()/18)));
            pageFact.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageFact.getCurrentPageIndex();
                    factureList.setItems( FXCollections.observableArrayList( factures.subList(18*(p-1), Math.min(18*p, factures.size())) ) );
                }
            });
            
            factureList.itemsProperty().addListener(new ChangeListener<ObservableList<Facture>>() {
                @Override
                public void changed(ObservableValue<? extends ObservableList<Facture>> observable, ObservableList<Facture> oldValue, ObservableList<Facture> newValue) {
                    pageFact.setPageCount((int) Math.ceil(((double)Gestionnaire.getAll().size()/18)));
                    pageFact.setCurrentPageIndex(0);
                    pageFact.requestFocus();
                }
            });
            
            remiseCol.setCellValueFactory( (p) -> { return new SimpleObjectProperty<>(p.getValue().getRemise()); });
            montantCol.setCellValueFactory( (p) -> { 
                return new SimpleStringProperty(""+ p.getValue().getMontant() +" Fcfa"); 
            });
            montantFinalCol.setCellValueFactory( (p) -> { 
                return new SimpleStringProperty(""+( p.getValue().getMontant()*(1-p.getValue().getRemise()/100))+" Fcfa" ); 
            });
            typeFacCol.setCellValueFactory( (p) -> { return new SimpleStringProperty(!p.getValue().getTypeFac()?"Cash":"Electronique"); });
            gestFacCol.setCellValueFactory( (p) -> { return new SimpleStringProperty(p.getValue().getGest().toString()); });
            
            factureList.widthProperty().addListener((p)->{
               for(TableColumn tc : factureList.getColumns()){
                   if(tc != gestFacCol){
                        tc.setMinWidth(factureList.getWidth()/(factureList.getColumns().size()+1) );
                        tc.setMaxWidth(factureList.getWidth()/(factureList.getColumns().size()+1) );
                   }
                } 
            });
            
            for(TableColumn tc : factureList.getColumns()){
                if(tc != gestFacCol){
                     tc.setMinWidth(factureList.getWidth()/(factureList.getColumns().size()+1) );
                     tc.setMaxWidth(factureList.getWidth()/(factureList.getColumns().size()+1) );
                }
             } 
            
            gestFacCol.setPrefWidth(factureList.getWidth()/3);
            viewFactureBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        if(factureList.getSelectionModel().getSelectedItem() == null){
                            Alert t = new Alert(AlertType.INFORMATION);
                            t.setContentText("Veuillez selectionner une facture");
                            t.showAndWait();
                            return;
                        }
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("apercuFacture.fxml"));
                        AnchorPane page = (AnchorPane) loader.load();
                        
                        // Create the dialog Stage.
                        Stage dialogStage = new Stage();
                        
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.getIcons().add(new Image("images/icon.png"));
                        dialogStage.initOwner(ownStage);
                        dialogStage.setTitle("Visualisation de la facture");
                        
                        ApercuFactureController t = loader.getController();
                        Scene scene = new Scene(page);
                        
                        t.initialize(factureList.getSelectionModel().getSelectedItem());
                        dialogStage.setScene(scene);
                        dialogStage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else if(ae.getSource() == compBtn){
            compBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            benBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            compPane.setVisible(true);
            
            benBtn.fire();
        }
    }
    
    private java.sql.Date date_min = null;
    private java.sql.Date date_max = null;
    private int i=0;
    
    @FXML
    private TableView<ArrayList<String>> mouvementsList, recapList, compteGestList;
    @FXML
    private Pagination pageBen, pageMouv, pageRecap, pageCompteGest;
    
    @FXML
    private void handleCompClick(ActionEvent ae){
        benBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        mouvBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        compteGestBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        recapBtn.setStyle("-fx-background-color: black; -fx-text-fill: white");
        
        dateMin.setValue(null);
        dateMax.setValue(null);
        
        mouvementsList.setVisible(false);
        comptaList.setVisible(false);
        recapList.setVisible(false);
        compteGestList.setVisible(false);
        
        pageBen.setVisible(false);
        pageRecap.setVisible(false);
        pageMouv.setVisible(false);
        pageCompteGest.setVisible(false);
        
        if(ae.getSource() == mouvBtn){
            mouvBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            mouvementsList.setVisible(true);
            ObservableList<ArrayList<String>> mouvs = FXCollections.observableArrayList(BD.getMouvements("1900-01-01",(new Date().getYear()+1900)+"-12-12"));
            
            mouvementsList.setItems( FXCollections.observableArrayList(mouvs.subList(0, Math.min(18, mouvs.size()))) );
            pageMouv.setVisible(true);
            pageMouv.setPageCount((int)Math.ceil((double)mouvs.size()/18));
            pageMouv.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageMouv.getCurrentPageIndex();
                    mouvementsList.setItems( FXCollections.observableArrayList( mouvs.subList(18*(p-1), Math.min(18*p, mouvs.size())) ) );
                }
            });
            
            mouvementsList.getColumns().clear();
            List<String> columnsLabels = Arrays.asList(new String[]{"Categorie", "Article", "Prix U", "Prix vente", "Initiale", "Entree", "Sortie", "Stock"});
            
            TableColumn<ArrayList<String>, String> tmp = new TableColumn<>(columnsLabels.get(0));
            tmp.setPrefWidth(mouvementsList.getWidth()/8);
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(0));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(1));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(1));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(2));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(2));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(3));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(3));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(4));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(4));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(5));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(5));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(6));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(6));
            });
            mouvementsList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(7));
            tmp.prefWidthProperty().bind(mouvementsList.widthProperty().divide(8));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(7));
            });
            mouvementsList.getColumns().add(tmp);
            
            dateMin.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    ObservableList<ArrayList<String>> mouvs = null;
                    if(date_max == null)
                        mouvs = FXCollections.observableArrayList(BD.getMouvements(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",(new Date().getYear()+1900)+"-12-12"));
                    else
                        mouvs = FXCollections.observableArrayList(BD.getMouvements(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",date_max+""));
                    date_min =  new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    mouvementsList.setItems(mouvs);
                }
            });
            dateMax.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    ObservableList<ArrayList<String>> mouvs = null;
                    if(date_min == null)
                        mouvs = FXCollections.observableArrayList(BD.getMouvements("1900-01-01",new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    else
                        mouvs = FXCollections.observableArrayList(BD.getMouvements(date_min+"", new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    date_max =  new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    mouvementsList.setItems(mouvs);
                }
            });
            
        }else if(ae.getSource() == benBtn){
            benBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            comptaList.setVisible(true);
            benefices = FXCollections.observableArrayList(BD.getBenefices("1900-01-01",(new Date().getYear()+1900)+"-12-12"));

            comptaList.setItems( FXCollections.observableArrayList(benefices.subList(0, Math.min(18, benefices.size()))) );
            pageBen.setVisible(true);
            pageBen.setPageCount((int)Math.ceil((double)benefices.size()/18));
            pageBen.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageBen.getCurrentPageIndex();
                    comptaList.setItems( FXCollections.observableArrayList( benefices.subList(18*(p-1), Math.min(18*p, benefices.size())) ) );
                }
            });
            
            categCompCol.prefWidthProperty().bind(comptaList.widthProperty().divide(6));
            articleCol.prefWidthProperty().bind(comptaList.widthProperty().divide(6));
            prixUCol.prefWidthProperty().bind(comptaList.widthProperty().divide(6));
            benCol.prefWidthProperty().bind(comptaList.widthProperty().divide(6));
            qteCol.prefWidthProperty().bind(comptaList.widthProperty().divide(6));
            benTotalCol.prefWidthProperty().bind(comptaList.widthProperty().divide(6));
            
            categCompCol.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(0));
            });
            articleCol.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(1));
            });
            prixUCol.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(2)+" FCFA");
            });
            benCol.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(3)+" FCFA");
            });
            qteCol.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(4));
            });
            benTotalCol.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(5)+" FCFA");
            });
            dateMin.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if(date_max == null)
                        benefices = FXCollections.observableArrayList(BD.getBenefices(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",(new Date().getYear()+1900)+"-12-12"));
                    else
                        benefices = FXCollections.observableArrayList(BD.getBenefices(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",date_max+""));
                    date_min =  new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    comptaList.setItems(benefices);
                }
            });
            dateMax.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if(date_min == null)
                        benefices = FXCollections.observableArrayList(BD.getBenefices("1900-01-01",new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    else
                        benefices = FXCollections.observableArrayList(BD.getBenefices(date_min+"", new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    date_max =  new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    comptaList.setItems(benefices);
                }
            });
        } else if(ae.getSource() == recapBtn){
            recapBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            recapList.setVisible(true);
            ObservableList<ArrayList<String>> mouvs = FXCollections.observableArrayList(BD.getRecap("1900-01-01",(new Date().getYear()+1900)+"-12-12"));
            
            recapList.setItems( FXCollections.observableArrayList(mouvs.subList(0, Math.min(18, mouvs.size()))) );
            pageRecap.setVisible(true);
            pageRecap.setPageCount((int)Math.ceil((double)mouvs.size()/18));
            pageRecap.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageRecap.getCurrentPageIndex();
                    recapList.setItems( FXCollections.observableArrayList( mouvs.subList(18*(p-1), Math.min(18*p, mouvs.size())) ) );
                }
            });
            
            recapList.getColumns().clear();
            List<String> columnsLabels = Arrays.asList(new String[]{"Categorie", "Article", "Prix U", "Prix vente", "Initiale", "Entree", "Sortie", "Benefice", "Stock"});
            
            TableColumn<ArrayList<String>, String> tmp = new TableColumn<>(columnsLabels.get(0));
            tmp.setPrefWidth(recapList.getWidth()/8);
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(0));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(1));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(1));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(2));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(2));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(3));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(3));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(4));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(4));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(5));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(5));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(6));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(6));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(7));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(7));
            });
            recapList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(8));
            tmp.prefWidthProperty().bind(recapList.widthProperty().divide(9));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(8));
            });
            recapList.getColumns().add(tmp);
            
            dateMin.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    ObservableList<ArrayList<String>> mouvs = null;
                    if(date_max == null)
                        mouvs = FXCollections.observableArrayList(BD.getRecap(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",(new Date().getYear()+1900)+"-12-12"));
                    else
                        mouvs = FXCollections.observableArrayList(BD.getRecap(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",date_max+""));
                    date_min =  new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    recapList.setItems(mouvs);
                }
            });
            dateMax.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    ObservableList<ArrayList<String>> mouvs = null;
                    if(date_min == null)
                        mouvs = FXCollections.observableArrayList(BD.getRecap("1900-01-01",new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    else
                        mouvs = FXCollections.observableArrayList(BD.getRecap(date_min+"", new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    date_max =  new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    recapList.setItems(mouvs);
                }
            });
            
        } else if(ae.getSource() == compteGestBtn){
            compteGestBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            compteGestList.setVisible(true);
            ObservableList<ArrayList<String>> mouvs = FXCollections.observableArrayList(BD.getCompteGestion("1900-01-01",(new Date().getYear()+1900)+"-12-12"));
            
            compteGestList.setItems( FXCollections.observableArrayList(mouvs.subList(0, Math.min(18, mouvs.size()))) );
            pageCompteGest.setVisible(true);
            pageCompteGest.setPageCount((int)Math.ceil((double)mouvs.size()/18));
            pageCompteGest.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    int p = pageCompteGest.getCurrentPageIndex();
                    compteGestList.setItems( FXCollections.observableArrayList( mouvs.subList(18*(p-1), Math.min(18*p, mouvs.size())) ) );
                }
            });
            
            compteGestList.getColumns().clear();
            List<String> columnsLabels = Arrays.asList(new String[]{"Categorie", "Initiale Achat", "Initiale Vente", "Entree Achat", "Entree Vente", "Sortie Achat", "Sortie Vente"
                                                                     , "Benefice", "EnStock Achat", "EnStock Vente"});
            
            TableColumn<ArrayList<String>, String> tmp = new TableColumn<>(columnsLabels.get(0));
            tmp.setPrefWidth(compteGestList.getWidth()/8);
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(0));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(1));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(1));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(2));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(2));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(3));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(3));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(4));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(4));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(5));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(5));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(6));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(6));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(7));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(7));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(8));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(8));
            });
            compteGestList.getColumns().add(tmp);
            
            tmp = new TableColumn<>(columnsLabels.get(9));
            tmp.prefWidthProperty().bind(compteGestList.widthProperty().divide(10));
            tmp.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().get(9));
            });
            compteGestList.getColumns().add(tmp);
            
            dateMin.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    ObservableList<ArrayList<String>> mouvs = null;
                    if(date_max == null)
                        mouvs = FXCollections.observableArrayList(BD.getCompteGestion(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",(new Date().getYear()+1900)+"-12-12"));
                    else
                        mouvs = FXCollections.observableArrayList(BD.getCompteGestion(new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+"",date_max+""));
                    date_min =  new java.sql.Date( Date.from(dateMin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    compteGestList.setItems(mouvs);
                }
            });
            dateMax.valueProperty().addListener(new ChangeListener<LocalDate>(){
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    ObservableList<ArrayList<String>> mouvs = null;
                    if(date_min == null)
                        mouvs = FXCollections.observableArrayList(BD.getCompteGestion("1900-01-01",new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    else
                        mouvs = FXCollections.observableArrayList(BD.getCompteGestion(date_min+"", new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() )+""));
                    date_max =  new java.sql.Date( Date.from(dateMax.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() );
                    compteGestList.setItems(mouvs);
                }
            });
            
        }
    }
    
    @FXML
    private void handleImageClick() throws MalformedURLException, IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif", "*.jpeg")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(prodPhoto.getScene().getWindow());

        if (selectedFile != null) {

            String imageFile = selectedFile.toURI().toURL().toString();
            
            ph = new Photo();
            ph.setLienPhoto(imageFile);
            
            //Image image = new Image(imageFile);
            //prodPhoto.setImage(image);
            
            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            
            HttpPost httppost = new HttpPost("http://"+Utilities.SERVER_URL+"/upload.php");
            MultipartEntity mpEntity = new MultipartEntity();
            
            ContentBody cbFile = new FileBody(selectedFile);
            
            mpEntity.addPart("userfile", cbFile);
            httppost.setEntity(mpEntity);
            
            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = hc.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            System.out.println(response.getStatusLine());
            if (resEntity != null) {
              System.out.println(EntityUtils.toString(resEntity));
            }
            if (resEntity != null) {
              resEntity.consumeContent();
            }

            hc.getConnectionManager().shutdown();
            
            ph.setLienPhoto(selectedFile.getName());
            System.out.println("====> "+ph.getLienPhoto());
            
            prodPhoto.setImage(new Image("http://"+Utilities.SERVER_URL+"/images/"+ph.getLienPhoto()));
    
        } else {
            //fileSelected.setText("Image file selection cancelled.");
            prodPhoto.setImage(new Image("images/icons8_Google_Images_100px.png"));
        }
    }
    
    @FXML
    private void handleSearch(){
        try {
            prodData = FXCollections.observableArrayList(Produit.find(searchText.getText()+""+searchText1.getText(), 1));
            prodList.setItems( FXCollections.observableArrayList(prodData.subList(0, Math.min(10, prodData.size()) )) );
        } catch (Exception ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleConfigClick() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Config.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.getIcons().add(new Image("images/icon.png"));
        dialogStage.initOwner(ownStage);
        dialogStage.setTitle("Parametres de l'application");
        
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.show();
    }
    
    @FXML
    private void handleFactureCmd() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("commande.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.getIcons().add(new Image("images/icon.png"));
        dialogStage.initOwner(ownStage);
        dialogStage.setTitle("Passer une commande");
        
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.show();

        // Set the person into the controller.
         CommandeController app = loader.getController();

         scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xoff = event.getSceneX();
                yoff = event.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialogStage.setX(event.getScreenX()-xoff);
                dialogStage.setY(event.getScreenY()-yoff);
            }
        });
        Alert a = new Alert(AlertType.CONFIRMATION);
    }
    
    @FXML 
    private void handleSearchAv() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Search.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        SearchController.prods = prodList;

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.getIcons().add(new Image("images/icon.png"));
        dialogStage.initOwner(ownStage);
        dialogStage.setTitle("Recherche avancee");
        
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.show();
        
        prodData = prodList.getItems();
        prodList.setItems( FXCollections.observableArrayList(prodData.subList(0, Math.min(10, prodData.size()) )) );
    }
    
    Stage searchStage() throws Exception {
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
        
        tv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    prodToStock = tv.getSelectionModel().getSelectedItem();
                    st.close();
                    codeLabel.setText(Utilities.idToStr(prodToStock.getCodePro()));
                    codeLabelStock.setText(Utilities.idToStr(prodToStock.getCodePro()));
                    nomLabel.setText(prodToStock.getNomPro());
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
        return st;
    }
    
    @FXML
    private void handleChooseProd() throws Exception{
        Stage st = this.searchStage();
        st.show();
    }
    
    @FXML
    public void handleCodeProd() throws Exception{
        
        Stage st = this.searchStage();
        TableView<Produit> t = (TableView) st.getScene().getRoot().getChildrenUnmodifiable().get(2);
        t.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    System.out.println("double click");
                    prodToStock = t.getSelectionModel().getSelectedItem();
                    st.close();
                    codeLabel.setText(Utilities.idToStr(prodToStock.getCodePro()));
                    codeLabelStock.setText(Utilities.idToStr(prodToStock.getCodePro()));
                    nomLabel.setText(prodToStock.getNomPro());
                    System.out.println("setting code label stock text");
                    try{
                        ArrayList<Facture> facts = Facture.all();
                        ArrayList<Facture> finals = new ArrayList<>();
                        facts.stream().forEach(fact -> {
                            for(Lignefacture l : fact.getLignes()){
                                if(l.getProd().getCodePro() == prodToStock.getCodePro()){
                                    finals.add(fact);
                                    break;
                                }
                            }
                        });
                        System.out.println("fitering factures list");
                        factures = finals;
                        factureList.setItems(FXCollections.observableArrayList(finals));
                    }catch(Exception e){
                        System.out.println("exception to handle");
                    }
                }
            }
        });
        
        st.show();
    }
    
    @FXML
    private void handleRadioSelect(){
        ArrayList<Facture> finals = new ArrayList<>();
        Date deb = null;
        Date fin = null;
        if(jourRadio.isSelected()){
            deb = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 1);
            fin = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 23, 59);
        }
        if(moisRadio.isSelected()){
            deb = new Date(new Date().getYear(), new Date().getMonth(), 1);
            if(new Date().getMonth() == 1)
                fin = new Date(new Date().getYear(), new Date().getMonth(), 28);
            else
                fin = new Date(new Date().getYear(), new Date().getMonth(), 30);
        }
        if(anRadio.isSelected()){
            deb = new Date(new Date().getYear(), 0, 1);
            fin = new Date(new Date().getYear(), 11, 31);
        }
        System.out.println("deb ==> "+deb);
        System.out.println("fin ==> "+fin);
        try {
            for(Facture fact : Facture.all()){
                System.out.println("one date ==> "+fact.getDateFac());
                if(fact.getDateFac().after(deb) && fact.getDateFac().before(fin)){
                    finals.add(fact);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        }
        factureList.setItems(FXCollections.observableArrayList(finals));
    }
    
    double xoff, yoff;

    public void setName() {
        String type = "";
        cmdBtn.setText("Factures");
        if(user_connect.getTypeGest()==1) {
            type="caissiere";
            prodBtn.setVisible(false);
            catBtn.setVisible(false);
            stockBtn.setVisible(false);
        }
        if(user_connect.getTypeGest()==0) {
            type="magasinier";
            cmdBtn.setVisible(false);
        }
        if(user_connect.getTypeGest()==2) {
            type="gestionnaire";
        }
        if(user_connect.getTypeGest()==3){
            type="root";
        }else{
            usersBtn.setVisible(false);
            configBtn.setVisible(false);
        }
        connected.setText(user_connect.getNomGest()+"("+type+")");
    }
}
