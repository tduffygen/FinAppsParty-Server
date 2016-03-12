package com.realex.clubpay.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.realexpayments.hpp.sdk.RealexHpp;
import com.realexpayments.hpp.sdk.domain.HppRequest;

@Service
public class PayService {

	private Map<String, CustomerVO> lookup = new HashMap<String, CustomerVO>();

	RealexHpp realexHpp;

	public PayService() {
		this.realexHpp = new RealexHpp(RequestConstants.SECRET);
	}

	/**
	 * Determine if it this user has their details stored already.
	 * 
	 * @param phoneId
	 * @return true if they have otherwise false
	 */
	public boolean detailsStored(String phoneId) {
		return true;
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

	public long getAmountToCharge(String moteId) {
		return 1000; // equals 10.00
	}
}
