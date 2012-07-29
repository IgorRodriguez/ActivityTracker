package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class FirstVersionImportDao extends Observable implements Closeable{
	private static FirstVersionImportDao importDao = null;
    private static final Logger log = (Logger) LoggerFactory.getLogger("model.FirstVersionImportDao");
    private static Properties dbProperties;
    private static Connection dbConnection;
	private static String databasePath;

    private FirstVersionImportDao(String databasePath) throws ClassNotFoundException, SQLException {
		FirstVersionImportDao.databasePath = databasePath;
        createDbProperties();
    }
	
	/**
	 * Returns a reference to a FirstVersionImportDao instance.
	 * @return A non-null FristVersionImportDao reference.
	 * @throws ClassNotFoundException If there is a problem while loading the database driver.
	 * @throws SQLException If there is any problem while accessing the database.
	 */
	public static FirstVersionImportDao getInstance(String databasePath) throws ClassNotFoundException, SQLException {
		if (importDao == null) {
			importDao = new FirstVersionImportDao(databasePath);
		}
		return importDao;
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
        dbProperties.put("password", "admin");
        dbProperties.put("derby.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        dbProperties.put("derby.url", "jdbc:derby:");
        dbProperties.put("db.schema", "APP");
    }
    
    /**
     * Returns the database's connection URL.
     * @return The database's connection URL.
     */
    private static String getDatabaseUrl() {
        return dbProperties.getProperty("derby.url") + databasePath;
    }

    /**
    * Checks if the database exists.
    * @return True if the database exists, false otherwise.
    */
    private boolean dbExists() {
        File dbFile = new File(databasePath);
        
        if(dbFile.exists()) {
            return true;
        }
        return false;
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

}
