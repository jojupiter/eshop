package classe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Categorie {

    private int idCa;
    private String nomCa;
    private PreparedStatement loadstmt, savestmt, updatestmt, getidcaprod/*,getnamestmt,setnamestmt,getidstmt*/;
    private static Statement stmt;
    
    public Categorie() {
        this.nomCa = "Inconnu";
        initializeDB();
    }

    public Categorie(int id) {
        this.idCa = id;
        initializeDB();
        load();
    }

    public ArrayList<Produit> getAllProd() {
        ArrayList<Produit> listProd = new ArrayList<>();
        try {
            getidcaprod.setString(1, Integer.toString(idCa));
            ResultSet resultSet = getidcaprod.executeQuery();
            while (resultSet.next()) {
                Produit prod = new Produit(resultSet.getInt(1));
                prod.load();
                listProd.add(prod);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return listProd;
    }

    private void initializeDB() {
        try {
            Connection connection = BD.ConnexionMySql();
            savestmt = connection.prepareStatement("insert into categorie(nomCat) values(?)");
            loadstmt = connection.prepareStatement("select * from Categorie where idCat = ?");
            updatestmt = connection.prepareStatement("update Categorie set nomCat = ? where idCat = ?");
            getidcaprod = connection.prepareStatement("select * from produit where idCategorie = ?");
            stmt = connection.createStatement();
            /*
             * 
             * getnamestmt = connection.prepareStatement("select nomCa from Categorie where idCa = ?");
            setnamestmt = connection.prepareStatement("update Categorie set nomCa = ? where idCa = ?");
            getidstmt = connection.prepareStatement("select idCa from Categorie where nomCa = ?");
             * */
        } catch (SQLException e) {
            System.err.println("Sorry we couldn't connect to DB");
        }
    }

    public int getIdCa() {
        /*try{
			initializeDB();
			getidstmt.setString(1, this.nomCa);
			ResultSet resultSet = getidstmt.executeQuery(); 
			while(resultSet.next())
				this.idCa = Integer.parseInt(resultSet.getString(1));	
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}*/
        return this.idCa;
    }
    
    public static ArrayList<Categorie> getAll(){
        ArrayList<Categorie> tmp = new ArrayList<>();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("select * from categorie");
            while(set.next()){
                Categorie c = new Categorie(set.getInt(1));
                c.load();
                tmp.add(c);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }
    
    public static ArrayList<Categorie> find(String pattern){
        ArrayList<Categorie> tmp = new ArrayList<>();
        try {
            Connection connection = BD.ConnexionMySql();
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery("select * from categorie where nomCat like '%"+pattern+"%'");
            while(set.next()){
                Categorie c = new Categorie(set.getInt(1));
                c.load();
                tmp.add(c);
            }
            return tmp;
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tmp;
    }

    /**
     *
     * @param idCa
     */
    public void setIdCa(int idCa) {
        this.idCa = idCa;
    }

    public String getNomCa() {
        /*try{
			initializeDB();
			this.getIdCa();
			getnamestmt.setString(1, Integer.toString(this.idCa));
			ResultSet resultSet = getnamestmt.executeQuery(); 
			while(resultSet.next())
				nomCa = resultSet.getString(1);
			
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}*/
        return this.nomCa;
    }

    /**
     *
     * @param nomCa
     */
    public void setNomCa(String nomCa) {
        /*try{
			initializeDB();
			this.getIdCa();
			setnamestmt.setString(1, nomCa);
			setnamestmt.setString(2, Integer.toString(this.idCa));
			setnamestmt.executeUpdate();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}*/
        this.nomCa = nomCa;

    }

    public void save() {
        try {
            initializeDB();
            // savestmt.setString(1, Integer.toString(this.idCa));
            savestmt.setString(1, this.nomCa);
            savestmt.executeUpdate();
            ResultSet set = savestmt.executeQuery("select last_insert_id()");
            set.next();
            setIdCa(set.getInt(1));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void update() {
        try {

            this.getIdCa();
            this.getNomCa();
            updatestmt.setString(1, this.nomCa);
            updatestmt.setString(2, Integer.toString(this.idCa));
            updatestmt.executeUpdate();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void load() {
        try {

            String one, two;
            this.getIdCa();
            loadstmt.setString(1, Integer.toString(this.idCa));
            ResultSet resultSet = loadstmt.executeQuery();
            while (resultSet.next()) {
                one = resultSet.getString(1);
                two = resultSet.getString(2);

                setIdCa(resultSet.getInt(1));
                setNomCa(resultSet.getString(2));
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return this.getNomCa();
    }
}
