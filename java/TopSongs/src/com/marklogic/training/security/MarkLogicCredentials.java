package com.marklogic.training.security;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarkLogicCredentials {
	
	public MarkLogicCredentials() {
		
	}
	
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicCredentials.class);
	// users and their passwords
	private Map<String,String> credentials = new HashMap<String,String>(20);
	// users and their roles
	private Map<String,String> roles =  new HashMap<String,String>(20);
	// roles and their authorization level (reader, writer, admin)
	private Map<String,String> authLevels =  new HashMap<String,String>(20);
	// current user
	private String currentUser = null;
	private boolean isLoggedOn = false;
	
	public boolean login(String user, String password, String level) {
		if (!authenticated(user, password)) {
			logger.error(" failed to authenticate user "+user+" with password " +password );
			return false;
		} else if (!authorised(user, level)) {
			logger.error(" user "+user+ " NOT authorised for level "+level );
			return false;			
		} else {
			logger.info( " user "+user+ " logged in as "+level);
			currentUser = user;
			isLoggedOn = true;
			return true;			
		}

	}
	public String getCurrentUser() {
		return currentUser;
	}
	public String getCurrentRole() {
		return roles.get(currentUser);
	}
	public String getCurrentLevel() {
		return authLevels.get(roles.get(currentUser) );
	}
	public boolean isLoggedOn() {
		return isLoggedOn;
	}
	public void logout() {
		currentUser = null;
		isLoggedOn = false;
	}
	private boolean authenticated(String user,String pword) {
		String password = credentials.get(user);
		if (password == null) {
			logger.error(" user "+user+" UNKNOWN " );
			return false;
		}
			
		if (!password.equals(pword) ) {
			logger.error(" password "+pword+" INCORRECT for user "+user );
			return false;
		} else {
			logger.info(" password "+pword+" for user "+user+" correct");
			return true;
		}
		
	}
	private boolean authorised(String user, String level) {
		String role = roles.get(user);
		if (role == null) {
			logger.error(" role for user "+user+" is UNDEFINED " );
			return false;
		}
		String authLevel = authLevels.get(role);
		if (authLevel == null) {
			logger.error(" auth level for role "+role+" is UNDEFINED " );
			return false;
		}
		if ( !isLevelValid(level) ) {		
			logger.error(" input auth level "+level+" is UNKNOWN " );
			return false;
		}
			
				
		int inLevelRole = getOrdinal(level);
		int authLevelRole = getOrdinal(authLevel);
				
		if (authLevelRole >= inLevelRole) {
			logger.info(" user "+user+" with level "+ authLevel+ " authorised for level "+level);
			return true;
		} else {
			logger.error(" user "+user+" with level "+ authLevel+ " NOT authorised for level "+level);
			return false;
			
		}
	}
	public void addCredentials(String user, String password, String role, String level) {
		if (!isLevelValid(level)) {
			logger.error("level = " +level+ "\n level must be one of "+AuthorisationLevel.reader.toString()+
												" or "+AuthorisationLevel.writer.toString()+
												" or "+AuthorisationLevel.admin.toString()		);
			return;
		}
		credentials.put(user, password);
		roles.put(user, role);
		authLevels.put(role, level);
		
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MarkLogicCredentials object : \n");
		sb.append("User to password: \n");
		sb.append(credentials.toString());
		sb.append("\n");
		sb.append("User to role: \n");
		sb.append(roles.toString());
		sb.append("\n");
		sb.append("Role to auth level:\n ");
		sb.append(authLevels.toString());
		sb.append("\n");
		
		return sb.toString();
		
	}
	private boolean isLevelValid(String level) {
		
		return (level.equals(AuthorisationLevel.reader.toString()) 
				|| level.equals(AuthorisationLevel.writer.toString())
				|| level.equals(AuthorisationLevel.admin.toString()) );
	}
	private int getOrdinal(String level) {
		
		if (level.equals(AuthorisationLevel.reader.toString())) 
			return AuthorisationLevel.reader.ordinal();
		
		if (level.equals(AuthorisationLevel.writer.toString())) 
			return AuthorisationLevel.writer.ordinal();
		
		if (level.equals(AuthorisationLevel.admin.toString())) 
			return AuthorisationLevel.admin.ordinal();
		
		return 0;

	}
	private enum AuthorisationLevel {
	    reader,
	    writer,
	    admin
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info(" entering main() ");
		
		try {
			MarkLogicCredentials mlc = new MarkLogicCredentials();
			String allowedLevel = "writer";
			logger.info(" adding credentials ");
			mlc.addCredentials("rest-reader-user", "reader", "rest-reader-role", "reader");
			mlc.addCredentials("rest-writer-user", "writer", "rest-writer-role", "writer");
			mlc.addCredentials("rest-admin-user", "admin", "rest-admin-role", "admin");
			logger.info(" Credentials object is  "+mlc.toString() );

/*			logger.info("joe bloggs authenticated? "+ mlc.authenticated("joe","bloggs") );
			logger.info("rest-reader-user authenticated? "+ mlc.authenticated("rest-reader-user","bloggs") );
			logger.info("rest-reader-user authenticated? "+ mlc.authenticated("rest-reader-user","reader") );
			
			logger.info("rest-reader-user authorised for level admin ? "+ mlc.authorised("rest-reader-user","admin") );
			logger.info("rest-reader-user authorised for level writer ? "+ mlc.authorised("rest-reader-user","writer") );
			logger.info("rest-reader-user authorised for level reader ? "+ mlc.authorised("rest-reader-user","reader") );
			
			logger.info("rest-writer-user authorised for level admin ? "+ mlc.authorised("rest-writer-user","admin") );
			logger.info("rest-writer-user authorised for level writer ? "+ mlc.authorised("rest-writer-user","writer") );
			logger.info("rest-writer-user authorised for level reader ? "+ mlc.authorised("rest-writer-user","reader") );
			
			logger.info("rest-admin-user authorised for level admin ? "+ mlc.authorised("rest-admin-user","admin") );
			logger.info("rest-admin-user authorised for level writer ? "+ mlc.authorised("rest-admin-user","writer") );
			logger.info("rest-admin-user authorised for level reader ? "+ mlc.authorised("rest-admin-user","reader") );
*/
			logger.info("joe bloggs logged in ? "+ mlc.login("joe","bloggs","reader") );
			logger.info("rest-reader-user logged in? "+ mlc.login("rest-reader-user","bloggs","reader") );
			logger.info("rest-reader-user logged in? "+ mlc.login("rest-reader-user","reader","reader") );

			logger.info("rest-reader-user authorised for level admin ? "+ mlc.login("rest-reader-user","reader","admin") );
			logger.info("rest-reader-user authorised for level writer ? "+ mlc.login("rest-reader-user","reader","writer") );
			logger.info("rest-reader-user authorised for level reader ? "+ mlc.login("rest-reader-user","reader","reader") );

			logger.info("rest-writer-user authorised for level admin ? "+ mlc.login("rest-writer-user","writer","admin") );
			logger.info("rest-writer-user authorised for level writer ? "+ mlc.login("rest-writer-user","writer","writer") );
			logger.info("rest-writer-user authorised for level reader ? "+ mlc.login("rest-writer-user","writer","reader") );

			logger.info("rest-admin-user authorised for level admin ? "+ mlc.login("rest-admin-user","admin","admin") );
			logger.info("rest-admin-user authorised for level writer ? "+ mlc.login("rest-admin-user","admin","writer") );
			logger.info("rest-admin-user authorised for level reader ? "+ mlc.login("rest-admin-user","admin","reader") );
			
			logger.info("logged on? "+mlc.isLoggedOn() + " current user "+mlc.getCurrentUser() + " current level "+mlc.getCurrentLevel());
		} catch (Exception e) {
			logger.error("caught exception "+e.toString());
		}

	}

}
