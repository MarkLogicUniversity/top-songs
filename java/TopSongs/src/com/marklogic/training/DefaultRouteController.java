package com.marklogic.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")	
public class DefaultRouteController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	/*
	 * route default URL requests onto the main SearchController
	 */
	@RequestMapping("")
	public String route() {
		logger.info("Default router called");
		return "redirect:/search/search.html";
	}

}
