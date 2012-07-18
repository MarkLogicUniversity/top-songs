package com.marklogic.training;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrap the connection creation process and make it consistent. Either the connection is created or not (in which 
 * case the constructor will return an exception).
 * 
 * @author jcrean
 *
 */
public class MarkLogicConnection {
	
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicConnection.class);

	private static DatabaseClient client = null;
	
	/**
	 * constructor to create a connection the MarkLogic Database
	 * Uses default name of properties file
	 */
	public MarkLogicConnection() {
		createConnection("marklogic.properties");
		
	}
	/**
	 * 
	 * @param filename name of the file containing MarkLogic connection properties
	 */
	public MarkLogicConnection(String filename) {
		createConnection(filename);
	}
	/**
	 * 
	 * @param filename name of the file containing MarkLogic connection properties
	 * @return void
	 */
	private static void createConnection(String filename) {
		
		String propsFileName = filename;
		
		logger.info("Loading connection properties from : "+propsFileName);
		
		InputStream propsStream = null;
		try {
			propsStream = LoadDocument.class.getClassLoader().getResourceAsStream(propsFileName);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (propsStream == null)
			throw new RuntimeException("Could not read marklogic properties in " + propsFileName);

		Properties props = new Properties();
		
		try {
			props.load(propsStream);
		} catch (IOException e) {
			throw new IllegalArgumentException(e );
		}
		
		// connection parameters for writer user
		String         host            = props.getProperty("marklogic.host");
		int            port            = Integer.parseInt(props.getProperty("marklogic.port"));
		String         writer_user     = props.getProperty("marklogic.writer_user");
		String         writer_password = props.getProperty("marklogic.writer_password");
		Authentication authType        = Authentication.valueOf(
										 props.getProperty("marklogic.authentication_type").toUpperCase()
										 );
		logger.info("Loaded following connection properties " + props.toString());
		logger.info("Creating Connection to MarkLogic db on " + host + ":" + port);
		
		try {
			// create the client
			client = DatabaseClientFactory.newClient(host, port, writer_user, writer_password, authType);
		} catch (Exception e) {
			logger.error("Failed to create db connection " + e.toString() + e.getMessage() );
			throw new RuntimeException(e);
		}
		
		
	}
	/**
	 * accessor for DatabaseClient
	 * usage: conn.getClient().newXMLDocumentManager()
	 * @return
	 */
	public DatabaseClient getClient() {
		return client;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MarkLogicConnection conn = new MarkLogicConnection();
		if (conn.getClient() != null)
			conn.getClient().release();

	}

}
