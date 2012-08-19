package com.hersis.activitytracker.model;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez
 */
public class ImportDao {
    private static final ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("model.ImportDao");
    /**
     * Crea una nueva instancia de SeguimientoEstudiosDAO
     */
    public ImportDao() {
        iniciarLog();
        setDBSystemDir();
        dbProperties = loadDBProperties();
        String driverName = dbProperties.getProperty("derby.driver");
        loadDatabaseDriver(driverName);
        if(!dbExists()) {
            createDatabase();
        }
    }

    private void iniciarLog() {
        boolean append = true;
        try {
            LogManager lm = LogManager.getLogManager();
            String ruta = DIRECTORIO_ARCHIVOS + "/" + "SeguimientoEstudiosDAO.log";
            File archivo = new File(ruta);
            archivo.createNewFile();
            loggerFh = new FileHandler(ruta, append);
            lm.addLogger(logger);
            // El nivel del logger lo controla SeguimientoEstudiosView.
            logger.addHandler(loggerFh);
        } catch (IOException ioe) {
            logger.log(Level.WARNING,"No se pudo crear el FileHandler para el logger"
                    + " SeguimientoEstudiosDAO.log", ioe);
        }
    }

    /**
     * Comprueba que exista la base de datos.
     * @return True si la base de datos existe.
     */
    private boolean dbExists() {
        boolean bExists = false;
        String dbLocation = getDatabaseLocation();
        File dbFileDir = new File(dbLocation);
        if(dbFileDir.exists()) {
            bExists = true;
        }
        return bExists;
    }

    /**
     * Crea el directorio de sistema de la bd en el Home de la aplicacion.
     */
    private void setDBSystemDir() {
        // Decidir cual sera el directorio de sistema de la bd.
        // Ruta de ejecucion de la aplicacion
        String userHomeDir = "/media/Z/tmp/";
        String systemDir = "/media/Z/tmp/";
        System.setProperty("derby.system.home", systemDir);
        
        // Crear el directorio de sistema de la bd.
        File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir();
    }
    
    /**
     * Cargar driver de Derby
     * @param driverName Driver de Derby a utilizar con la base de datos.
     */
    private void loadDatabaseDriver(String driverName) {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "No se pudo cargar el driver de Derby.", e);
        }
    }

    /**
     * Carga las propiedades del archivo Configuration.properties
     */
    private Properties loadDBProperties() {
        dbProperties = new Properties();
        dbProperties.put("user", "admin");
        dbProperties.put("password", "admin");
        dbProperties.put("derby.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        dbProperties.put("derby.url", "jdbc:derby:");
        dbProperties.put("db.schema", "APP");
		
        return dbProperties;
    }
	
    /**
     * Crea las tablas por defecto de la base de datos.
     * @param conn Connection a la base de datos.
     * @return True si las tablas se crearon con exito.
     */
    private boolean crearTablas(Connection conn) {
        boolean bTablasCreadas = false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(STR_CREAR_TABLA_ESTUDIOS);
            stmt.execute(STR_CREAR_TABLA_TIEMPOS);
            conn.close();
            bTablasCreadas = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "No se pudieron crear las tablas en la BD.", e);
        }
        return bTablasCreadas;
    }

    /**
     * Crea la base de datos.
     * @return True si la base de datos y las tablas han sido creadas correctamente. False
     * en caso contrario.
     */
    private boolean createDatabase() {
        boolean bCreada = false;
        Connection conn = null;
        String dbUrl = getDatabaseUrl();
        dbProperties.put("create", "true");
        try {
            conn = DriverManager.getConnection(dbUrl, dbProperties);
            bCreada = crearTablas(conn);
            logger.log(Level.INFO, "Base de datos creada con exito.");
			log.info("Database created successfully in {}", getDatabaseUrl());
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, "No se pudo crear la BD???.", sqle);
        }
        dbProperties.remove("create");
        return bCreada;
    }

    /**
     * Se conecta a la base de datos y prepara los Statement para las futuras consultas.
     * @return True si se ha conectado con exito.
     */
    public boolean connect() {
        String dbUrl = getDatabaseUrl();
//        String dbUrl = getDatabaseLocation();
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            stmtGuardarNuevoEstudio =
                    dbConnection.prepareStatement(STR_GUARDAR_NUEVO_ESTUDIO);
            stmtGuardarNuevoTiempo =
                    dbConnection.prepareStatement(STR_GUARDAR_NUEVO_TIEMPO);
            stmtGetEstudios =
                    dbConnection.prepareStatement(STR_GET_ESTUDIOS);
            stmtGetTiempos =
                    dbConnection.prepareStatement(STR_GET_TIEMPOS);
            stmtBorrarEstudio =
                    dbConnection.prepareStatement(STR_BORRAR_ESTUDIO);
            stmtBorrarTiempo =
                    dbConnection.prepareStatement(STR_BORRAR_TIEMPO);
            isConnected = dbConnection != null;
            logger.log(Level.INFO, "Conectado a la base de datos");
        } catch (SQLException sqle) {
            isConnected = false;
            logger.log(Level.SEVERE, "No se pudo crear la conexion.", sqle);
        }
        return isConnected;
    }

    /**
     * Obtiene el directorio Home del usuario.
     * @return String conteniendo el directorio Home del usuario.
     */
    private String getHomeDir() {
        return System.getProperty("user.home");
    }

    /**
     * Se desconecta de la base de datos. Opuesto a "connect()".
     */
    public void disconnect() {
        if(isConnected) {
            String dbUrl = getDatabaseUrl();
            dbProperties.put("shutdown", "true");
            try {
                DriverManager.getConnection(dbUrl, dbProperties);
            } catch (Exception e) {
            }
            logger.log(Level.INFO, "Desconectado de la base de datos.");
            loggerFh.close();
            isConnected = false;
        }
    }

    /**
     * Devuelve la ruta completa a la base de datos.
     * @return String con la ruta completa a la base de datos.
     */
    private String getDatabaseLocation() {
        String dbLocation = System.getProperty("derby.system.home") + "/"
                + DB_NAME;
        return dbLocation;
    }

    public String getDatabaseUrl() {
        String dbUrl = dbProperties.getProperty("derby.url") + getDatabaseLocation();
        return dbUrl;
    }

    public ArrayList<Activity> getEstudios() {
        ArrayList<Activity> estudios = new ArrayList<>();
        int idEstudio;
        String nombre;
        ResultSet rs;

        try {
            rs = stmtGetEstudios.executeQuery();
            while(rs.next()) {
                idEstudio = rs.getInt(1);
                nombre = rs.getString(2);
                estudios.add(new Activity(nombre));
            }
            rs.close();
        } catch (SQLException sqlEx) {
            logger.log(Level.SEVERE, "Error al recuperar Estudios de la BD.", sqlEx);
        }
        return estudios;
    }

    public ArrayList<Time> getTiempos(ArrayList<Activity> activities) {
        ArrayList<Time> tiempos = new ArrayList<>();
        String nombreEstudio;
        Timestamp start, end, duration;
        ResultSet rs;

        try {
            rs = stmtGetTiempos.executeQuery();
            while (rs.next()) {
                nombreEstudio = rs.getString("NOMBRE");
                start = rs.getTimestamp("INICIO");
                end = rs.getTimestamp("FIN");
                duration = rs.getTimestamp("DURACION");
                tiempos.add(new Time(getActivityIdByName(activities, nombreEstudio), start, end, 
						end.getTime() - start.getTime(), ""));
            }
            rs.close();
        } catch (SQLException sqlEx) {
            logger.log(Level.SEVERE, "No se pudieron recuperar los tiempos de "
                    + "estudio de la BD.", sqlEx);
        }

        return tiempos;
    }
	
	private int getActivityIdByName(ArrayList<Activity> activities, String name) {
		for (Activity a : activities) {
			if (name.equals(a.getName())) return a.getIdActivity();
		}		
		return -1;
	}
	
	public static void main(String [] args) {
		try {
			ImportDao importDao = new ImportDao();
			importDao.connect();
			Dao.getInstance();
			Connection conn = Dao.getConnection();
			
			ArrayList<Activity> estudios = importDao.getEstudios();
			ActivityDao activityDao = ActivityDao.getInstance();
			
			System.out.println("Estudios");
			for(Activity a : estudios) {
				System.out.println(a.toString());
				activityDao.insertActivity(conn, a);
			}
			ArrayList<Activity> activities = activityDao.getActivities(conn);
			
			ArrayList<Time> tiempos = importDao.getTiempos(activities);
			TimeDao timeDao = TimeDao.getInstance();
			System.out.println("Tiempos");
			for(Time t : tiempos) {
				System.out.println(t.toString());
				timeDao.insertTime(conn, t);
			}
			System.out.println("Data import ended.");
		} catch (ClassNotFoundException ex) {
			log.error("Unable to load the database driver.\nError message: {}", ex.getLocalizedMessage());
		} catch (SQLException ex) {
			String [] errors = {ex.getLocalizedMessage(), String.valueOf(ex.getErrorCode()), ex.getSQLState()};
			log.error("Error while accessing the database.\nError message: {}\nError code: {}\nSQLState: {}", 
					errors);
		}
	}

//    private final String DIRECTORIO_ARCHIVOS = "/media/Z/tmp/SeguimientoEstudiosDB";
    private final String DIRECTORIO_ARCHIVOS = "/media/Z/tmp";
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private static String DB_NAME = "SeguimientoEstudiosDB";
    private PreparedStatement stmtGuardarNuevoEstudio;
    private PreparedStatement stmtGuardarNuevoTiempo;
    private PreparedStatement stmtGetEstudios;
    private PreparedStatement stmtGetTiempos;
    private PreparedStatement stmtBorrarEstudio;
    private PreparedStatement stmtBorrarTiempo;
    private PreparedStatement stmtCrearBackup;
    private PreparedStatement stmtRestaurarBackup;
    private PreparedStatement stmtNuevaBdDesdeBackup;

    private FileHandler loggerFh;
    private static final Logger logger = Logger.getLogger("LogSeguimientoEstudios");

    private static final String STR_CREAR_TABLA_ESTUDIOS =
            "CREATE TABLE ESTUDIOS (" +
            "   ID_ESTUDIO INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT ID_ESTUDIO_PK PRIMARY KEY," +
            "   NOMBRE VARCHAR(35) NOT NULL" +
            ")";

    private static final String STR_GET_ESTUDIOS = "SELECT * FROM ESTUDIOS";

    private static final String STR_GUARDAR_NUEVO_ESTUDIO = "INSERT INTO ESTUDIOS (NOMBRE) VALUES (?)";

    private static final String STR_CREAR_TABLA_TIEMPOS =
            "CREATE TABLE TIEMPOS (" +
            "   ID_TIEMPO INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT ID_TIEMPO_PK PRIMARY KEY," +
            "   ID_ESTUDIO INT NOT NULL," +
            "   INICIO TIMESTAMP NOT NULL," +
            "   FIN TIMESTAMP NOT NULL," +
            "   DURACION TIMESTAMP NOT NULL," +
            "   FOREIGN KEY (ID_ESTUDIO) REFERENCES ESTUDIOS(ID_ESTUDIO)" +
            ")";

    private static final String STR_GET_TIEMPOS =
            "SELECT T.ID_TIEMPO, T.ID_ESTUDIO, E.NOMBRE, T.INICIO, T.FIN, T.DURACION " +
            "FROM TIEMPOS T "+
            "JOIN ESTUDIOS E ON T.ID_ESTUDIO=E.ID_ESTUDIO " +
            "ORDER BY T.INICIO DESC";

    private static final String STR_GUARDAR_NUEVO_TIEMPO =
            "INSERT INTO TIEMPOS (ID_ESTUDIO, INICIO, FIN, DURACION) VALUES (?, ?, ?, ?)";

    private static final String STR_BORRAR_ESTUDIO =
            "DELETE " +
            "FROM ESTUDIOS " +
            "WHERE ID_ESTUDIO = ?";

    private static final String STR_BORRAR_TIEMPO =
            "DELETE " +
            "FROM TIEMPOS " +
            "WHERE ID_TIEMPO = ?";

    private static final String STR_CREAR_BACKUP =
            "CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)";

    private static final String STR_RESTAURAR_BACKUP =
            "?;restoreFrom=?";

    private static final String STR_NUEVA_BD_DESDE_BACKUP =
            "?;createFrom=?";
}
