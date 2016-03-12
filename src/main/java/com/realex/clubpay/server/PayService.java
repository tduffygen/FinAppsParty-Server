package com.realex.clubpay.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.realexpayments.hpp.sdk.RealexHpp;
import com.realexpayments.hpp.sdk.domain.HppRequest;
import com.realexpayments.hpp.sdk.domain.HppResponse;
import com.realexpayments.remote.sdk.RealexClient;
import com.realexpayments.remote.sdk.domain.payment.PaymentRequest;
import com.realexpayments.remote.sdk.domain.payment.PaymentRequest.PaymentType;
import com.realexpayments.remote.sdk.domain.payment.PaymentResponse;

@Service
public class PayService {

	RealexHpp realexHpp;
	RealexClient realexClient;

	private Map<String, CustomerVO> customerLookup = new HashMap<String, CustomerVO>();

	private Map<String, Long> amountLookup = new HashMap<String, Long>();

	private String ourMoteId = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
	private String hardCodedPhoneId = "351533063999440";
	
	public PayService() {
		amountLookup.put(ourMoteId, 1000l);
		amountLookup.put("moteId2", 2000l);
		amountLookup.put("moteId3", 3000l);
		this.realexHpp = new RealexHpp(RequestConstants.SECRET);
		this.realexClient = new RealexClient(RequestConstants.SECRET);
	}

	/**
	 * Determine if it this user has their details stored already.
	 * 
	 * @param phoneId
	 * @return true if they have otherwise false
	 */
	public boolean detailsStored(String phoneId) {
		return customerLookup.get(phoneId) != null;
	}

	public void addDetails(String phoneId, String payerRef, String cardRef) {
		CustomerVO customer = new CustomerVO();
		customer.setPayerRef(payerRef);
		customer.setCardRef(cardRef);
		customerLookup.put(phoneId, customer);
	}

	/**
	 * Generate the JSON request required for getting a HPP page.
	 * 
	 * @return
	 */
	public String generateJsonRequest(HppRequest hppRequest) {

		/*String moteId = hppRequest.getCommentOne();
		String phoneId = hppRequest.getCommentTwo();
		System.out.println("moteId: " + moteId);
		System.out.println("phoneId: " + phoneId);
		System.out.println(hppRequest.getMerchantId());*/
		
		hppRequest
				.addMerchantId(RequestConstants.MERCHANT_ID)
				.addAmount(getAmountToCharge(ourMoteId))
				.addAutoSettleFlag(true)
				.addCardStorageEnable(true)
				.addCurrency(RequestConstants.CURRENCY)
				.addPayerReference(hardCodedPhoneId)
				.addPayerExists(false)
				.addOfferSaveCard(true);

		return realexHpp.requestToJson(hppRequest);
	}

	/**
	 * Decode and validate json response and see if the transaction was successful or not.
	 * 
	 * @param jsonResponse
	 * @return
	 */
	public HppResponse validateJsonResponse(String jsonResponse) {

		HppResponse hppResponse = realexHpp.responseFromJson(jsonResponse);
		//addDetails(hardCodedPhoneId, hardCodedPhoneId, "cardId");
		return hppResponse;
	}

	/**
	 * Generate a receipt in.
	 * 
	 * @param phoneId
	 * @return
	 */
	public boolean generateReceiptInRequest(String phoneId, String moteId) {

		CustomerVO customer = customerLookup.get(phoneId);
		String payerRef = customer.getPayerRef();
		String cardRef = customer.getCardRef();

		PaymentRequest paymentRequest = new PaymentRequest()
				.addMerchantId(RequestConstants.MERCHANT_ID)
				.addType(PaymentType.RECEIPT_IN)
				.addAmount(getAmountToCharge(moteId))
				.addCurrency(RequestConstants.CURRENCY)
				.addPayerReference(payerRef)
				.addPaymentMethod(cardRef);
		
		PaymentResponse response = realexClient.send(paymentRequest);
		if ( response.getAuthCode().equals("00")) {
			return true;
		}
		return false;
	}

	public long getAmountToCharge(String moteId) {
		return amountLookup.get(moteId);
	}
}
