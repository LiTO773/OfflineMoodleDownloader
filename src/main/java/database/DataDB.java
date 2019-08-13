package database;

import java.sql.*;

public class DataDB {
    private String dbURL = "jdbc:sqlite:omdData.db";
    private String dbDriver = "org.sqlite.JDBC";

    // Starts the connection to the database
    public DataDB() throws SQLException {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            System.out.println("Java was unable to find the SQLite connector!");
            e.printStackTrace();
        }
        initDB();
    }

    /**
     * Checks if the DB is populated, if not, creates all the necessary tables
     * @throws SQLException
     */
    private void initDB() throws SQLException {
        Connection con = DriverManager.getConnection(dbURL);
        Statement state = con.createStatement();
        ResultSet check = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='moodles'");

        if (!check.next()) {
            // There is no data in the database
            System.out.println("Building the Database");

            Statement statement = con.createStatement();
            // Moodles table: id | name | url | username | token | userid | location | download (bool)
            statement.execute("CREATE TABLE moodles(id integer, name varchar(60), url varchar(60), username varchar(60), token varchar(60), userid integer, location varchar(255), download integer, primary key(id));");
            // Courses table: id | name | moodle_id | download (bool)
            statement.execute("CREATE TABLE courses(id integer , name varchar(60), moodle_id integer, download integer, primary key(id));");
            // Sections table: id | name | course_id | download (bool)
            statement.execute("CREATE TABLE sections(id integer, name varchar(60), course_id integer, download integer, primary key(id));");
            // Folders table: id | name | section_id | download (bool)
            statement.execute("CREATE TABLE folders(id integer, name varchar(60), section_id integer, download integer, primary key(id));");
            // Files table: id | module_name | file_name | url | time_modified | section_id | folder_id | download (bool)
            statement.execute("CREATE TABLE files(id integer, module_name varchar(60), file_name varchar(60), url varchar(255), time_modified integer, section_id integer, folder_id integer, download integer, primary key(id));");
            statement.close();
        }
        check.close();
        state.close();
        con.close();
    }

    /**
     * Checks if the inserted Moodle already exists through it's url and username.
     * @param url Moodle's URL
     * @param username Moodle's username
     * @return if it already exists or not
     * @throws SQLException
     */
    public boolean moodleExists(String url, String username) throws SQLException {
        String sql = "SELECT url, username FROM moodles WHERE url=? AND username=?";

        Connection con = DriverManager.getConnection(dbURL);
        PreparedStatement prep = con.prepareStatement(sql);
        prep.setString(1, url);
        prep.setString(2, username);
        ResultSet rs = prep.executeQuery();
        boolean result = rs.next();
        rs.close();
        prep.close();
        con.close();
        return result;
    }

    /**
     * Creates a new Moodle in the table.
     * @param name Moodle's name
     * @param url Moodle's URL
     * @param username User account username
     * @param token Access token
     * @param userid Access user id
     * @return If the operation was successful
     */
    public boolean newMoodle(String name, String url, String username, String token, int userid) throws SQLException {
        String sql = "INSERT INTO moodles(name,url,username,token,userid) values(?,?,?,?,?);";

        Connection con = DriverManager.getConnection(dbURL);
        PreparedStatement prep = con.prepareStatement(sql);
        prep.setString(1, name);
        prep.setString(2, url);
        prep.setString(3, username);
        prep.setString(4, token);
        prep.setInt(5, userid);
        boolean result = prep.executeUpdate() == 1;
        prep.close();
        con.close();
        return result;
    }
}
