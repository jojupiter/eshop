/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author Nivekiba
 */
public class BD {
    private static String urlPilote = "com.mysql.jdbc.Driver";
    private static String urlBD = "jdbc:mysql://"+Utilities.URL_BD+":"+Utilities.PORT_BD+"/"+Utilities.DBNAME_BD;
    public static Connection connex = null;
    private Statement statement = null;

    public static Connection ConnexionMySql() {
        if(connex==null) {
            try {
                Class.forName(urlPilote);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Pilote du SGBD introuvable");
            }

            try{
                connex =  DriverManager.getConnection(urlBD, Utilities.USER_BD, Utilities.PASS_BD);
                return connex;
            }catch(SQLException ex){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Impossible de se connecter a la Base de Donnees");
                a.showAndWait();
                System.exit(0);
		ex.printStackTrace();
                return null;
            }
        } else {
            return connex;
        }
    }

    public static Statement getStatement()
    {
        try {
            return ConnexionMySql().createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<ArrayList<String>> getBenefices(String dateMin, String dateMax){
        ArrayList tmp = new ArrayList();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("SELECT nomCat AS Categorie, nomPro AS Article, prix_achat AS Prix_Achat, prix - prix_achat AS Benefice_Unitaire\n" +
"		, SORTIE AS Quantite_Vendue, (prix - prix_achat)*SORTIE AS Benefice_Total\n" +
"	FROM\n" +
"		(SELECT codePro, SUM(qte) AS SORTIE\n" +
"		 FROM lignefacture NATURAL JOIN facture\n" +
"		 WHERE dtEnreg BETWEEN '"+dateMin+"' AND '"+dateMax+"'\n" +
"		 GROUP BY codePro) AS TABLE_SORTIE INNER JOIN Produit \n" +
"			ON produit.codePro = TABLE_SORTIE.codePro\n" +
"		INNER JOIN categorie\n" +
"			ON produit.idCategorie = categorie.idCat\n" +
"	ORDER BY 1, 2");
            while (set.next()) {
                ArrayList<String> data = new ArrayList<>();
                data.add(set.getString(1));
                data.add(set.getString(2));
                data.add(set.getString(3));
                data.add(set.getString(4));
                data.add(set.getString(5));
                data.add(set.getString(6));
                tmp.add(data);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }
    
    public static ArrayList<ArrayList<String>> getRecap(String dateMin, String dateMax){
        ArrayList tmp = new ArrayList();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("SELECT nomCat AS Categorie, nomPro AS Article, prix_achat AS Prix_Achat, prix AS Prix_Vente, \n" +
"		COALESCE(ENTREE_INI,0) - COALESCE(SORTIE_INI,0) AS Initiale, COALESCE(ENTREE,0) - COALESCE(ENTREE_INI,0) AS Entree,\n" +
"		COALESCE(SORTIE,0) - COALESCE(SORTIE_INI,0) AS Sortie, (prix - prix_achat)*(COALESCE(SORTIE,0) - COALESCE(SORTIE_INI,0)) AS Benefice,\n" +
"		COALESCE(ENTREE,0) - COALESCE(SORTIE,0) AS Stock\n" +
"	FROM\n" +
"		produit INNER JOIN categorie\n" +
"			ON produit.idCategorie = categorie.idCat\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS ENTREE\n" +
"				   FROM gestionstock\n" +
"				   WHERE dateStock <= '"+dateMax+"'\n" +
"				   GROUP BY codePro) AS TABLE_ENTREE \n" +
"			ON produit.codePro = TABLE_ENTREE.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS SORTIE\n" +
"				   FROM lignefacture NATURAL JOIN facture\n" +
"				   WHERE dtEnreg <= '"+dateMax+"'\n" +
"				   GROUP BY codePro) AS TABLE_SORTIE \n" +
"			ON produit.codePro = TABLE_SORTIE.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS ENTREE_INI\n" +
"				   FROM gestionstock\n" +
"				   WHERE dateStock < '"+dateMin+"'\n" +
"				   GROUP BY codePro) AS TABLE_ENTREE_INI \n" +
"			ON produit.codePro = TABLE_ENTREE_INI.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS SORTIE_INI\n" +
"				   FROM lignefacture NATURAL JOIN facture\n" +
"				   WHERE dtEnreg < '"+dateMin+"'\n" +
"				   GROUP BY codePro) AS TABLE_SORTIE_INI \n" +
"			ON produit.codePro = TABLE_SORTIE_INI.codePro\n" +
"	HAVING Stock > 0\n" +
"	ORDER BY 1, 2");
            while (set.next()) {
                ArrayList<String> data = new ArrayList<>();
                data.add(set.getString(1));
                data.add(set.getString(2));
                data.add(set.getString(3));
                data.add(set.getString(4));
                data.add(set.getString(5));
                data.add(set.getString(6));
                data.add(set.getString(7));
                data.add(set.getString(8));
                data.add(set.getString(9));
                tmp.add(data);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }
    
    public static ArrayList<ArrayList<String>> getMouvements(String dateMin, String dateMax){
        ArrayList tmp = new ArrayList();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("	SELECT nomCat AS Categorie, nomPro AS Article, prix_achat AS Prix_Achat, prix AS Prix_Vente, \n" +
"		COALESCE(ENTREE_INI,0) - COALESCE(SORTIE_INI,0) AS Initiale, COALESCE(ENTREE,0) - COALESCE(ENTREE_INI,0) AS Entree,\n" +
"		COALESCE(SORTIE,0) - COALESCE(SORTIE_INI,0) AS Sortie,\n" +
"		COALESCE(ENTREE,0) - COALESCE(SORTIE,0) AS Stock\n" +
"	FROM\n" +
"		produit INNER JOIN categorie\n" +
"			ON produit.idCategorie = categorie.idCat\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS ENTREE\n" +
"				   FROM gestionstock\n" +
"				   WHERE dateStock <= '"+dateMax+"'\n" +
"				   GROUP BY codePro) AS TABLE_ENTREE \n" +
"			ON produit.codePro = TABLE_ENTREE.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS SORTIE\n" +
"				   FROM lignefacture NATURAL JOIN facture\n" +
"				   WHERE dtEnreg <= '"+dateMax+"'\n" +
"				   GROUP BY codePro) AS TABLE_SORTIE \n" +
"			ON produit.codePro = TABLE_SORTIE.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS ENTREE_INI\n" +
"				   FROM gestionstock\n" +
"				   WHERE dateStock < '"+dateMin+"'\n" +
"				   GROUP BY codePro) AS TABLE_ENTREE_INI \n" +
"			ON produit.codePro = TABLE_ENTREE_INI.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS SORTIE_INI\n" +
"				   FROM lignefacture NATURAL JOIN facture\n" +
"				   WHERE dtEnreg < '"+dateMin+"'\n" +
"				   GROUP BY codePro) AS TABLE_SORTIE_INI \n" +
"			ON produit.codePro = TABLE_SORTIE_INI.codePro\n" +
"	HAVING Entree !=0 OR Sortie != 0\n" +
"	ORDER BY 1, 2");
            while (set.next()) {
                ArrayList<String> data = new ArrayList<>();
                data.add(set.getString(1));
                data.add(set.getString(2));
                data.add(set.getString(3));
                data.add(set.getString(4));
                data.add(set.getString(5));
                data.add(set.getString(6));
                data.add(set.getString(7));
                data.add(set.getString(8));
                tmp.add(data);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }
    
     public static ArrayList<ArrayList<String>> getCompteGestion(String dateMin, String dateMax){
        ArrayList tmp = new ArrayList();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("SELECT nomCat AS Categorie, SUM(prix_achat*(COALESCE(ENTREE_INI,0)-COALESCE(SORTIE_INI,0))) AS Initiale_achat, SUM(prix*(COALESCE(ENTREE_INI,0)-COALESCE(SORTIE_INI,0))) AS Initiale_vente, \n" +
"		SUM(prix_achat*(COALESCE(ENTREE,0)-COALESCE(ENTREE_INI,0))) AS Entree_achat, SUM(prix*(COALESCE(ENTREE,0)-COALESCE(ENTREE_INI,0))) AS Entree_vente, \n" +
"		SUM(prix_achat*(COALESCE(SORTIE,0)-COALESCE(SORTIE_INI,0))) AS Sortie_achat, SUM(prix*(COALESCE(SORTIE,0)-COALESCE(SORTIE_INI,0))) AS Sortie_vente,\n" +
"		SUM((prix-prix_achat)*(COALESCE(SORTIE,0)-COALESCE(SORTIE_INI,0))) AS Bénéfice, SUM(prix_achat*(COALESCE(ENTREE,0)-COALESCE(SORTIE,0))) AS EnStock_achat, \n" +
"		SUM(prix*(COALESCE(ENTREE,0)-COALESCE(SORTIE,0))) AS EnStock_vente\n" +
"	FROM \n" +
"		(SELECT nomCat, COALESCE(prix,0) AS prix, COALESCE(prix_achat,0) AS prix_achat, COALESCE(ENTREE,0) AS ENTREE, COALESCE(SORTIE,0) AS SORTIE, COALESCE(ENTREE_INI,0) AS ENTREE_INI, COALESCE(SORTIE_INI,0) AS SORTIE_INI \n" +
"		FROM categorie LEFT JOIN produit\n" +
"			ON categorie.idCat = produit.idCategorie\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS ENTREE\n" +
"				   FROM gestionstock\n" +
"				   WHERE dateStock <= '"+dateMax+"'\n" +
"				   GROUP BY codePro) AS TABLE_ENTREE \n" +
"			ON produit.codePro = TABLE_ENTREE.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS SORTIE\n" +
"				   FROM lignefacture NATURAL JOIN facture\n" +
"				   WHERE dtEnreg <= '"+dateMax+"'\n" +
"				   GROUP BY codePro) AS TABLE_SORTIE \n" +
"			ON produit.codePro = TABLE_SORTIE.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS ENTREE_INI\n" +
"				   FROM gestionstock\n" +
"				   WHERE dateStock < '"+dateMin+"'\n" +
"				   GROUP BY codePro) AS TABLE_ENTREE_INI \n" +
"			ON produit.codePro = TABLE_ENTREE_INI.codePro\n" +
"		LEFT JOIN (SELECT codePro, SUM(qte) AS SORTIE_INI\n" +
"				   FROM lignefacture NATURAL JOIN facture\n" +
"				   WHERE dtEnreg < '"+dateMin+"'\n" +
"				   GROUP BY codePro) AS TABLE_SORTIE_INI \n" +
"			ON produit.codePro = TABLE_SORTIE_INI.codePro)a\n" +
"	GROUP BY 1");
            while (set.next()) {
                ArrayList<String> data = new ArrayList<>();
                data.add(set.getString(1));
                data.add(set.getString(2));
                data.add(set.getString(3));
                data.add(set.getString(4));
                data.add(set.getString(5));
                data.add(set.getString(6));
                data.add(set.getString(7));
                data.add(set.getString(8));
                data.add(set.getString(9));
                data.add(set.getString(10));
                tmp.add(data);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }
    
}
