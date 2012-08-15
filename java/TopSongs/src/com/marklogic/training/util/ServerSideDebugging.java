package com.marklogic.training.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.ServerConfigurationManager;
import com.marklogic.training.MarkLogicConnection;

/*
 * helper class which sets Server side request logging on
 * this is done in a separate class because admin credentials are required.
 * 
 */
public class ServerSideDebugging {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ServerSideDebugging.class);

	private static MarkLogicConnection conn = null;
	/*
	 * call this method to turn on Server side request logging 
	 * (check the MarkLogic Server Errorlog for debug output)
	 */
	private static void setServerSideDebug(boolean debugOn) {
		ServerConfigurationManager configManager = conn.getClient().newServerConfigManager();
		if (debugOn) {
			configManager.setServerRequestLogging(debugOn);
			configManager.writeConfiguration();
		} else {
			configManager.setServerRequestLogging(debugOn);
			configManager.writeConfiguration();
			
		}

	}
	private static void setDebug(boolean debugOn) {
		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			conn =  new MarkLogicConnection("data/marklogic-admin.properties");
			
			try {
				
				setServerSideDebug(debugOn);
				
				
			} catch (Exception e) {
				logger.error("Exception : " + e.toString() );
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw, true);
				e.printStackTrace(pw);
				pw.flush();
				sw.flush();
				logger.error( sw.toString() );
				
			} finally { 
				// don't leave a hanging connection in the case an exception is thrown in this try block
				if (conn.getClient() != null)
					conn.getClient().release();
			}
		
		} catch(Exception e) {
			logger.error("Exception :" + e.toString() );
		}

	}
		

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setDebug(true);
	}

}
