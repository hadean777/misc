package com.hadean777.misc.webapp.controller;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.manager.HashManager;
import com.hadean777.misc.manager.RandomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CommonController {

	@Autowired
	@Qualifier(AppConstants.MANAGER_BEAN_HASH_MANAGER)
	private HashManager hashManager;

	@Autowired
	@Qualifier(AppConstants.MANAGER_BEAN_RANDOM_MANAGER)
	private RandomManager randomManager;
	
	@RequestMapping("/common/loginPage.do")
	public String goToLoginPage() {
		return "login";
	}

	@RequestMapping(value = "/common/mainPage.do", method = RequestMethod.GET)
	public String goToMainPage() {
		return "main";
	}

	@RequestMapping(value = "/common/mainPage.do", method = RequestMethod.POST)
	public ModelAndView getMainPageWithResults(@RequestParam String sha512) {

		//String sha512res = hashManager.getSHA512(sha512);
		long startSeconds = System.currentTimeMillis()/1000;

		String sha512res = hashManager.proofOfWork512(sha512, 2);

		long endSeconds = System.currentTimeMillis()/1000;

		Long totalTime = endSeconds - startSeconds;

		String result = "Time: " + totalTime + " Input: " + sha512 + " Result: " + sha512res;

		randomManager.startSound();

		return new ModelAndView("main", "sha512res", result);
	}

	@RequestMapping("/secure/common/screenLayout.do")	
	public String screenWithLeftMenu(@RequestParam(value="screen", required=false) String p_screen,
			Model p_model) {
		p_model.addAttribute( "screen", p_screen );
		return "screenLayout";
	}

}
