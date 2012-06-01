package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Dao implements Closeable{
    private final Logger log = (Logger) LoggerFactory.getLogger("model.Dao");
    private static final String DERBY_SYSTEM_HOME = System.getProperty("user.dir");
    private static final String DB_NAME = "db";
    private static final String STR_BACKUP_DATABASE = "CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)";
    private static final String STR_RESTORE_DATABASE = "?;restoreFrom=?";
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
            "   DURATION TIMESTAMP NOT NULL," +
            "   DESCRIPTION VARCHAR(200)," +
            "   FOREIGN KEY (ID_ACTIVITY) REFERENCES APP.ACTIVITIES(ID_ACTIVITY)" +
            ")";
    private Properties dbProperties;
    private Connection dbConnection;

    public Dao() throws ClassNotFoundException, SQLException {
        setDBSystemDir();
        createDbProperties();
        if(!dbExists()) { // Commented for test purpouses only
            createDatabase();
        }
    }

	/**
	 * Creates a connection to the database. It's needed to close it with the <code>disconnect()</code> method when 
	 * finished working with the connection.
	 * @return An opened connection to the database.
	 * @throws SQLException If there was an error in the SQL expression employed.
	 * @throws ClassNotFoundException If there was an error when loading the database driver.
	 */
    public Connection connect() throws SQLException, ClassNotFoundException {
        log.debug("Opening database connection...");
        try {
            Class.forName(dbProperties.getProperty("derby.driver"));
            dbConnection = DriverManager.getConnection(getDatabaseUrl(), dbProperties);
        } catch (SQLException ex) {
            log.error("No se pudo establecer conexion con la base de datos.\n" +
                    "Mensaje: {}\nCodigo de error: {}", ex.getMessage(), ex.getErrorCode());
            throw ex;
        } catch (ClassNotFoundException e) {
            log.error("No se pudo cargar el driver de la base de datos.\n" +
                    "Mensaje: {}", e.getMessage());
            throw e;
        }
        log.debug("Connection opened successfully.");
        return dbConnection;
    }

	/**
	 * Closes the connection previously created with the <code>connect()</code> method.
	 */
    public void disconnect() {
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
    public void exitDatabase() throws SQLException {
//        if(dbConnection != null && !dbConnection.isClosed()) {
            dbProperties.put("shutdown", "true");
            DriverManager.getConnection(dbProperties.getProperty("derby.url"), 
                    dbProperties);
//        }
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
            log.error("Error al crear las tablas de la BD.");
            throw ex;
        }
        log.info("Tablas creadas correctamente");
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
            log.error("Error al crear la base de datos.\nMensaje: {}\n" +
                    "Codigo de error: {}", ex.getMessage(), ex.getErrorCode());
            throw ex;
        } finally {
            dbProperties.remove("create");
        }
        log.info("Base de datos creada con exito");
    }
    
    /**
     * Returns the database's connection URL.
     * @return The database's connection URL.
     */
    private String getDatabaseUrl() {
        return dbProperties.getProperty("derby.url") + getDatabaseLocation();
    }

    /**
     * Returns the database's location on the file system.
     * @return The database's location on the file system.
     */
    private String getDatabaseLocation() {
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
	
	public Connection getConnection() {
		return dbConnection;
	}

	@Override
	public void close() throws IOException {
		disconnect();
	}

}
