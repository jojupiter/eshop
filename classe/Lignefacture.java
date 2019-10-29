/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Nivekiba
 */

public class Lignefacture {

    private int idLFac;

    private double prix;

    private int qte;

    private Facture facture;
    
    private Produit prod;

    public Lignefacture() {
    }

    public Lignefacture(int idLFac) {
        this.idLFac = idLFac;
    }

    public int getIdLFac() {
        return idLFac;
    }

    public void setIdLFac(int idLFac) {
        this.idLFac = idLFac;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    
    public Produit getProd(){
        return prod;
    }
    
    public void setProduit(Produit prod){
        this.prod = prod;
    }

    public void load() throws Exception {
        String sql = "SELECT * FROM lignefacture WHERE idLFac = " + idLFac;
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        ResultSet rset = etat.executeQuery(sql);
        rset.next();
        qte = rset.getInt("qte");
        prix = rset.getDouble("prix");
        prod = new Produit(rset.getInt("codePro"));  prod.load();
    }

    public void save() throws Exception {
        String sql = "INSERT INTO lignefacture values(null, "+prod.getCodePro()+", "+facture.getIdFac()+", "+prix+", "+qte+" )";
        prod.setQte(prod.getQte()-qte);
        prod.update();
        
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        etat.executeUpdate(sql);
    }
}
