package classe;

/**
 *
 * @author Carole
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class Photo {
    private int idPhoto;
    private String lienPhoto;
    private int codePro;

    public Photo()
    {
        
    }
    
    public Photo(int id){
        this.idPhoto = id;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getLienPhoto() {
        return lienPhoto;
    }

    public void setLienPhoto(String lienPhoto) {
        this.lienPhoto = lienPhoto;
    }

    public int getCodePro() {
        return codePro;
    }

    public void setCodePro(int codePro) {
        this.codePro = codePro;
    }

    public void load() {
        try {

            Connection connection = BD.ConnexionMySql();
            Statement statement = (Statement) connection.createStatement();
            String select = "select * from shopDB.photo where idphoto='" + this.getIdPhoto() + "';";
            ResultSet RS = statement.executeQuery(select);
            while (RS.next()) {
                this.setIdPhoto(RS.getInt("idPhoto"));
                this.setCodePro(RS.getInt("codePro"));
                this.setLienPhoto(RS.getString("lienPhoto"));
            }

        } catch (SQLException e) {
            System.err.println("erreur" + e.getMessage());
        }

    }

    public void save() {
        try {

            Connection connection = BD.ConnexionMySql();
            Statement statement = (Statement) connection.createStatement();
            String add = "insert into Photo values (null,'"
                    + this.getLienPhoto() + "'," + this.getCodePro() + ")";
            statement.executeUpdate(add);
        } catch (SQLException ex) {
            System.err.println("message erreur" + ex.getMessage());
        }
    }

    public void update() {
        try {
            Connection connection = BD.ConnexionMySql();
            Statement statement = (Statement) connection.createStatement();
            //String update = "update shopDB.photo set idPhoto = '" + this.getIdPhoto() + "' where idphoto = '"
              //      + this.getIdPhoto() + "';";
            String update1 = "update shopDB.photo set lienPhoto = '" + this.getLienPhoto() + "' where idPhoto = "
                    + this.getIdPhoto() + ";";
            String update3 = "update shopDB.photo set codePro = " + this.getCodePro() + " where idphoto = "
                    + this.getIdPhoto() + ";";
            //statement.executeUpdate(update);
            statement.executeUpdate(update1);
            statement.executeUpdate(update3);

            connection.close();
        } catch (SQLException ex) {
            System.err.println("erreur" + ex.getMessage());
        }
    }
}

