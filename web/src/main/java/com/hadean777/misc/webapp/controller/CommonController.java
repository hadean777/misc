package com.hadean777.misc.webapp.controller;

import com.hadean777.misc.AppConstants;
import com.hadean777.misc.manager.HashManager;
import com.hadean777.misc.manager.RandomManager;
import com.hadean777.misc.service.AudioService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Controller
public class CommonController {

	@Autowired
	@Qualifier(AppConstants.MANAGER_BEAN_HASH_MANAGER)
	private HashManager hashManager;

	@Autowired
	@Qualifier(AppConstants.MANAGER_BEAN_RANDOM_MANAGER)
	private RandomManager randomManager;

	@Autowired
	@Qualifier(AppConstants.MANAGER_BEAN_AUDIO_SERVICE)
	private AudioService audioService;


	@RequestMapping("/common/loginPage.do")
	public String goToLoginPage() {
		return "login";
	}

	@RequestMapping(value = "/common/testSound.do", method = RequestMethod.GET)
	public String testSound() {
		audioService.sampleSound();
		return "main";
	}

	@RequestMapping(value = "/common/mainPage.do", method = RequestMethod.GET)
	public String goToMainPage() {
		return "main";
	}

	@RequestMapping(value = "/common/mainPage.do", method = RequestMethod.POST)
	public ModelAndView getMainPageWithResults(@RequestParam String sha512) {

		byte[] sourceText = null;
		Double sourceEntropy = 0.0;
		Double binSource = 0.0;

		if (StringUtils.isNotBlank(sha512)) {
			sourceText = sha512.getBytes();
			sourceEntropy = randomManager.countEntropy(sourceText);
			binSource = randomManager.binaryTest(sourceText);
		}

		//String sha512res = hashManager.getSHA512(sha512);
		long startSeconds = System.currentTimeMillis()/1000;

//		String sha512res = hashManager.proofOfWork512(sha512, 2);
//		String sha512res = String.format("%040x", new BigInteger(1, randomManager.getRandomHash512()));

		int x = 16;
		int y = 16;

		int N = x*y/64;
		byte[] nativeArray = randomManager.getRandomNumbersNative();
		byte[] audioArray = randomManager.getAudioBytes();

		byte[] nativeHash = hashManager.getSHA512(nativeArray);
		byte[] audioHash = randomManager.getRandomHash512();

		for (int i = 1; i < N; i++) {
			byte[] native2 = randomManager.getRandomNumbersNative();
			nativeArray = ArrayUtils.addAll(nativeArray, native2);
			audioArray = ArrayUtils.addAll(audioArray, randomManager.getAudioBytes());
			nativeHash = ArrayUtils.addAll(nativeHash, hashManager.getSHA512(native2));
			audioHash = ArrayUtils.addAll(audioArray, randomManager.getRandomHash512());
		}

//		String nativeImage = Base64.getEncoder().encodeToString(nativeArray);

		Double nativeHashEntropy = randomManager.countEntropy(nativeHash);
		Double audioHashEntropy = randomManager.countEntropy(audioHash);

		Double nativeEntropy = randomManager.countEntropy(nativeArray);
		Double audioEntropy = randomManager.countEntropy(audioArray);

		String nativeRand = String.format("%040x", new BigInteger(1, nativeArray));
		//String nativeRandHash = String.format("%040x", new BigInteger(1, nativeHash));
//		String audioRand = String.format("%040x", new BigInteger(1, audioArray));

		double binNative = randomManager.binaryTest(nativeArray);
		double binAudio = randomManager.binaryTest(audioArray);
		double binHash = randomManager.binaryTest(nativeHash);
		double binAudioHash = randomManager.binaryTest(audioHash);

		long endSeconds = System.currentTimeMillis()/1000;

		Long totalTime = endSeconds - startSeconds;

		//String result = "Time: " + totalTime + " Input: " + sha512 + " Result: " + sha512res;

		//randomManager.startSound();
		DecimalFormat df = new DecimalFormat("#.################################################");
		DecimalFormat dfEn = new DecimalFormat("#.###############");
		String resultStr = "Time: " + totalTime + " N = " + N +
				"<br> Native Entropy:      " + dfEn.format(nativeEntropy) + " bin: " + df.format(binNative) +
				"<br> Native Hash Entropy: " + dfEn.format(nativeHashEntropy) + " bin: " + df.format(binHash) +
				"<br> Audio Entropy:       " + dfEn.format(audioEntropy) + " bin: " + df.format(binAudio) +
				"<br> Audio Hash Entropy:  " + dfEn.format(audioHashEntropy) + " bin: " + df.format(binAudioHash) +
				"<br> Source Entropy:      " + dfEn.format(sourceEntropy) + " bin: " + df.format(binSource) +
				"<br> <br> <br> Text: " + sha512;

//		String result = "Time: " + totalTime +
//				"<br> Native Entropy: 		" + nativeEntropy + " Value: " + nativeRand +
//				"<br> Native Hash Entropy: 	" + nativeHashEntropy + " Value: " + nativeRandHash +
//				"<br> Audio Entropy: 		" + audioEntropy + " Value: " + audioRand;

		Map<String, Object> model = new HashMap<>();
		model.put("sha512res", resultStr);
//		model.put("nativeImg", nativeImage);
		//audioService.sampleSound();
		//return new ModelAndView("main", "sha512res", resultStr);
		return new ModelAndView("main", model);
	}

	@RequestMapping("/secure/common/screenLayout.do")	
	public String screenWithLeftMenu(@RequestParam(value="screen", required=false) String p_screen,
			Model p_model) {
		p_model.addAttribute( "screen", p_screen );
		return "screenLayout";
	}

}
