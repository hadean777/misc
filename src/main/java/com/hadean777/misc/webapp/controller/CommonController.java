package com.hadean777.misc.webapp.controller;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.manager.HashManager;
import com.hadean777.misc.manager.RandomManager;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.text.DecimalFormat;


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

		String sha512res = hashManager.getSHA512(sha512);
		long startSeconds = System.currentTimeMillis()/1000;

//		String sha512res = hashManager.proofOfWork512(sha512, 2);
//		String sha512res = String.format("%040x", new BigInteger(1, randomManager.getRandomHash512()));

		int N = 20;
		byte[] nativeArray = randomManager.getRandomNumbersNative();
		byte[] audioArray = randomManager.getRandomHash512();

		byte[] nativeHash = hashManager.getSHA512(nativeArray);

		for (int i = 1; i < N; i++) {
			byte[] native2 = randomManager.getRandomNumbersNative();
			nativeArray = ArrayUtils.addAll(nativeArray, native2);
			audioArray = ArrayUtils.addAll(audioArray, randomManager.getRandomHash512());
			nativeHash = ArrayUtils.addAll(nativeHash, hashManager.getSHA512(native2));
		}



		Double nativeHashEntropy = randomManager.countEntropy(nativeHash);

		Double nativeEntropy = randomManager.countEntropy(nativeArray);
		Double audioEntropy = randomManager.countEntropy(audioArray);

		String nativeRand = String.format("%040x", new BigInteger(1, nativeArray));
		String nativeRandHash = String.format("%040x", new BigInteger(1, nativeHash));
		String audioRand = String.format("%040x", new BigInteger(1, audioArray));

		double binNative = randomManager.binaryTest(nativeArray);
		double binAudio = randomManager.binaryTest(audioArray);
		double binHash = randomManager.binaryTest(nativeHash);

		long endSeconds = System.currentTimeMillis()/1000;

		Long totalTime = endSeconds - startSeconds;

		//String result = "Time: " + totalTime + " Input: " + sha512 + " Result: " + sha512res;

		//randomManager.startSound();
		DecimalFormat df = new DecimalFormat("#.################################################");
		DecimalFormat dfEn = new DecimalFormat("#.###############");
		String result = "Time: " + totalTime + " N = " + N +
				"<br> Native Entropy:      " + dfEn.format(nativeEntropy) + " bin: " + df.format(binNative) +
				"<br> Native Hash Entropy: " + dfEn.format(nativeHashEntropy) + " bin: " + df.format(binHash) +
				"<br> Audio Entropy:       " + dfEn.format(audioEntropy) + " bin: " + df.format(binAudio);

//		String result = "Time: " + totalTime +
//				"<br> Native Entropy: 		" + nativeEntropy + " Value: " + nativeRand +
//				"<br> Native Hash Entropy: 	" + nativeHashEntropy + " Value: " + nativeRandHash +
//				"<br> Audio Entropy: 		" + audioEntropy + " Value: " + audioRand;

		return new ModelAndView("main", "sha512res", result);
	}

	@RequestMapping("/secure/common/screenLayout.do")	
	public String screenWithLeftMenu(@RequestParam(value="screen", required=false) String p_screen,
			Model p_model) {
		p_model.addAttribute( "screen", p_screen );
		return "screenLayout";
	}

}
