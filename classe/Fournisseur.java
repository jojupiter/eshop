package classe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fournisseur {

	private int idFour;
	private String nomFour;
	private String adresse;
	private String siteWeb;
	private String telFour;
	private String whatsap;
	private PreparedStatement loadstmt, savestmt, updatestmt,getidfourprod;
        private static Statement stmt;
		
	 
	public Fournisseur(int id){
		this.idFour = id;
		initializeDB();
                this.load();
	}

        public Fournisseur() {
            initializeDB();
        }
	public int getIdFour() {
		return this.idFour;
	}

	/**
	 * 
	 * @param idFour
	 */
	public void setIdFour(int idFour) {
		this.idFour = idFour;
	}

	public String getNomFour() {
		return this.nomFour;
	}

	/**
	 * 
	 * @param nomFour
	 */
	public void setNomFour(String nomFour) {
		this.nomFour = nomFour;
	}

	public String getAdresse() {
		return this.adresse;
	}

	/**
	 * 
	 * @param adresse
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getSiteWeb() {
		return this.siteWeb;
	}

	/**
	 * 
	 * @param siteWeb
	 */
	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}

	public String getTelFour() {
		return this.telFour;
	}

	/**
	 * 
	 * @param telFour
	 */
	public void setTelFour(String telFour) {
		this.telFour = telFour;
	}

	public String getWhatsap() {
		return this.whatsap;
	}

	/**
	 * 
	 * @param whatsap
	 */
	public void setWhatsap(String whatsap) {
		this.whatsap = whatsap;
	}
	
	
    
        
	 public final void initializeDB(){
	        try{
	            Connection connection = BD.ConnexionMySql();
	            savestmt = connection.prepareStatement("insert into Fournisseur(nomFour,adresse,siteWeb,tel1,whatsap) values(?,?,?,?,?)");
	            loadstmt = connection.prepareStatement("select * from Fournisseur where idFour = ?");
	            updatestmt = connection.prepareStatement("update Fournisseur set idFour = ?, nomFour = ?, adresse = ?, siteweb = ?, tel1 = ?, whatsap = ? where idFour = ? "); 
	            getidfourprod = connection.prepareStatement("select * from Produit where idFour = ?");
                    stmt = connection.createStatement();
	        }
	        catch(SQLException e){
	            System.err.println("Sorry we couldn't connect to DB");
	        }
	    }
	 
	 
	 public ArrayList<Produitv> getAllProd(){
	        ArrayList<Produitv> listProd = new ArrayList<>();
	        try{
	            getidfourprod.setString(1, Integer.toString(this.idFour));
	            ResultSet resultSet = getidfourprod.executeQuery();
	            while (resultSet.next()){
	            	Produitv prod = new Produitv(resultSet.getInt(2));
	            	prod.load();
	            	listProd.add(prod);
	            }
	            	
	        }
	        catch(Exception ex){
	            System.err.println(ex.getMessage());
	            ex.printStackTrace();
	        }
	        
	    	return listProd;
	    }
        public static ArrayList<Fournisseur> getAll(){
            ArrayList<Fournisseur> tmp = new ArrayList<>();
            try {
                Connection connection = BD.ConnexionMySql();
                Statement stmt = connection.createStatement();
                ResultSet set = stmt.executeQuery("select * from fournisseur");
                while(set.next()){
                    Fournisseur f = new Fournisseur(set.getInt(1));
                    f.load();
                    tmp.add(f);
                }
                return tmp;
            } catch (SQLException ex) {
                Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            }
            return tmp;
        }
        
        public static ArrayList<Fournisseur> find(String pattern){
            ArrayList<Fournisseur> tmp = new ArrayList<>();
            try {
                Connection connection = BD.ConnexionMySql();
                Statement stmt = connection.createStatement();
                //ResultSet set = stmt.executeQuery("select * from fournisseur where nomFour like '%"+pattern+"%' or adresse like '%"+pattern+"%' or siteWeb like '%"+pattern+"%' ");
                ResultSet set = stmt.executeQuery("select * from fournisseur where nomFour like '%"+pattern+"%'");
                while(set.next()){
                    Fournisseur f = new Fournisseur(set.getInt(1));
                    f.load();
                    tmp.add(f);
                }
                return tmp;
            } catch (SQLException ex) {
                Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            }
            return tmp;
        }
	 
	 public void save(){
	        try{
	        	
	            savestmt.setString(1, this.nomFour);
	            savestmt.setString(2, this.adresse);
	            savestmt.setString(3, this.siteWeb);
	            savestmt.setString(4, this.telFour);
	            savestmt.setString(5, this.whatsap);
	            //savestmt.setString(6, Integer.toString(this.idFour));
	            savestmt.executeUpdate();	
	            ResultSet set = savestmt.executeQuery("select last_insert_id()");
	            set.next();
	            setIdFour(set.getInt(1));
	        }
	        catch(Exception ex) {
	            System.err.println(ex.getMessage());
	        }
	    }

	    
	    public void update(){
	        try{
	        	
	        	this.getIdFour();
	        	this.getNomFour();
	            updatestmt.setString(1, Integer.toString(this.idFour));
	            updatestmt.setString(2, this.nomFour);
	            updatestmt.setString(3, this.adresse);
	            updatestmt.setString(4, this.siteWeb);
	            updatestmt.setString(5, this.telFour);
	            updatestmt.setString(6, this.whatsap);
	            updatestmt.setString(7, Integer.toString(this.idFour));
	            updatestmt.executeUpdate();		
	        }
	        catch(Exception ex) {
	            System.err.println(ex.getMessage());
                    ex.printStackTrace();
	        }
	    }

	    
	    private void load(){
	    	try{
	    		
	    		this.getIdFour();
	    		loadstmt.setString(1, Integer.toString(this.idFour));
	    		 ResultSet resultSet = loadstmt.executeQuery();
	             while (resultSet.next())
	             {
	            	 setIdFour(resultSet.getInt(1));
	            	 setNomFour(resultSet.getString(2));
	            	 setAdresse(resultSet.getString(3));
	            	 setTelFour(resultSet.getString(4));
	            	 setWhatsap(resultSet.getString(5));
	            	 setSiteWeb(resultSet.getString(6));
	             }
	             	
	    	}
	    	catch(Exception e){
	    		System.err.println(e.getMessage());
	    	}
            }

}