package com.marklogic.training;

import java.io.IOException;
import java.io.InputStream;
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
	/*
	 * the Search object will be used to to search the MarkLogic database
	 */
	private Search search = null;
	
	/**
	 * Routes the user to the advanced search page.
	 */
	@RequestMapping("search*")
	public String search( @RequestParam(required=false, value="q") String q, Model model) {
		
		logger.info("Entered search function.. " );
		
		String arg = null;
		
		if (q != null) {
			logger.info("q = "+q );
			arg = q;
		} else {
			arg = "";			
		}
		
		Song[] songs = null;
		try {
			 
			songs = search.search(arg);
			
		} catch (Exception e ) {
			logger.error("caught exception in search()"+e.toString() );
		}
		
		
		// set the display mode for the JSP  
		model.addAttribute("mode", "list");
	
		model.addAttribute("songs", songs);
		
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
		
		return this.search("whole buncha advanced stuff", model);
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
	
	
	
}
