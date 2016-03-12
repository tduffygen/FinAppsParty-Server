package com.realex.clubpay.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.realexpayments.hpp.sdk.RealexHpp;
import com.realexpayments.hpp.sdk.domain.HppRequest;

@Service
public class PayService {

	RealexHpp realexHpp;

	private Map<String, CustomerVO> customerLookup = new HashMap<String, CustomerVO>();

	private Map<String, Long> amountLookup = new HashMap<String, Long>();

	public PayService() {
		amountLookup.put("moteId1", 1000l);
		amountLookup.put("moteId2", 2000l);
		amountLookup.put("moteId3", 3000l);
		this.realexHpp = new RealexHpp(RequestConstants.SECRET);
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

		HppRequest hppRequest = new HppRequest();
		hppRequest.setMerchantId(RequestConstants.MERCHANT_ID);
		hppRequest.addAmount(getAmountToCharge(moteId));
		hppRequest.addAutoSettleFlag(true);
		hppRequest.addCardStorageEnable(storeCard);
		hppRequest.addCurrency(RequestConstants.CURRENCY);
		hppRequest.addOfferSaveCard(true);

		realexHpp.requestToJson(hppRequest);
		return null;
	}

	/**
	 * Generate a receipt in.
	 * 
	 * @param phoneId
	 * @return
	 */
	public String generateReceiptInRequest(String phoneId) {

		return null;
	}

	public long getAmountToCharge(String moteId) {
		return amountLookup.get(moteId);
	}
}
