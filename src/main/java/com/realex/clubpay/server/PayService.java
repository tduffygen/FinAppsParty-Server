package com.realex.clubpay.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.realexpayments.hpp.sdk.RealexHpp;
import com.realexpayments.hpp.sdk.domain.HppRequest;
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

	public PayService() {
		amountLookup.put("moteId1", 1000l);
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
	 * @param moteId
	 * @param storeCard
	 * @return
	 */
	public String generateJsonRequest(String moteId, boolean storeCard) {

		HppRequest hppRequest = new HppRequest()
				.addMerchantId(RequestConstants.MERCHANT_ID)
				.addAmount(getAmountToCharge(moteId))
				.addAutoSettleFlag(true)
				.addCardStorageEnable(storeCard)
				.addCurrency(RequestConstants.CURRENCY)
				.addOfferSaveCard(true);

		return realexHpp.requestToJson(hppRequest);
	}

	/**
	 * Generate a receipt in.
	 * 
	 * @param phoneId
	 * @return
	 */
	public boolean generateReceiptInRequest(String phoneId, String moteId) {

		/*
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
		*/
		return false;
	}

	public long getAmountToCharge(String moteId) {
		return amountLookup.get(moteId);
	}
}
