/**
 * 
 */
package com.realex.clubpay.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Thomas
 *
 */

import org.springframework.web.bind.annotation.RestController;

import com.realexpayments.hpp.sdk.domain.HppRequest;

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

	@RequestMapping("/generateJsonRequest")
	public String generateJsonRequest(HppRequest hppRequest) {
		System.out.println("At generateJson endpoint.");
//		System.out.println("JsonResponse: " + jsonResponse);
		System.out.println("HppRequest: " + hppRequest);

		return payService.generateJsonRequest(hppRequest);
	}

	@RequestMapping("/validateJsonResponse")
//	public String validateJsonResponse(@RequestParam("jsonResponse") String jsonResponse) {
	public String validateJsonResponse(@RequestBody String requestString, HttpServletRequest req) throws UnsupportedEncodingException{
		System.out.println("At deocdeJsonResponse endpoint.");
		System.out.println("RequestString: " + requestString);

		String jsonResponse = URLDecoder.decode(requestString, "UTF-8");
		jsonResponse = jsonResponse.replace("hppResponse=", "");
		System.out.println("JsonResponse: " + jsonResponse);

		return (payService.validateJsonResponse(jsonResponse) ? "true" : "false");
	}

	@RequestMapping("/generateReceiptIn")
    public boolean generateRecieptIn(@RequestParam("phoneId") String phoneId,
    		@RequestParam("moteId") String moteId) {
    	System.out.println("At generateReceiptIn endpoint.");
    	System.out.println("PhoneID: " + phoneId);
    	System.out.println("MoteID: " + moteId);
    	
    	return payService.generateReceiptInRequest(phoneId, moteId);
    }
    

}