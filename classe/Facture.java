/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Nivekiba
 */
public class Facture {

    private int idFac;

    private Timestamp dateFac;
    
    private Date dtEnreg;

    private double remise;

    private double montant;

    private String tel;

    private boolean typeFac;

    private Collection<Lignefacture> lignes;
    
    private Gestionnaire gest;

    public Facture() {
        lignes = new ArrayList<>();
    }

    public Facture(Integer idFac) {
        this.idFac = idFac;
        lignes = new ArrayList<>();
    }

    public Facture(Integer idFac, Timestamp dateFac, Date dtEnreg, double remise, double montant, boolean typeFac) {
        this.idFac = idFac;
        this.dateFac = dateFac;
        this.dtEnreg = dtEnreg;
        this.remise = remise;
        this.montant = montant;
        this.typeFac = typeFac;
        lignes = new ArrayList<>();
    }

    public Integer getIdFac() {
        return idFac;
    }

    public Date getDtEnreg() {
        return dtEnreg;
    }

    public void setDtEnreg(Date dtEnreg) {
        this.dtEnreg = dtEnreg;
    }

    public void setIdFac(Integer idFac) {
        this.idFac = idFac;
    }

    public Timestamp getDateFac() {
        return dateFac;
    }

    public void setDateFac(Timestamp dateFac) {
        this.dateFac = dateFac;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean getTypeFac() {
        return typeFac;
    }

    public void setTypeFac(boolean typeFac) {
        this.typeFac = typeFac;
    }

    public Collection<Lignefacture> getLignes() {
        return lignes;
    }
    
    public Gestionnaire getGest() {
        return gest;
    }
    
    public void setGest(Gestionnaire g){
        gest = g;
    }
    
    public void load() throws Exception {
        String sql = "SELECT * FROM facture WHERE idFac = " + idFac;
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        ResultSet rset = etat.executeQuery(sql);
        rset.next();
        dateFac = rset.getTimestamp("dateFac");
        dtEnreg = rset.getDate("dtEnreg");
        
        remise = rset.getDouble("remise");
        montant = rset.getDouble("montant");
        tel = rset.getString("tel");
        typeFac = rset.getBoolean("typeFac");
        gest = new Gestionnaire(rset.getInt("idCaissiere"));
        gest.load();
        
        sql = "SELECT * FROM lignefacture WHERE idFac = " + idFac;
        etat = cx.createStatement();
        ResultSet res = etat.executeQuery(sql);
        while(res.next()){
            Lignefacture lf = new Lignefacture(res.getInt("idLFac"));
            lf.load();
            lf.setFacture(this);
            lignes.add(lf);
        }
    }
    
    public void save() throws Exception {
        String sql = "INSERT INTO facture VALUES (NULL, now(), '"+ new java.sql.Date(dtEnreg.getTime()) +"',"+remise+
                ", "+montant+", '"+tel+"', "+typeFac+", '"+gest.getIdGest()+"');";
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        etat.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = etat.getGeneratedKeys();
        rs.next();
        idFac = rs.getInt(1);
        load();
        for(Lignefacture lf : lignes){
            lf.setFacture(this);
            lf.save();
        }
    }
    
    public static ArrayList<Facture> all() throws Exception{
        ArrayList<Facture> f = new ArrayList<>();
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        ResultSet res = etat.executeQuery("select * from facture order by dateFac desc");
        while(res.next()){
            Facture g = new Facture(res.getInt("idFac"));
            g.load();
            f.add(g);
        }
        return f;
    }
}
