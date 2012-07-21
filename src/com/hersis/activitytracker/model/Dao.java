package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.controler.ErrorMessages;
import com.hersis.activitytracker.model.nio.DirUtils;
import com.hersis.activitytracker.view.AlertMessages;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
    private static final String DERBY_SYSTEM_HOME = System.getProperty("user.dir");
    private static final String DB_NAME = "db";
    private static final String SQL_BACKUP_DATABASE = "CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)";
    private static final String SQL_RESTORE_DATABASE = ";restoreFrom=";
    private static final String STR_NEW_DATABASE_FROM_BACKUP = "?;createFrom=?";
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
    private static Properties dbProperties;
    private static Connection dbConnection;

    private Dao() throws ClassNotFoundException, SQLException {
        setDBSystemDir();
        createDbProperties();
        if(!dbExists()) { 
            createDatabase();
        }
    }
	
	public static Dao getInstance() throws ClassNotFoundException, SQLException {
		if (dao == null) {
			dao = new Dao();
		}
		return dao;
	}

	/**
	 * Creates a connection to the database. It's needed to close it with the <code>disconnect()</code> method when 
	 * finished working with the connection.
	 * @return An opened connection to the database.
	 * @throws SQLException If there was an error in the SQL expression employed.
	 * @throws ClassNotFoundException If there was an error when loading the database driver.
	 */
    public static Connection connect() throws SQLException, ClassNotFoundException {
		if (dbConnection != null && !dbConnection.isClosed()) return dbConnection;
        log.debug("Opening database connection...");
        try {
            Class.forName(dbProperties.getProperty("derby.driver"));
			String st = dbProperties.getProperty("derby.driver");
			String s = getDatabaseUrl();
            dbConnection = DriverManager.getConnection(getDatabaseUrl(), dbProperties);
        } catch (SQLException ex) {
            log.error("Couldn't connect to the database.\n" +
                    "Message: {}\nError code: {}", ex.getMessage(), ex.getErrorCode());
			ex.printStackTrace();
            throw ex;
        } catch (ClassNotFoundException e) {
            log.error("Couldn't load database driver.\n" +
                    "Message: {}", e.getMessage());
            throw e;
        }
        log.debug("Connection opened successfully.");
        return dbConnection;
    }

	/**
	 * Closes the connection previously created with the <code>connect()</code> method.
	 */
    public static void disconnect() {
        log.debug("Closing database connection...");
        try {
            if(!dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            log.info("No se pudo cerrar la conexion a la base de datos.\n" +
                    "Codigo de error: {}", e.getErrorCode());
        }
        log.debug("Connection closed successfully");
    }
    
    /**
     * Shut-downs the database.
     */
    public static boolean exitDatabase() {
		boolean exit = true;
		int errorCode = -1;
		
        try {
            dbProperties.put("shutdown", "true");
            DriverManager.getConnection(dbProperties.getProperty("derby.url"), 
                    dbProperties);
        } catch (SQLException ex) {
			exit = AlertMessages.exitSQLException(null, ex);
			errorCode = ex.getErrorCode();
        } finally {
            // Exits if not cancelled by the user.
            if (exit) {
				log.info("Successfully disconnected from database: {}", errorCode);
			}
        }
		dbProperties.remove("shutdown");
		return exit;
    }

    private void createDbProperties() {
        dbProperties = new Properties();
        dbProperties.put("user", "admin");
        dbProperties.put("password", "aiiyaeaiadmin");
        dbProperties.put("derby.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        dbProperties.put("derby.url", "jdbc:derby:");
        dbProperties.put("db.schema", "APP");
    }

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
     * If doesn't exist, creates the database directory.
     */
    private void setDBSystemDir() {
    	File derbyHomeDir = new File(DERBY_SYSTEM_HOME);
    	
    	if(!derbyHomeDir.exists()) {
    		derbyHomeDir.mkdir();
    	}
    }
	
	public static Connection getConnection() {
		return dbConnection;
	}

	@Override
	public void close() throws IOException {
		disconnect();
	}

	public static int executeBackup(String backupPath) throws SQLException, ClassNotFoundException {
		connect();
		try (PreparedStatement stmt = getConnection().prepareStatement(SQL_BACKUP_DATABASE)) {
			stmt.setString(1, backupPath);
			int executeUpdate = stmt.executeUpdate();
			log.info("Database backed-up in {}", backupPath);
			return executeUpdate;
		} catch (SQLException ex) {
			ErrorMessages.databaseBackupError("Dao.executeBackup()", ex, backupPath);
		} finally {
			disconnect();
		}
		return -1;
	}
	
	public void restoreBackup(String backupSourcePath) throws IOException, SQLException, ClassNotFoundException {
////		exitDatabase(); // Try with disconnect();
//		disconnect();
//		Path sourcePath = new File(backupSourcePath + File.separatorChar + DB_NAME).toPath();
//		Path destinationPath = new File (getDatabaseLocation()).toPath();
//
//		DirUtils.deleteIfExists(destinationPath);
//		DirUtils.copy(sourcePath, destinationPath);
////		dao = new Dao();
//		connect();
		// Notify to observers
		
		
		disconnect();
			String connectionString = getDatabaseUrl() + ";restoreFrom=" + backupSourcePath + File.separatorChar + DB_NAME;
	//		
	//		DirUtils dirUtils = new DirUtils();
//			String s = dbProperties.getProperty("derby.driver");
//			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	//		disconnect();
			dbConnection = DriverManager.getConnection(connectionString);
			dbConnection.commit();
			log.info("Database has been restored from {}", backupSourcePath);
			disconnect();
			connect();
			
	//		disconnect();
			
	//		public SeguimientoEstudiosDAO restaurarBackup(String origenBackup) throws SQLException {
	//        String urlRestaurar = getDatabaseUrl() + ";restoreFrom=" + origenBackup;
	//
	//        if (isConnected) {
	//            disconnect();
	//        }
	//        dbConnection = DriverManager.getConnection(urlRestaurar);
	//        logger.log(Level.WARNING, "Se ha restaurado un backup de la BD: {0}", origenBackup);
	//        isConnected = true;
	//        disconnect();
	//        //@todo Corregir error al conectar. Tal vez sea cuestion de tiempo.
	////        connect();
	//    }
	//    }
			
			
//			
		setChanged();
		notifyObservers();
		System.out.println("Hello!");
	}

}
