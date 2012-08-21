package com.marklogic.training.admin;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.ServerConfigurationManager;
import com.marklogic.client.admin.config.QueryOptions;
import com.marklogic.client.admin.config.QueryOptionsBuilder;
import com.marklogic.client.admin.config.support.RangeSpec;
import com.marklogic.client.io.QueryOptionsHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.training.MarkLogicConnection;
/*
 * This class demonstrates building options with the Java API and writing them to MarkLogic Server.
 * These particular options are needed for the Birthday search box.
 * 
 */
public class LoadOptionsProgrammatically {

	private static final String OPTIONS_NAME = "birthday-query-options";
	private static final String CONSTRAINT_NAME = "week";

	private static final Logger logger = LoggerFactory.getLogger(LoadOptionsProgrammatically.class);

	private static MarkLogicConnection conn = null;

	private static void buildAndLoadOptions(boolean debugOn) {
		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			conn =  new MarkLogicConnection("data/marklogic-admin.properties");
			
			try {
				// add top songs namespace to the server
				ServerConfigurationManager configManager = conn.getClient().newServerConfigManager();
				configManager.newNamespacesManager().updatePrefix("ts", "http://marklogic.com/MLU/top-songs");

				
				QueryOptionsManager qom =  conn.getClient().newServerConfigManager().newQueryOptionsManager();

				// Create a builder for constructing query configurations.
				QueryOptionsBuilder qob = new QueryOptionsBuilder();
				RangeSpec rs = qob.elementRangeIndex(new QName("http://marklogic.com/MLU/top-songs","week"), qob.rangeType(new QName("xs:date")) );
				
				// create the query options - a simple range constraint and a sort order.
				QueryOptionsHandle queryOptions = new QueryOptionsHandle()
					.withSortOrders(qob.sortOrder(rs, QueryOptions.QuerySortOrder.Direction.ASCENDING))
					.withConstraints(
						qob.constraint(CONSTRAINT_NAME,
								qob.range(rs)
										));

				// write the query options to the database
				qom.writeOptions(OPTIONS_NAME, queryOptions);		
				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				StringHandle readHandle = new StringHandle();

				// read the query options from the database
				qom.readOptions(OPTIONS_NAME, readHandle);
				
				logger.info("Search Options" +" named "+ OPTIONS_NAME + " : \n" + readHandle.get());
				
				
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
		buildAndLoadOptions(true);
	}

}
