package com.marklogic.training;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	private static final String sortOperator = "sort:";
	/*
	 * the Search object will be used to to search the MarkLogic database
	 */
	private Search search = null;
	
	/**
	 * Routes the user to the advanced search page.
	 */
	@RequestMapping("search*")
	public String search( @RequestParam(required=false, value="q") String q, 
			 			  @RequestParam(required=false, value="submitbtn") String submitbtn,
			 			  @RequestParam(required=false, value="sortby") String sortby,
			 			  Model model) {
		
		logger.info("Entered search function.. " );
		
		String arg = processInput(q, submitbtn, sortby);
		logger.info("effective search arg = "+arg );
		
		Sortoptions[] options = fillSortbyOptions(arg);
		
		Song[] songs = null;
		Query query = new Query();
		query.setParameter(arg);
		try {
			 
			songs = search.search(arg);
			
		} catch (Exception e ) {
			logger.error("caught exception in search()"+e.toString() );
		}
		
		
		// set the display mode for the JSP  
		model.addAttribute("mode", "list");
		// add the display data objects for processing in the JSP
		model.addAttribute("songs", songs);
		model.addAttribute("query", query);
		model.addAttribute("sortoptions", options);

		
		return "search";
	}

	/**
	 * Fetches the song detail page.
	 */
	@RequestMapping("detail.html")	
	public String detail(@RequestParam(required=true, value="uri") String uri, Model model) {
		
		logger.info("entered song details controller function");
		String arg = null;
		
		if (uri != null) {
			logger.info("uri = "+uri );
			arg = uri;
		} else {
			arg = "";			
		}
		
		Song song = null;
		try {
			song = search.getSongDetails(arg);
		} catch (Exception e) {
			logger.error("caught exception in detail()"+e.toString() );
		}

		model.addAttribute("song", song);
		// set the display mode for the JSP  
		model.addAttribute("mode", "detail");
		
		return "search";
	}
	/**
	 * serves images from MarkLogic
	 */
	@RequestMapping("image")	
	public void serveImage(@RequestParam(required=true, value="uri") String uri, HttpServletResponse response) {
		
		logger.info("serving  image "+ uri);
		
		InputStream is = search.serveImage(uri);
	    // copy it to response's OutputStream
	    try {
			IOUtils.copy(is, response.getOutputStream());
			//response.setContentType("application/jpeg"); 
			response.flushBuffer();
		} catch (IOException e) {
		      logger.info("Error writing image to output stream. Filename was '" + uri + "'");
		      throw new RuntimeException("IOError writing image to output stream");
		}

	}

	/**
	 * Routes the user to the advanced search page.
	 */
	@RequestMapping("advanced.html")	
	public String advanced(Locale locale, Model model) {
		
		logger.info("Routing to advanced search page ");
		
		return "advanced";
	}
	/**
	 * processed the advanced search args and displays the corresponding search results page.
	 */
	@RequestMapping("advancedSearch*")	
	public String advancedSearch(Locale locale, Model model) {
		
		logger.info("Routing to advanced search page ");
		
		return this.search("whole buncha advanced stuff","","", model);
	}	
	/**
	 * Routes the user to the special search results based on his birthday
	 */
	@RequestMapping("bday.html")	
	public String birthdaySearch(@RequestParam("bday") String bday, Model model) {
		
		logger.info("Routing to birthday search page ..");
		
		return "search";
	}
	 
	/*
	 * create the MarkLogic Database connection
	 */
	@PostConstruct
	public void init() {
		logger.info("servlet init() called...");
		try {
			search = new Search();
		} catch (Exception e) {
			logger.error("Failed to initialise MarkLogic Search - caught the following exception "+e.toString() );
			logger.error("Failed to initialise MarkLogic Search - search not possible at this time!!!" );
			logger.error("Failed to initialise MarkLogic Search - check MarkLogic Server Health!!!" );
		}
		
	}
	/* 
	 * release the MarkLogic database connection.
	 */
	@PreDestroy
	public void destroy() {
		logger.info("servlet destroy() called...calling stop on MarkLogic Search");
		try {
			search.stop();
		} catch (Exception e ) {
			logger.error("Failed to close MarkLogic Search - caught the following exception "+e.toString() );
			logger.error("Failed to close MarkLogic Search - check MarkLogic Server Health!!!" );
			
		}
	}
	/*
	 * user parameter validation 
	 * calculates the effective search argument
	 * processes the various sort options
	 * (sorts can be specified in search box or using the sortby dropdown)
	 *  
	 */
	private static String processInput(String q, String button, String sortby) {
		
		if (q == null) {
			q = "sort:relevance";
			logger.info("q was null");
		} else if (q == "") {
			logger.info("q was "+q);
			q = "sort:relevance";
		}
		if (button == null) {
			logger.info("button was null");
		} else {
			logger.info("button was "+button);
			
		}
		
		if (sortby == null) {
			logger.info("sortby was null");
		} else {
			logger.info("sortby was "+sortby);
		
		}
		/*
		 * test to see whether
		 * 1. sortby dropdown has been selected
		 * 2. a sort option has been entered on the command line (button != null and sort: present), 
		 * 		so it must be extracted from q (sort:XXXX)
		 * 
		 */
		String sort = null;
		if (button == null) {
			// we only need the value of sortby
			// or to replace any residual sort option in the search
			// box with the value of sortby
			if (isSortOptionPresent(q)) {
				logger.info(" sort argument found");
				sort = replaceSortOptions(q,sortby);
				
				
			} else {
				// insert default sort option
				logger.info("No sort argument found");
				sort = insertSortOptions(q, sortby);
			}

		} else {
			// extract the sort query out of q
			if (isSortOptionPresent(q)) {
				logger.info(" sort argument found");
				sort = q;
				
			} else {
				// insert default sort option
				logger.info("No sort argument found");
				sort = insertSortOptions(q, "relevance");
			}
			
		}
		logger.info("sort = "+sort);
		return sort;
	}
	private static boolean isSortOptionPresent(String q) {
		CharSequence cs = (CharSequence) sortOperator;
		return q.contains(cs);

	}
	private static Sortoptions[] fillSortbyOptions(String q) {
		//count occurence
		String[] tokens = q.split(" ");
		List<String> sorts = new ArrayList<String>();
		for (int i = 0; i<tokens.length; i++) {
			if (tokens[i].contains(sortOperator)) {
				// offset of char after ":" in "sort:"
				int x = tokens[i].indexOf(":") + 1;
				// offset of last char in string
				int y = tokens[i].length();

				//record the sort type with a lookahead
				sorts.add(tokens[i].substring(x, y));
			}
		}
		if (sorts.size() > 1)
			logger.error(sorts.size() + " sort operators found : " );
		
		String opt = sorts.get(0);
		logger.info(" selected sort option should be " + opt + " boolean select "+ opt.equals(SortOperatorValues.newest.toString()) );
		Sortoptions[] options = new Sortoptions[5];
		options[0] = new Sortoptions( SortOperatorValues.relevance.toString(), (opt.equals(SortOperatorValues.relevance.toString())) );
		options[1] = new Sortoptions( SortOperatorValues.newest.toString(), (opt.equals(SortOperatorValues.newest.toString())) );
		options[2] = new Sortoptions( SortOperatorValues.oldest.toString(), (opt.equals(SortOperatorValues.oldest.toString())) );
		options[3] = new Sortoptions( SortOperatorValues.artist.toString(), (opt.equals(SortOperatorValues.artist.toString())) );
		options[4] = new Sortoptions( SortOperatorValues.title.toString(), (opt.equals(SortOperatorValues.title.toString())) );
		
		logger.info("using sort operator " + options[1].getOption() + " selected "+options[1].getSelected() );
		
		return options;
		
	}
	private static String insertSortOptions(String q, String sortby) {
		String[] tokens = q.split(" ");
		
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i<tokens.length; i++) {
			sb.append(tokens[i]);
			sb.append(" ");
		}
		sb.append(sortOperator+sortby);
		
		return sb.toString();
	}

	private static String replaceSortOptions(String q, String sortby) {
		String[] tokens = q.split(" ");
		boolean replaced = false;
		for (int i = 0; i<tokens.length; i++) {
			logger.info(" tokens "+i+" is "+tokens[i]);
			if (tokens[i].contains(sortOperator)) {
				// offset of char after ":" in "sort:"
				int x = tokens[i].indexOf(":") + 1;
				// offset of last char in string
				int y = tokens[i].length();
				// old sort option
				String target = tokens[i].substring(x, y);
				logger.info(" target = "+target);
				
				if (target.length() > sortby.length()) {
					char[] spaces = target.toCharArray();
					Arrays.fill(spaces, ' ');
					// space filled array - same size as old option
					// make a string out of the array
					StringBuilder sb = new StringBuilder();
					char[] sortbyChars = sortby.toCharArray();
					for (int j=0; j<spaces.length; j++) {
						if (j < sortby.length()) {
							sb.append(sortbyChars[j]);						
						} else {
							sb.append(spaces[j]);
							
						}
						
					}
					tokens[i] = tokens[i].replace(target, sb.toString()).trim();
					
					logger.info("new token " + tokens[i]);
					
				} else if (target.length() <= sortby.length() ) {
					// replace target with sortby 
					// no probs with trailing characters
					tokens[i] = tokens[i].replace(target, sortby);
					logger.info("new token " + tokens[i]);
					
				}
				replaced = true;				
			}
		}
		if (replaced) {
			logger.info("building new arg string");
			StringBuilder sb = new StringBuilder();
			for (int i= 0; i<tokens.length; i++) {
				sb.append(tokens[i]);
				sb.append(" ");
			}
			return sb.toString();
			
		}
		return q;
	}
	private enum SortOperatorValues {
	    relevance,
	    newest,
	    oldest,
	    artist,
	    title
	 }
	
}
