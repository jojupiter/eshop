/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author MPR
 */
public class Gestionnaire {
    private int idGest;
    private String nomGest;
    private int typeGest;
    private String login;
    private String pwd;
    private boolean actif;
    PreparedStatement getidgest;

    public int getIdGest() {
        return idGest;
    }

    public void setIdGest(int idGest) {
        this.idGest = idGest;
    }

    public String getNomGest() {
        return nomGest;
    }

    public void setNomGest(String nomGest) {
        this.nomGest = nomGest;
    }

    public int getTypeGest() {
        return typeGest;
    }

    public void setTypeGest(int typeGest) {
        this.typeGest = typeGest;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Gestionnaire(){}
    
    public Gestionnaire (int idGest) {
        this.idGest = idGest;
    }

    public void initializeDB() {
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stm = connection.createStatement();
            getidgest = connection.prepareStatement("select * from Gestionnaire where idGest = ?");
        } catch (SQLException e) {
            System.err.println("Sorry we couldn't connect to DB");
        }
    }

    public static ArrayList<Gestionnaire> getAll() {
        ArrayList<Gestionnaire> listGestionnaire = new ArrayList<>();
        try {
            ResultSet resultSet = BD.getStatement().executeQuery("select * from Gestionnaire");
            while (resultSet.next()) {
                Gestionnaire gestionnaire = new Gestionnaire(resultSet.getInt(1));
                gestionnaire.load();
                listGestionnaire.add(gestionnaire);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return listGestionnaire;
    }
    
    public static Gestionnaire connect(String user, String pass) {
        try {
            ResultSet resultSet = BD.getStatement().executeQuery("select * from gestionnaire where login='"+user+"' && pwd = '"+pass+"' && actif=1");
            while (resultSet.next()) {
                Gestionnaire g = new Gestionnaire(resultSet.getInt("idGest"));
                g.load();
                return g;
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public void save() throws SQLException {
        Connection connection = BD.ConnexionMySql();
        Statement statement = (Statement) connection.createStatement();
        String add = "insert into Gestionnaire values (null,'" + getNomGest() + "'," + getTypeGest() + ",'" + getLogin() + "','" + getPwd()
                + "'," + getActif() + ");";
        statement.executeUpdate(add);
    }

    public void load() throws SQLException, Exception {
        String sql = "SELECT * FROM gestionnaire WHERE idGest = '" + idGest + "'";
        Connection cx = BD.ConnexionMySql();
        Statement etat = cx.createStatement();
        ResultSet rset = etat.executeQuery(sql);
        rset.next();
        idGest = rset.getInt("idGest");
        nomGest = rset.getString("nomGest");
        typeGest = rset.getInt("typeGest");
        login = rset.getString("login");
        pwd = rset.getString("pwd");
        actif = rset.getBoolean("actif");
    }
    
    public String toString(){
        if(getTypeGest()==1)
            return getNomGest()+"(caissier)";
        if(getTypeGest()==0)
            return getNomGest()+"(magasinier)";
        if(getTypeGest()==2)
            return getNomGest()+"(gestionnaire)";
        else
            return "unknown";
    }
        public void update() throws SQLException {
        try {
            Connection cx = BD.ConnexionMySql();
            Statement stm = cx.createStatement();
            stm.executeUpdate(
                    "UPDATE gestionnaire set nomGest = '" + nomGest + "'," + "typeGest = " + typeGest + "," + "login = '"
                            + login + "'," + "pwd = '" + pwd + "'," + "actif = " + actif + " where idGest = " + idGest);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
