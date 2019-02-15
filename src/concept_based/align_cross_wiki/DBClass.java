package concept_based.align_cross_wiki;

import java.sql.*;

/**
 * Created by Sahelsoft on 3/8/2018.
 */
public class DBClass {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/wikipediaextraction";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement preparedStatement=null;
    static ResultSet rs=null;

    static String selectSQL;
    static String sqlInsert;
    static String sqlUpdate;
    static  String resultsetValue;
    public static int cc=0;

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

    public static String checkLangLinkOnWiki() {
        try {
            rs=stmt.executeQuery("SELECT ll_title FROM eslanglinks where ll_from="+ FirstXMLParser.myWiki.getId()+" and ll_lang='en'");
            if (rs.next())
                return rs.getString("ll_title");
            else {
                return null;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static void insEnData() {

        try {
            // INSERT a partial record
            sqlInsert = "insert into esarticles(es_id, es_title, es_text, en_title) values (?,?,?,?)";
            preparedStatement = conn.prepareStatement(sqlInsert);
            preparedStatement.setInt(1, Integer.parseInt(FirstXMLParser.myWiki.getId()));
            preparedStatement.setString(2, FirstXMLParser.myWiki.getTitle());
            preparedStatement.setString(3, FirstXMLParser.myWiki.getText());
            preparedStatement.setString(4, FirstXMLParser.myWiki.getCrossTitle());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("error: " + FirstXMLParser.myWiki.getId() + "--error code=" + e.getErrorCode());
        }
    }

    public static boolean CheckHasCross() {
        try {
            boolean hasCross=false;
            selectSQL = "SELECT " + "ll_title as en_title" + " FROM "+ "eslanglinks" +" where ll_from=? and ll_lang=?";
            preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, CrossXMLParser.secondWiki.getId());
            preparedStatement.setString(2,"en" );
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                hasCross=true;
                CrossXMLParser.secondWiki.setCrossTitle(rs.getString("en_title"));
            }
            preparedStatement.close();

            selectSQL = "SELECT " + "ll_from as en_id" + " FROM "+ "enlanglinks" +" where ll_lang=?  and  ll_title=?";
            preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setString(1,"es" );
            preparedStatement.setString(2, CrossXMLParser.secondWiki.getTitle());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                hasCross = true;
                CrossXMLParser.secondWiki.setCrossId(rs.getString("en_id"));
            }
            preparedStatement.close();

            return hasCross;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static String returnENArticleID() {
        try {
            selectSQL = "SELECT " + "en_id" + " FROM "+ "enesarticlesNew" +" where es_title=?";
            preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, CrossXMLParser.secondWiki.getTitle());
            rs = preparedStatement.executeQuery();

            resultsetValue=null;
            if (rs.next()) resultsetValue=rs.getString("en_id");

            preparedStatement.close();
            return resultsetValue;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static void updEnWithSecondData() {
        try {
            sqlUpdate = "update " + "enesarticlesNew" + " set "+ "es_id=?" + " , " + "es_title=?" + " , " + "es_text=?" + " where en_title=? or en_id=?";
            preparedStatement = conn.prepareStatement(sqlUpdate);
            preparedStatement.setInt(1, Integer.parseInt(CrossXMLParser.secondWiki.getId()));
            preparedStatement.setString(2, CrossXMLParser.secondWiki.getTitle());
            preparedStatement.setString(3, CrossXMLParser.secondWiki.getText());
            preparedStatement.setString(4, CrossXMLParser.secondWiki.getCrossTitle());
            preparedStatement.setInt(5, Integer.parseInt(CrossXMLParser.secondWiki.getCrossId()));
            cc= preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(CrossXMLParser.secondWiki.getId()+"===="+e.getErrorCode());
        }
    }

    public static void retrieveUnCrossedElement() {
        try {
            selectSQL = "SELECT " + "count(*) as cnn" + " FROM "+ "enesarticles" +" where es_id=0";
            preparedStatement = conn.prepareStatement(selectSQL);
            rs = preparedStatement.executeQuery();

            resultsetValue=null;
            while (rs.next()) {
                resultsetValue=rs.getString("cnn");
                System.out.println(resultsetValue);
            }

            preparedStatement.close();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    public static void checkWikiRecords() {
        try {
            int i=0;
            selectSQL = "SELECT  * FROM " + "enesarticles" + " where es_title=''";
            preparedStatement = conn.prepareStatement(selectSQL);
            rs = preparedStatement.executeQuery();

            resultsetValue=null;
            while (rs.next()) {
                i++;
                resultsetValue=rs.getString("en_id");
                System.out.println(rs.getString("en_id")+"\t"+
                        rs.getString("es_id")+"\t"+
                        rs.getString("en_title")+
                        rs.getString("es_title")
                );
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dispalyRecords(String tableName) {
        try {
            // Issue a SELECT to check the changes
            String strSelect = "select * from "+tableName;
            System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
            rs = stmt.executeQuery(strSelect);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {   // Move the cursor to the next row
                rsmd=rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i > 1) System.out.print(",  ");
                    System.out.print(rsmd.getColumnName(i) + "= " + rs.getString(i));
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
