package com.marklogic.training.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryBuilder.TermQuery;
import com.marklogic.client.query.StructuredQueryDefinition;
import com.marklogic.training.holder.QueryHolder;

public class AdvancedHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(AdvancedHelper.class);
	
	private static final String OPTIONS_NAME = "advanced-options";

	public static QueryHolder buildQuery(String keywords, String type, String exclude, String genre, String creator, String songtitle) {
		
		// create a query builder for the query options
        StructuredQueryBuilder qb = new StructuredQueryBuilder(OPTIONS_NAME);
        String constraintName = "";

		String[] tokenArr = keywords.split(" ");
		List<String> tokens = new ArrayList<String>(Arrays.asList(tokenArr));
		StringBuilder query = new StringBuilder();
		StructuredQueryDefinition searchQuery = null;
		TermQuery[] termQueryArr = null;
		List<TermQuery> termQueries = new ArrayList<TermQuery>();
		if (type.equals("any")) {
			logger.debug(" type is " + type);
			for (Iterator<String> i = tokens.iterator(); i.hasNext(); ) {
				String arg = i.next();
				query.append(arg);
				// build a list of term queries
				termQueries.add(qb.term(arg));
				if (i.hasNext())
					query.append(" OR ");
			}
			// make an array of term queries then OR them all
			termQueryArr = new TermQuery[termQueries.size()];
			termQueries.toArray(termQueryArr);
			searchQuery = qb.or(termQueryArr); 
		} else if (type.equals("phrase")) {
			logger.debug(" type is " + type);
			query.append("\"");
			for (Iterator<String> i = tokens.iterator(); i.hasNext(); ) {
				query.append(i.next());				
				if (i.hasNext())
					query.append(" ");
			}
			query.append("\"");	
			// create a single term query from the phrase (removing the quotes)
			searchQuery = qb.term(query.toString().replaceAll("\"",""));
		} else if (type.equals("all") && !keywords.equals("")){
			logger.debug(" type is " + type);
			query.append(keywords);
			// iterate through the array of search args creating a new term query for each one
			// then include them all in an AND query.
			for (int i=0; i<tokenArr.length; i++) {
				termQueries.add(qb.term(tokenArr[i]));
			}
			termQueryArr = new TermQuery[termQueries.size()];
			termQueries.toArray(termQueryArr);
			searchQuery = qb.and(termQueryArr); 
			
		}
		// clear the list in order to reuse it
		termQueries.clear();
		StructuredQueryDefinition excludeQuery = null;

		if (exclude != "") {		
			logger.debug(" not query for  " + exclude);
			String[] excludeArr = exclude.split(" ");
			List<String> excludes = new ArrayList<String>(Arrays.asList(excludeArr));
			query.append(" ");
			for (Iterator<String> i = excludes.iterator(); i.hasNext(); ) {
				query.append("-");
				String arg = i.next();
				query.append(arg);
				query.append(" ");
				termQueries.add(qb.term(arg));
			}
			// assume only one not query has been added
			excludeQuery = qb.not(termQueries.get(0)); 
		}
		// if a NOT term has been entered then add it into the searchQuery
		if (excludeQuery != null) {
			searchQuery = qb.and(searchQuery,excludeQuery);
		} 
		// the genre constraint is defined in the search options
		constraintName = "genreSearch";
		StructuredQueryDefinition genreQuery = null;
		if (genre != "" && !genre.equals("all") ) {	
			String[] genreArr = genre.split(" ");
			query.append(" genre:");
			logger.debug("genre array contains elements "+genreArr.length);
			if (genreArr.length == 1 ) {
				query.append(genre);

			} else {
				List<String> genres = new ArrayList<String>(Arrays.asList(genreArr));
				query.append("\"");
				for (Iterator<String> i = genres.iterator(); i.hasNext(); ) {
					query.append(i.next());
					if (i.hasNext())
						query.append(" ");
				}
				query.append("\"");				
			}
			// create a single value query from genre
			genreQuery = qb.valueConstraint(constraintName, genre);
			
		}
		// the creator constraint is defined in the search options
		constraintName = "creator";
		StructuredQueryDefinition creatorQuery = null;
		if (creator != "") {		
			String[] creatorArr = creator.split(" ");
			query.append(" creator:");
			logger.debug("creator array contains elements "+creatorArr.length);
			if (creatorArr.length == 1 ) {
				query.append(creator);			
			} else {
				List<String> creators = new ArrayList<String>(Arrays.asList(creatorArr));
				query.append("\"");
				for (Iterator<String> i = creators.iterator(); i.hasNext(); ) {
					query.append(i.next());
					if (i.hasNext())
						query.append(" ");
				}
				query.append("\"");				
			}
			creatorQuery = qb.wordConstraint(constraintName, creator);
			
		}
		// the creator constraint is defined in the search options
		constraintName = "title";
		StructuredQueryDefinition titleQuery = null;		
		if (songtitle != ""){
			query.append(" title:"+"\""+songtitle.replaceAll("\"", "&quot;")+"\"");
			query.append(" ");
			titleQuery = qb.valueConstraint(constraintName, songtitle.replaceAll("\"", "&quot;"));
		}
		
		logger.debug(" query string advanced is "+query.toString());
		
		StructuredQueryDefinition finalQuery = null;
		List<StructuredQueryDefinition> queries = new ArrayList<StructuredQueryDefinition>();
		if (searchQuery != null)
			queries.add(searchQuery);
		if (genreQuery != null)
			queries.add(genreQuery);
		if (creatorQuery != null) 
			queries.add(creatorQuery);
		if (titleQuery != null)
			queries.add(titleQuery);
		
		if (queries.size() > 0) {
			StructuredQueryDefinition[] queriesArr = new StructuredQueryDefinition[queries.size()];
			queries.toArray(queriesArr);
			finalQuery = qb.and(queriesArr);
		} else {
			// nothing has been entered on the advanced search page
			// return empty search
			finalQuery = qb.term("");
		}
		logger.debug("structured query is " +finalQuery.toString());	
		
		QueryHolder qh = new QueryHolder(query.toString(),finalQuery);
		return qh;
	}
}
