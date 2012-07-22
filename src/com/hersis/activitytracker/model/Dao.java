package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.controler.ErrorMessages;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Observable;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Dao extends Observable implements Closeable{
	private static Dao dao = null;
    private static final Logger log = (Logger) LoggerFactory.getLogger("model.Dao");
    private static Properties dbProperties;
    private static Connection dbConnection;
    private static final String DERBY_SYSTEM_HOME = System.getProperty("user.dir");
    private static final String DB_NAME = "db";
    private static final String SQL_BACKUP_DATABASE = "CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)";
	private static final String SQL_CREATE_ACTIVITIES_TABLE =
            "CREATE TABLE APP.ACTIVITIES (" +
            "   ID_ACTIVITY INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT ID_ACTIVITY_PK PRIMARY KEY," +
            "   NAME VARCHAR(35) UNIQUE NOT NULL," +
            "   DESCRIPTION VARCHAR(200)" +
            ")";
	private static final String SQL_CREATE_TIMES_TABLE =
            "CREATE TABLE APP.TIMES (" +
            "   ID_TIME INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT ID_TIME_PK PRIMARY KEY," +
            "   ID_ACTIVITY INT NOT NULL," +
            "   START_TIME TIMESTAMP NOT NULL," +
            "   END_TIME TIMESTAMP NOT NULL," +
            "   DURATION BIGINT NOT NULL," +
            "   DESCRIPTION VARCHAR(200)," +
            "   FOREIGN KEY (ID_ACTIVITY) REFERENCES APP.ACTIVITIES(ID_ACTIVITY)" +
            ")";

    private Dao() throws ClassNotFoundException, SQLException {
        setDBSystemDir();
        createDbProperties();
        if(!dbExists()) { 
            createDatabase();
        }
    }
	
	/**
	 * Returns a references to a Dao instance.
	 * @return A non-null Dao reference.
	 * @throws ClassNotFoundException If there is a problem while loading the database driver.
	 * @throws SQLException If there is any problem while accessing the database.
	 */
	public static Dao getInstance() throws ClassNotFoundException, SQLException {
		if (dao == null) {
			dao = new Dao();
		}
		return dao;
	}

	/**
	 * Creates a connection to the database. It's needed to be closed with the <code>disconnect()</code> 
	 * method when finished working with the connection.
	 * @return An opened connection to the database.
	 * @throws SQLException If there was an error in the SQL expression employed.
	 * @throws ClassNotFoundException If there was an error when loading the database driver.
	 */
    private static Connection connect() throws SQLException, ClassNotFoundException {
		if (dbConnection != null && !dbConnection.isClosed()) return dbConnection;
        log.debug("Opening database connection...");
        try {
            Class.forName(dbProperties.getProperty("derby.driver"));
            dbConnection = DriverManager.getConnection(getDatabaseUrl(), dbProperties);
        } catch (SQLException ex) {
            log.error("Couldn't connect to the database.\n" +
                    "Message: {}\nError code: {}", ex.getMessage(), ex.getErrorCode());
            throw ex;
        } catch (ClassNotFoundException ex) {
            log.error("Couldn't load database driver.\n" +
                    "Message: {}", ex.getMessage());
            throw ex;
        }
        log.debug("Connection opened successfully.");
        return dbConnection;
    }

	/**
	 * Closes the connection previously created with the <code>connect()</code> method.
	 */
    private static void disconnect() {
        try {
            if(!dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException ex) {
            log.info("Couldn't close the database connection.\nError code: {}", ex.getErrorCode());
        }
        log.debug("Connection closed successfully");
    }
    
    /**
     * Shut-downs the database engine.
     */
    public static void closeDatabaseEngine() throws SQLException {
        try {
            dbProperties.put("shutdown", "true");
            DriverManager.getConnection(dbProperties.getProperty("derby.url"), 
                    dbProperties);
        } catch (SQLException ex) {
			dbProperties.remove("shutdown");
			// At database engine shutdown, Derby throws error 50.000 and SQLState XJ015 to show 
			// that the operation was successful.
			int errorCode = ex.getErrorCode();
			if (!(errorCode == 50000 && "XJ015".equals(ex.getSQLState()))) throw ex;
			log.info("Successfully disconnected from the database engine: {}", errorCode);
        } 
    }

	/**
	 * Initializes the Properties variable for the database with all needed values.
	 */
    private void createDbProperties() {
        dbProperties = new Properties();
        dbProperties.put("user", "admin");
        dbProperties.put("password", "aiiyaeaiadmin");
        dbProperties.put("derby.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        dbProperties.put("derby.url", "jdbc:derby:");
        dbProperties.put("db.schema", "APP");
    }

	/**
	 * Creates the tables of the database.
	 * @param conn An open connection to the database.
	 * @throws SQLException If there is any problem when accessing the database.
	 */
    private void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL_CREATE_ACTIVITIES_TABLE);
            stmt.execute(SQL_CREATE_TIMES_TABLE);
        } catch (SQLException ex) {
            log.error("Error while creating database tables");
            throw ex;
        }
        log.info("Database tables successfully created");
    }

	/**
	 * Creates the application's database.
	 * @throws SQLException If the SQL expression is wrong.
	 */
    private void createDatabase() throws SQLException {
        dbProperties.put("create", "true");
        try (Connection conn = DriverManager.getConnection(getDatabaseUrl(), dbProperties)) {
            createTables(conn);
        } catch (SQLException ex) {
            log.error("Error while creating the database.\nMessage: {}\n" +
                    "Error code: {}", ex.getMessage(), ex.getErrorCode());
            throw ex;
        } finally {
            dbProperties.remove("create");
        }
        log.info("Database successfully created");
    }
    
    /**
     * Returns the database's connection URL.
     * @return The database's connection URL.
     */
    private static String getDatabaseUrl() {
        return dbProperties.getProperty("derby.url") + getDatabaseLocation();
    }

    /**
     * Returns the database's location on the file system.
     * @return The database's location on the file system.
     */
    private static String getDatabaseLocation() {
        return DERBY_SYSTEM_HOME + File.separator + DB_NAME;
    }

    /**
    * Checks if the database exists.
    * @return True if the database exists, false otherwise.
    */
    private boolean dbExists() {
        File dbFile = new File(getDatabaseLocation());
        
        if(dbFile.exists()) {
            return true;
        }
        return false;
    }
	
    /**
     * If doesn't exists, creates the database directory.
     */
    private void setDBSystemDir() {
    	File derbyHomeDir = new File(DERBY_SYSTEM_HOME);
    	
    	if(!derbyHomeDir.exists()) {
    		derbyHomeDir.mkdir();
    	}
    }
	
	/**
	 * Returns an open connection to the database.
	 * @return An open connection to the database.
	 * @throws SQLException If there is any problem when accessing the database.
	 * @throws ClassNotFoundException If there is any problem when loading the database driver.
	 */
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if (dbConnection == null || dbConnection.isClosed()) return connect();
		else return dbConnection;
	}

	/**
	 * Closes the database connection.
	 * @throws IOException 
	 */
	@Override
	public void close() throws IOException {
		disconnect();
	}

	/**
     * Closes the current database.
     */
    public static void closeDatabase() throws SQLException {
		try {
			String dbUrl = getDatabaseUrl();
			dbProperties.put("shutdown", "true");
			dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
		} catch (SQLException ex) {
			dbProperties.remove("shutdown");
			// If the database is successfully shutted-down, Derby throws an SQLException with
			// errorCode = 45.000 and SQLState = 080006.
			int errorCode = ex.getErrorCode();
			if (!(errorCode == 45000 && "08006".equals(ex.getSQLState()))) throw ex;
			log.info("Database successfully shutted-down");
		}
    }

	/**
	 * Creates a backup of the current database in the specified directory.
	 * @param backupPath A valid path where to save the backed-up database.
	 * @return -1 if the backup hasn't been successfully performed.
	 * @throws SQLException If there is a problem when accessing the database.
	 * @throws ClassNotFoundException If there is a problem while loading the database driver.
	 */
	public static int executeBackup(String backupPath) throws SQLException, ClassNotFoundException {
		try (PreparedStatement stmt = getConnection().prepareStatement(SQL_BACKUP_DATABASE)) {
			stmt.setString(1, backupPath);
			int executeUpdate = stmt.executeUpdate();
			log.info("Database backed-up in {}", backupPath);
			return executeUpdate;
		} catch (SQLException ex) {
			ErrorMessages.databaseBackupError("Dao.executeBackup()", ex, backupPath);
		}
		return -1;
	}
	
	/**
	 * Replaces the current database with the specified database backup.
	 * @param backupSourcePath A valid database backup path to restore from.
	 * @throws SQLException If there was any problem when accessing the database.
	 */
	public void restoreBackup(String backupSourcePath) throws SQLException {
		try {
			closeDatabase();
			String connectionString = getDatabaseUrl() + ";restoreFrom=" + backupSourcePath + 
					File.separatorChar + DB_NAME;

			dbConnection = DriverManager.getConnection(connectionString);

			log.info("Database has been restored from {}", backupSourcePath);
				
			// Notify the Observers to allow the GUI update.
			setChanged();
			notifyObservers();
		} catch (SQLException ex) {
			ErrorMessages.databaseRestoreError("Dao.restoreBackup()", ex, backupSourcePath);
			throw ex;
		}
	}

}
