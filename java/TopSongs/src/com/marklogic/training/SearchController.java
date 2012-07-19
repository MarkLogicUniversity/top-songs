package com.marklogic.training;

import java.util.Locale;

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
	
	
	/**
	 * Routes the user to the advanced search page.
	 */
	@RequestMapping("search*")
	public String search( @RequestParam(required=false, value="q") String q, Model model) {
		
		logger.info("Entered search function.. " );
		
		if (q != null) {
			logger.info("q = "+q );
		}
		
		return "search";
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
		
		logger.info("Routing to birthday search page ");
		
		return "search";
	}
	
	
	
}
