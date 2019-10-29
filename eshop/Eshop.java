/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Categorie;
import classe.Gestionnaire;
import classe.Produit;
import classe.Utilities;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Nivekiba
 */
public class Eshop extends Application {
    
    private static double xoff=0, yoff=0, xoff_=0, yoff_=0;
    private static Stage pStage;
    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        Eshop.pStage = pStage;
    }
    
    @Override
    public void start(Stage dialogStage) throws Exception {
        dialogStage.getIcons().add(new Image("images/icon.png"));
        setPrimaryStage(dialogStage);
        /*Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();*/
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        //Stage dialogStage = new Stage();
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.show();

        // Set the person into the controller.
         loginController lo = loader.getController();
         lo.ownStage = dialogStage;
         
        scene.setOnMousePressed((MouseEvent event) -> {
            xoff = event.getSceneX();
            yoff = event.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent event) -> {
            dialogStage.setX(event.getScreenX()-xoff);
            dialogStage.setY(event.getScreenY()-yoff);
        });
    }
    public boolean showCatEditDialog(Categorie categorie) {
        try{
            Eshop eshop= new Eshop();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Eshop.class.getResource("catEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //Création du Stage
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(eshop.getPrimaryStage());
            Scene scenen = new Scene(page);
            dialog.setScene(scenen);

            CatEditDialogController controller = loader.getController();
            controller.setDialogStage(dialog);
            controller.setCategorie(categorie);
            dialog.showAndWait();
        scenen.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xoff_ = event.getSceneX();
                yoff_ = event.getSceneY();
            }
        });
        scenen.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialog.setX(event.getScreenX()-xoff_);
                dialog.setY(event.getScreenY()-yoff_);
            }
        });
            return controller.isOkClicked();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        public boolean showProdEditDialog(Produit produit) {
        try {
            appController app= new appController();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Eshop.class.getResource("prodEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //Création du Stage
            Stage dialog = new Stage();
            
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(app.ownStage);
            dialog.getOwner();
            Scene scenen = new Scene(page);
            dialog.setScene(scenen);

            ProdEditDialogController controller = loader.getController();
            controller.setDialogStage(dialog);
            controller.setProduit(produit);
            dialog.showAndWait(); 
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean showGestEditDialog(Gestionnaire gestSelect) {
                try {
            appController app = new appController();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Eshop.class.getResource("gestEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //Création du Stage
            Stage dialog = new Stage();

            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(app.ownStage);
            dialog.getOwner();
            Scene scenen = new Scene(page);
            dialog.setScene(scenen);

            GestEditDialogController controller = loader.getController();
            controller.setDialogStage(dialog);
            controller.setGestionnaire(gestSelect);
            dialog.showAndWait();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Utilities.loadConfig("src/eshop/infos.xml");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Eshop.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
    }
    
}
