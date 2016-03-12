/**
 * 
 */
package com.realex.clubpay.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Thomas
 *
 */

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Main {

	@Autowired
	PayService payService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/hello")
	public String hello() {
		return "Hello";
	}

	@RequestMapping("/payAction")
	public String pay(@RequestParam("phoneId") String phoneIdentifier) {
		System.out.println("At pay endpoint.");
		System.out.println("PhoneID: " + phoneIdentifier);

		if (payService.detailsStored(phoneIdentifier)) {
			return "true";
		}
		return "false";
	}

	@RequestMapping("/validateHppResponse")
	public String validate(@RequestParam("phoneId") String phoneId, @RequestParam("moteId") String moteId,
			@RequestParam("payerRef") String payerRef, @RequestParam("cardRef") String cardRef) {
		System.out.println("At validateHppResponse endpoint.");
		System.out.println("PhoneID: " + phoneId);
		System.out.println("MoteID: " + moteId);
		System.out.println("payerRef: " + payerRef);
		System.out.println("cardRef: " + cardRef);

		payService.addDetails(phoneId, payerRef, cardRef);

		return "true";
	}

	@RequestMapping("/generateJson")
	public String pay(@RequestParam("moteId") String moteId, @RequestParam("storeCard") boolean storeCard) {
		System.out.println("At generateJson endpoint.");
		System.out.println("MoteID: " + moteId);
		System.out.println("StoreCard: " + storeCard);

		return payService.generateJsonRequest(moteId, storeCard);
	}

}