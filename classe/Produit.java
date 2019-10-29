/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nivekiba
 */
public class Produit {
    private int codePro;
    private String nomPro;
    private float prix;
    private float prixAchat;
    private int qte;
    private String description;
    private int codeFour;
    private boolean actif;
    private int idPro;
    private int idCategorie;
    private Categorie categ;
    private static String login = "MPR";
    private static String motDePasse = "powermatevl351";
    private static Connection cx;
    private static Statement etat;
    private static Statement stm;
    private static ResultSet rset;
    private PreparedStatement getidprophoto;
    private ArrayList<Photo> sesPhoto;

    public int getCodePro() {
        return codePro;
    }

    public void setCodePro(int codePro) {
        this.codePro = codePro;
    }

    public String getNomPro() {
        return nomPro;
    }

    public void setNomPro(String nomPro) {
        this.nomPro = nomPro;
    }
    
    public Categorie getCategorie(){
        return categ;
    }
    
    public void setCategorie(Categorie cat){
        this.categ = cat;
    }

    public float getPrix() {
        return prix;
    }

    public float getPrixAchat() {
        return prixAchat;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setPrixAchat(float prix) {
        this.prixAchat = prix;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCodeFour() {
        return codeFour;
    }

    public void setCodeFour(int codeFour) {
        this.codeFour = codeFour;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Produit() {
        initializeDB();
    }

    public Produit(int codePro) {
        this.codePro = codePro;
        initializeDB();
    }

    public Produit(int codePro, String nomPro, float prix, float prixA, int qte, String description, int codeFour, boolean actif, Categorie c) {
        this.codePro = codePro;
        this.nomPro = nomPro.toString();
        this.prix = prix;
        this.prixAchat = prixA;
        this.qte = qte;
        this.description = description.toString();
        this.codeFour = codeFour;
        this.actif = actif;
        this.categ = c;
        initializeDB();
    }

    public void initializeDB() {
        try {
            Connection connection = BD.ConnexionMySql();
            stm = connection.createStatement();
            getidprophoto = connection.prepareStatement("select * from Photo where idPro = ?");
        } catch (SQLException e) {
            System.err.println("Sorry we couldn't connect to DB");
        }
    }

    public void load() throws SQLException, Exception {
        String sql = "SELECT * FROM produit WHERE codePro = " + codePro;
        rset = BD.ConnexionMySql().createStatement().executeQuery(sql);
        rset.next();
        codePro = rset.getInt("codePro");
        nomPro = rset.getString("nomPro");
        prix = rset.getFloat("prix");
        prixAchat = rset.getFloat("prix_achat");
        qte = rset.getInt("qte");
        description = rset.getString("description");
        idCategorie = rset.getInt("idCategorie");
        categ = new Categorie(idCategorie);
        codeFour = rset.getInt("codeFour");
        actif = rset.getBoolean("actif");
        
        sql = "select * from photo where codePro = "+codePro;
        ResultSet result = stm.executeQuery(sql);
        sesPhoto = new ArrayList<>();
        while(result.next()){
            Photo p = new Photo(result.getInt(1));
            p.load();
            sesPhoto.add(p);
        }
    }
    
    public ArrayList<Photo> getPhotos(){
        return this.sesPhoto;
    }

    public void save() throws SQLException, Exception {
        int i = 0;
        codePro = 100100;
        do {
            codePro = Utilities.genId(100100, 999999);
            String sq = "SELECT count(*) FROM produit WHERE codePro = '" + codePro + "'";
            ResultSet result = stm.executeQuery(sq);
            result.next();
            i = result.getInt(1);
        } while (i > 0);
        
        String sql = "INSERT INTO produit VALUES(" + codePro + ",'" + nomPro + "'," + prix + "," + prixAchat + "," + qte + ",'"
                + description + "'," + codeFour + "," + actif + "," + categ.getIdCa() + ");";
        stm.executeUpdate(sql);
        load();
    }

    public static void affiche() throws SQLException, Exception {
        try {
            System.out.println("Initialisation de la connexion");

            /*Chargement du pilote JDBC MySQL*/
            Class.forName("com.mysql.jdbc.Driver").newInstance();

        } catch (ClassNotFoundException ex) {
            System.out.println("Problème au chargement" + ex.toString());
        }
        try {
            /*CréatResultSetion d’une connexion*/
            cx = BD.ConnexionMySql();

            /*Création d’un état de connexion. */
            etat = cx.createStatement();

            /*Extraction de la date courante.*/
            rset = etat.executeQuery("USE shopDB;");

            /*Affichage du résultat.*/
        } catch (SQLException e) {
            System.out.println("SQL EXception");
        }
        String sql = "SELECT * FROM produit;";
        rset = etat.executeQuery(sql);
        ResultSetMetaData rsmd = rset.getMetaData();
        int nombreColonnes = rsmd.getColumnCount(); //récupération du nombre de colonnes de la table

        /* Boucle pour parcourir et afficher toute la table */
        while (rset.next()) {
            for (int i = 1; i <= nombreColonnes; i++) {
                System.out.println(rset.getString(i) + "");
            }
            System.out.println();
        }

    }

    public void update() {
        try {
            stm.executeUpdate("UPDATE produit set nomPro = '" + nomPro + "',"
                    + "prix = " + prix + ", prix_achat = " + prixAchat + "," + "qte = " + qte + "," + "description = '" + description + "'," + "codeFour = "
                    + codeFour + "," + "actif = " + actif + ", idCategorie = "+categ.getIdCa()+" where codePro = " + codePro);
        } catch (SQLException ex) {
            Logger.getLogger(Produit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<Photo> getAllPhoto() {
        ArrayList<Photo> listPhoto = new ArrayList<>();
        try {
            getidprophoto.setString(1, Integer.toString(idPro));
            ResultSet resultSet = getidprophoto.executeQuery();
            while (resultSet.next()) {
                Photo photo = new Photo(resultSet.getInt(1));
                photo.load();
                listPhoto.add(photo);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return listPhoto;
    }

    public static ArrayList<Produit> find(String pattern, int tp) throws SQLException, Exception{
        ArrayList<Produit> tmp = new ArrayList<>();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            /*ResultSet set = stmt.executeQuery("select * from produit where nomPro like '%"+pattern+"%' or codePro like '%"+pattern+"%'");
            while(set.next()){
                Produit c = new Produit(set.getInt(1));
                c.load();
                tmp.add(c);
            }*/
            if (tp == 0) {

                ResultSet set = stmt.executeQuery("select * from produit where nomPro like '%" + pattern + "%'");
                while (set.next()) {
                    Produit c = new Produit(set.getInt(1));
                    c.load();
                    tmp.add(c);
                }
            } else {

                ResultSet set = stmt.executeQuery("select * from produit where codePro like '%" + pattern + "%'");
                while (set.next()) {
                    Produit c = new Produit(set.getInt(1));
                    c.load();
                    tmp.add(c);
                }
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }

    public static ArrayList<Produit> all() throws SQLException, Exception{
        ArrayList<Produit> tmp = new ArrayList<>();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("select * from produit");
            while (set.next()) {
                Produit c = new Produit(set.getInt(1));
                c.load();
                tmp.add(c);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }

    public void addPhoto(Photo p) {
        if(p!=null){
            p.setCodePro(codePro);
            p.save();
        } else {
            Photo tmp = new Photo();
            tmp.setLienPhoto("avatar.jpg");
            addPhoto(tmp);
        }
    }
    
    public String toString(){
        return "( "+Utilities.idToStr(getCodePro())+" ) \n"+getNomPro();
    }
}