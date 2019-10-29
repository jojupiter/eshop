package eshop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import classe.*;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class ApercuFactureController implements Initializable {
    @FXML
    Label idFactLabel;
    @FXML
    Label typeFactLabel;
    @FXML
    Label numClientLabel;
    @FXML
    Label nomCaissLabel;
    @FXML
    Label dateLabel;
    @FXML
    TableView tableFact;
    @FXML
    Label totalLabel, netLabel;
    @FXML
    Label remiseLabel;
    @FXML
    Button btnFermer;

    @FXML
    AnchorPane factureAnchor;

    @FXML
    AnchorPane factureContainer;



    public void initialize(Facture fact){
        //Renseignement élts fact
        if(fact.getIdFac() !=-1) {
            System.out.println("Id fact" + fact.getIdFac());
            idFactLabel.setText(String.valueOf(fact.getIdFac()+100000));
        }
        else
            idFactLabel.setText("N/D");
        typeFactLabel.setText(!fact.getTypeFac()? "cash":"électronique");
        numClientLabel.setText(fact.getTel());
        nomCaissLabel.setText(fact.getGest().getNomGest());
        dateLabel.setText(new SimpleDateFormat("EE dd MMM yyyy").format(fact.getDtEnreg()));
        
        totalLabel.setText(String.valueOf(fact.getMontant()));
        remiseLabel.setText(String.valueOf(fact.getRemise() + "%"));
        netLabel.setText(String.valueOf(fact.getMontant()*(1-fact.getRemise()/100)));
        //Peuplage du tableau d'éléments fact
        tableFact.getColumns().clear();
        TableColumn<Lignefacture, String> codePro = new TableColumn<>("Produit");
        codePro.setCellValueFactory( (p) -> { return new SimpleObjectProperty("   "+Utilities.idToStr(p.getValue().getProd().getCodePro())+"\n"+p.getValue().getProd().getNomPro()); } );

        TableColumn<Lignefacture, String> prixUnitaire = new TableColumn("Prix Unitaire");
        prixUnitaire.setCellValueFactory( (p) -> new SimpleStringProperty(p.getValue().getPrix()/p.getValue().getQte()+" FCFA"));

        TableColumn<Lignefacture, Integer> quantite = new TableColumn("Quantité");
        quantite.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getQte()));

        TableColumn<Lignefacture, String>  sousTotal = new TableColumn("Sous - Total");
        sousTotal.setCellValueFactory(p -> new SimpleStringProperty( p.getValue().getPrix() +" FCFA"));

        tableFact.setItems(FXCollections.observableArrayList(fact.getLignes()));
        tableFact.getColumns().addAll(codePro, prixUnitaire, quantite, sousTotal);

    }
    public AnchorPane getFactureNode(){
        return factureContainer;
    }
    @FXML void handleBtnFermerClick(){
        ((Stage)(btnFermer.getScene().getWindow())).close();
    }
    @FXML void imprimer(){

        System.out.println("impression");
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job !=null) {
            if (job.showPrintDialog(null)) {
                job.printPage(factureAnchor);
                job.endJob();
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
