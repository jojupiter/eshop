/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eshop;

import static classe.BD.connex;
import classe.Utilities;
import java.io.File;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * FXML Controller class
 *
 * @author nivekiba
 */
public class ConfigController implements Initializable {

    
    @FXML
    TextField adresse_bd, port_bd, user_bd, pass_bd, name_bd, user_admin, pass_admin, adresse_serveur;
    
    @FXML
    Label connexMsg;
    
    @FXML
    Button testBtn, saveBtn, closeBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        String urlPilote = "com.mysql.jdbc.Driver";
        
        adresse_bd.setText(Utilities.URL_BD);
        port_bd.setText(Utilities.PORT_BD);
        user_bd.setText(Utilities.USER_BD);
        pass_bd.setText(Utilities.PASS_BD);
        name_bd.setText(Utilities.DBNAME_BD);
        
        user_admin.setText(Utilities.ADMIN_USER);
        pass_admin.setText(Utilities.ADMIN_PASS);
        
        adresse_serveur.setText(Utilities.SERVER_URL);
        connexMsg.setText("");
        
        testBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Class.forName(urlPilote);
                } catch (ClassNotFoundException ex) {
                    connexMsg.setTextFill(Color.RED);
                    connexMsg.setText("Pilote de la base de donnees introuvable");
                }

                try{
                    String urlBD = "jdbc:mysql://"+adresse_bd.getText()+":"+port_bd.getText()+"/"+name_bd.getText();
                    connex =  DriverManager.getConnection(urlBD, user_bd.getText(), pass_bd.getText());
                    connexMsg.setTextFill(Color.GREEN);
                    connexMsg.setText("Connexion reussie");
                }catch(SQLException ex){
                    connexMsg.setTextFill(Color.RED);
                    connexMsg.setText("Impossible de se connecter a la BD");
                }
            }
        });
        
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage)closeBtn.getScene().getWindow()).close();
            }
        });
    }    
    
    @FXML
    private void handleSaveConfig() throws Exception {
        File inputFile = new File("src/eshop/infos.xml");
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        
        Node bd = doc.getElementsByTagName("bd").item(0);
        
        ((Element)bd).getElementsByTagName("user").item(0).setTextContent(user_bd.getText());
        ((Element)bd).getElementsByTagName("pass").item(0).setTextContent(pass_bd.getText());
        ((Element)bd).getElementsByTagName("port").item(0).setTextContent(port_bd.getText());
        ((Element)bd).getElementsByTagName("db").item(0).setTextContent(name_bd.getText());
        ((Element)bd).getElementsByTagName("url").item(0).setTextContent(adresse_bd.getText());
        
        Node admin = doc.getElementsByTagName("admin").item(0);
        
        ((Element)admin).getElementsByTagName("user").item(0).setTextContent(user_admin.getText());
        ((Element)admin).getElementsByTagName("pass").item(0).setTextContent(pass_admin.getText());
        
        Node server = doc.getElementsByTagName("server").item(0);
        
        ((Element)server).getElementsByTagName("url").item(0).setTextContent(adresse_serveur.getText());
        
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final DOMSource source = new DOMSource(doc);
        final StreamResult sortie = new StreamResult(inputFile);
        
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");            

        //formatage
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        //sortie
        transformer.transform(source, sortie); 
        Utilities.loadConfig("src/eshop/infos.xml");
        closeBtn.fire();
    }
    
}
