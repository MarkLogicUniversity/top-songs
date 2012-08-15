package com.marklogic.training;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.query.QueryManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrap the connection creation process and make it consistent. Either the connection is created or not (in which 
 * case the constructor will return an exception).
 * It is allowed to have many MarkLogic connections in one application server - these may correspond to different users.
 * However it is assumed that all connections are to one and the same backend server.
 * 
 * @author jcrean
 *
 */
public class MarkLogicConnection {
	
	// logger
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicConnection.class);
	// default property file
	private static final String DEFAULT_PROPERTIES_FILE = "data/marklogic.properties";
	// property names
	private static final String ML_HOST_PROP_NAME = "marklogic.host";
	private static final String ML_PORT_PROP_NAME = "marklogic.port";
	private static final String ML_USER_PROP_NAME = "marklogic.user";
	private static final String ML_PWORD_PROP_NAME = "marklogic.password";
	private static final String ML_AUTH_PROP_NAME = "marklogic.authentication_type";

	// all connection instances share the following properties (see assumption above)
	// they can only be set inside the class and then only once
	// they are publicly accessible
	// Hence private setters and public getters.
	private static String host = null;
	private static String port = null;
	private static String authType = null;
	
	private DatabaseClient client = null;
	
	private static void setHost(String ht) {
		if (host == null) {
			host = ht;			
		} 
	}
	public static String getHost(){
		return host;
	}
	private static void setPort(String pt) {
		if (port == null) {
			port = pt;			
		} 
	}
	public static String getPort() {
		return port;
	}
	private static void setAuthType(String at) {
		if (authType == null) {
			authType = at;			
		} 
	}
	public static String getAuthType() {
		return authType;
	}
	private static void setStaticProps(String host, String port, String authType) {
		setHost(host);
		setPort(port);
		setAuthType(authType);
	}
	
	/**
	 * constructor to create a connection the MarkLogic Database
	 * Uses default name of properties file
	 * private to restrict access
	 */
	public MarkLogicConnection() {
		createConnection(DEFAULT_PROPERTIES_FILE);
		
	}
	/**
	 * 
	 * @param filename name of the file containing MarkLogic connection properties
	 * 
	 * private to restrict access to class creation
	 */
	public MarkLogicConnection(String filename) {
		createConnection(filename);
	}
	/**
	 * 
	 * This constructor should be used to create a MarkLogicConnection on the fly
	 * e.g. when a writer/admin user logs in. 
	 * 
	 * 
	 * @param user username to use in the connection
	 * @param password  password for the above username
	 * 
	 * private to restrict access to class creation
	 */
	public MarkLogicConnection(String user, String password) {
		createConnection(user, password);
	}
	/**
	 * This constructor will be used to create connections on the fly - not at startup.
	 * However if host, port and authtype properties have not been set (i.e. the constructor is called
	 * at startup - then it will read the default properties file to get that data).
	 * 
	 * @param port port to connect to
	 * @param authType authentication type to be used when connecting to the server
	 * @param user username to use in the connection
	 * @param password  password for the above username
	 * @return void
	 */
	private void createConnection(String user, String password) {
		
		logger.info("Creating Connection to MarkLogic db on " + host + ":" + port);
		Properties props = readProps(DEFAULT_PROPERTIES_FILE);
		
		String host = null;
		String port = null;
		String authType = null;
		if (getHost() == null) {
			host = props.getProperty(ML_HOST_PROP_NAME);
		} else {
			host = getHost();
			logger.info("host was not null "+host);
		}
		if (getPort() == null ) {
			port =  props.getProperty(ML_PORT_PROP_NAME);
		} else {
			port = getPort();
			logger.info("port was not null "+port);
		}
		if (getAuthType() == null) {
			authType = props.getProperty(ML_AUTH_PROP_NAME).toUpperCase();
		} else {
			authType = getAuthType();
			logger.info("auth was not null "+authType);
		}
		logger.info("Creating Connection to MarkLogic db on " + host + ":" + port);
			
		Authentication at = Authentication.valueOf(authType );
		int            pt = Integer.parseInt(port);

		try {
			// create the client
			client = DatabaseClientFactory.newClient(host, pt, user, password, at);
		} catch (Exception e) {
			logger.error("Failed to create db connection " + e.toString() + e.getMessage() );
			throw new RuntimeException(e);
		}
		setStaticProps(host,port,authType);
	}
	/**
	 * 
	 * @param filename name of the file containing MarkLogic connection properties
	 * @return void
	 */
	private void createConnection(String filename) {
		
		Properties props = readProps(filename);
		
		// connection parameters for writer user
		String         host            = props.getProperty(ML_HOST_PROP_NAME);
		int            port            = Integer.parseInt(props.getProperty(ML_PORT_PROP_NAME));
		String         user     		= props.getProperty(ML_USER_PROP_NAME);
		String         password 		= props.getProperty(ML_PWORD_PROP_NAME);
		Authentication authType        = Authentication.valueOf(
										 props.getProperty(ML_AUTH_PROP_NAME).toUpperCase()
										 );
		logger.info("Loaded following connection properties " + props.toString());
		logger.info("Creating Connection to MarkLogic db on " + host + ":" + port);
		
		try {
			// create the client
			client = DatabaseClientFactory.newClient(host, port, user, password, authType);
		} catch (Exception e) {
			logger.error("Failed to create db connection " + e.toString() + e.getMessage() );
			throw new RuntimeException(e);
		}
		setStaticProps(host, props.getProperty(ML_PORT_PROP_NAME), props.getProperty(ML_AUTH_PROP_NAME).toUpperCase());

		
	}
	private Properties readProps(String filename) {
				
		logger.info("Loading connection properties from : "+filename);
		
		InputStream propsStream = null;
		try {
			propsStream = MarkLogicConnection.class.getClassLoader().getResourceAsStream(filename);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (propsStream == null)
			throw new RuntimeException("Could not read marklogic properties in " + filename);

		Properties props = new Properties();
		
		try {
			props.load(propsStream);
		} catch (IOException e) {
			throw new IllegalArgumentException(e );
		}
		
		return props;

	}
	/**
	 * accessor for DatabaseClient
	 * usage: conn.getClient().newXMLDocumentManager()
	 * @return
	 */
	public DatabaseClient getClient() {
		return client;
	}
	
	/*
	 * release the MarkLogic db connection
	 * 
	 */
	public void release() {
		logger.info("releasing ML connection");
		client.release();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MarkLogicConnection conn =  new MarkLogicConnection();
			//MarkLogicConnection conn =  null;
			
			try {
				// create a new connection based on the known static details
				MarkLogicConnection conn1 =  new MarkLogicConnection("rest-writer-user","writer");
				
				QueryManager queryMgr = conn1.getClient().newQueryManager();
				conn1.release();
				
			} catch (Exception e) {
				logger.error("Exception : " + e.toString() );
			} finally { 
				// don't leave a hanging connection in the case an exception is thrown in this try block
				if (conn != null) { 
					if (conn.getClient() != null)
						conn.release();
				}
			}
		
		} catch(Exception e) {
			logger.error("Exception :" + e.toString() );
		}


	}

}
