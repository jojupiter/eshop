/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MPR
 */
public class GestionStock {
    private int idStock;
    private int qte;
    private Date dateStock;
    private int operation;
    private int idGest;
    private int codePro;
    private Produit prod;
    private Gestionnaire gest;

    public GestionStock() {
        
    }

    public int getIdStock() {
        return idStock;
    }

    private void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public Date getDateStock() {
        return dateStock;
    }

    public void setDateStock(Date dateStock) {
        this.dateStock = dateStock;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getIdGest() {
        return idGest;
    }

    public void setIdGest(int idGest) {
        this.idGest = idGest;
    }

    public int getCodePro() {
        return codePro;
    }

    public void setCodePro(int codePro) {
        this.codePro = codePro;
    }

    public GestionStock(int idStock) {
        setIdStock(idStock);
    }
    
    public Produit getProd() {
        return prod;
    }
    
    public Gestionnaire getGest() {
        return gest;
    }

    public void load() throws SQLException, Exception {
        ResultSet rset;
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        String sql = "SELECT * FROM gestionstock WHERE idStock = '" + this.idStock + "'";
        rset = etat.executeQuery(sql);
        rset.next();
        idStock = rset.getInt("idStock");
        qte = rset.getInt("qte");
        dateStock = rset.getDate("dateStock");
        operation = rset.getInt("operation");
        idGest = rset.getInt("idGest");
        codePro = rset.getInt("codePro");
        prod = new Produit(codePro); prod.load();
        gest = new Gestionnaire(idGest);gest.load();
    }

    public void save() throws SQLException, Exception{
        Connection connection = BD.ConnexionMySql();
        Statement stm = connection.createStatement();
        
        String sql = "INSERT INTO gestionstock VALUES(null," + qte + ",'" + new java.sql.Date(dateStock.getTime()) + "',now()," + operation + ","
                + idGest + "," + codePro + ");";
        stm.executeUpdate(sql);
    }

    public void update() throws SQLException {
        Connection cx = BD.ConnexionMySql();
        Statement stm = cx.createStatement();
        stm.executeUpdate("UPDATE gestionstock set " + "idStock = " + idStock + "," + "qte = " + qte + ","
                + "dateStock = '" + dateStock + "'," + "operation = " + operation + "," + "idGest = " + idGest +" where codePro = "
                + codePro + "");

    }

    public static ArrayList<GestionStock> find(String pattern, int tp) throws SQLException, Exception{
        ArrayList<GestionStock> tmp = new ArrayList<>();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            /*ResultSet set = stmt.executeQuery("select * from gestionstock where idStock like '%"+pattern+"%' or codePro like '%"+pattern+"%'");
            while(set.next()){
                GestionStock c = new GestionStock(set.getInt(1));
                c.load();
                tmp.add(c);
            }*/
            if (tp == 0) {

                ResultSet set = stmt.executeQuery("select * from gestionstock where idStock like '%" + pattern + "%'");
                while (set.next()) {
                    GestionStock gestionnaire = new GestionStock(set.getInt(1));
                    gestionnaire.load();
                    tmp.add(gestionnaire);
                }
            } else {

                ResultSet set = stmt.executeQuery("select * from gestionstock where idStock like '%" + pattern + "%'");
                while (set.next()) {
                    GestionStock c = new GestionStock(set.getInt(1));
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

    public static ArrayList<GestionStock> all() throws SQLException, Exception{
        ArrayList<GestionStock> tmp = new ArrayList<>();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("select * from gestionstock order by dateStock desc;");
            while (set.next()) {
                GestionStock c = new GestionStock(set.getInt(1));
                c.load();
                tmp.add(c);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }

}
