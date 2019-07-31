package database;

import java.sql.*;

public class DataDB {
    private String dbURL = "jdbc:sqlite:omdData.db";
    private String dbDriver = "org.sqlite.JDBC";
    private Connection con;

    // Starts the connection to the database
    public DataDB() throws SQLException {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            System.out.println("Java was unable to find the SQLite connector!");
            e.printStackTrace();
        }
        con = DriverManager.getConnection(dbURL);
        initDB();
    }

    /**
     * Checks if the DB is populated, if not, creates all the necessary tables
     * @throws SQLException
     */
    private void initDB() throws SQLException {
        Statement state = con.createStatement();
        ResultSet check = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='moodles'");

        if (!check.next()) {
            // There is no data in the database
            System.out.println("Building the Database");

            Statement statement = con.createStatement();
            // Moodles table: id | name | url | username | token | userid
            statement.execute("CREATE TABLE moodles(id integer, name varchar(60), url varchar(60), username varchar(60), token varchar(60), userid integer, primary key(id));");
            // Courses table: id | name | moodle_id
            statement.execute("CREATE TABLE courses(id integer , name varchar(60), moodle_id integer, primary key(id));");
            // Sections table: id | name | course_id
            statement.execute("CREATE TABLE sections(id integer, name varchar(60), course_id integer, primary key(id));");
            // Modules table: id | name | section_id
            statement.execute("CREATE TABLE modules(id integer, name varchar(60), section_id integer, primary key(id));");
            // Folders table: id | name | module_id
            statement.execute("CREATE TABLE folders(id integer, name varchar(60), module_id integer, primary key(id));");
            // Files table: id | name | url | time_modified | module_id | folder_id
            statement.execute("CREATE TABLE files(id integer, name varchar(60), url varchar(255), time_modified integer, module_id integer, folder_id integer, primary key(id));");
        }
    }

    /**
     * Checks if the inserted Moodle already exists through it's url and username.
     * @param url Moodle's URL
     * @param username Moodle's username
     * @return if it already exists or not
     * @throws SQLException
     */
    public boolean moodleExists(String url, String username) throws SQLException {
        Statement state = con.createStatement();
        ResultSet check = state.executeQuery("SELECT url, username FROM moodles WHERE url=? AND username=?");

        return check.next();
    }
}
