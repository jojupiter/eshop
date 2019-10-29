/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import classe.Photo;
import classe.Produit;
import classe.Utilities;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
 * FXML Controller class
 *
 * @author Nivekiba
 */
public class ProdviewController implements Initializable {
    
    
    @FXML
    private Label codePro, nomPro, descPro, prixPro, catPro;
    
    @FXML
    private ImageView nextBtn, prevBtn, imgPro, addBtn;
    
    public static Produit pro = null;
    int ind = 0;
   
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        codePro.setText(Utilities.idToStr(pro.getCodePro()));
        nomPro.setText(pro.getNomPro());
        descPro.setText(pro.getDescription());
        prixPro.setText(pro.getPrix()+" FCFA");
        catPro.setText(pro.getCategorie().getNomCa());
        
        imgPro.setImage(new Image("http://"+Utilities.SERVER_URL+"/images/"+pro.getPhotos().get(0).getLienPhoto()));
        
        testInd();
        
        prevBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(ind == 0)
                    return;
                ind--;
                imgPro.setImage(new Image("http://"+Utilities.SERVER_URL+"/images/"+pro.getPhotos().get(ind).getLienPhoto()));
                testInd();
            }
        });
        
        nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(ind == pro.getPhotos().size()-1)
                    return;
                ind++;
                imgPro.setImage(new Image("http://"+Utilities.SERVER_URL+"/images/"+pro.getPhotos().get(ind).getLienPhoto()));
                testInd();
            }
        });

        EventHandler ev = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                prevBtn.getScene().setCursor(Cursor.HAND);
                System.out.println("===> entered");
            }
        };
        prevBtn.setOnMouseEntered(ev);
        nextBtn.setOnMouseEntered(ev);
        EventHandler evt = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                prevBtn.getScene().setCursor(Cursor.DEFAULT);
                System.out.println("===> exited");
            }
        };
        prevBtn.setOnMouseExited(evt);
        nextBtn.setOnMouseExited(evt);
    }    
    
    public void testInd(){
        if(ind == 0)
            prevBtn.setOpacity(0.5);
        else
            prevBtn.setOpacity(1);
        if(ind == pro.getPhotos().size()-1)
            nextBtn.setOpacity(0.5);
        else
            nextBtn.setOpacity(1);
    }
    
    @FXML
    private void handleImageClick() throws MalformedURLException, IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif", "*.jpeg")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(imgPro.getScene().getWindow());

        if (selectedFile != null) {

            String imageFile = selectedFile.toURI().toURL().toString();
            
            Photo ph = new Photo();
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
            
            pro.addPhoto(ph);
            try {
                pro.load();
            } catch (Exception ex) {
                Logger.getLogger(ProdviewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            imgPro.setImage(new Image("http://"+Utilities.SERVER_URL+"/images/"+ph.getLienPhoto()));
            ind = pro.getPhotos().size()-1;
            
            testInd();
            
        } else {
            //fileSelected.setText("Image file selection cancelled.");
            imgPro.setImage(new Image("images/icons8_Google_Images_100px.png"));
        }
      
    }
    
}
