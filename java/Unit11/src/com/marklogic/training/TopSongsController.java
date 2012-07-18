package com.marklogic.training;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TopSongsController {
	
	private static final Logger logger = LoggerFactory.getLogger(TopSongsController.class);

	/**
	 * Routes the user to the advanced search page.
	 */
	@RequestMapping(value = "/advanced.html", method = RequestMethod.GET)
	public String advanced(Locale locale, Model model) {
		
		logger.info("Routing to advanced search page ");
		
		return "advanced";
	}
}
