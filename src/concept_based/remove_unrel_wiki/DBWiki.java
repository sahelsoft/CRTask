package concept_based.remove_unrel_wiki;

import java.sql.*;

/**
 * Created by Sahelsoft on 2/21/2018.
 */
public class DBWiki {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/wikipediaextraction";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs=null;


    public static void startWikiConnection() throws SQLException, ClassNotFoundException {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean execQueryOnWiki() {
        try {
              rs=stmt.executeQuery("SELECT ll_lang FROM eslanglinks where ll_from="+SaxHandler.myWiki.getId()+" and ll_lang='en'");
            if (rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static void stopWikiConnection() {
        try {
            if (conn != null)
                conn.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

}
