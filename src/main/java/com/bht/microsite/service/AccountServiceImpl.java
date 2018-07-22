package com.bht.microsite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bht.gateway.ms.util.GatewayUtil;
import com.bht.gw.exception.MicrositeException;
import com.bht.microsite.vo.Account;
import com.bht.microsite.vo.AccountDetailRequest;
import com.bht.microsite.vo.AccountOpenRequest;
import com.bht.microsite.vo.AccountOpenResponse;
import com.bht.microsite.vo.AccountTransactionDetails;
import com.bht.microsite.vo.Transaction;
import com.bht.microsite.vo.TransactionRequest;
import com.bht.vo.FGRequestContext;
import com.bht.vo.ServiceResponse;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private GatewayUtil util;

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public String openAccount(FGRequestContext reqContext, AccountOpenRequest accountOpenRequest) {
		String secondaryAccount = null;
		AccountOpenResponse accountResponse = null;
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", util.createJwtToken());
		headers.set("Content-Type", "application/json");
		HttpEntity reEntiry = new HttpEntity(accountOpenRequest, headers);
		ResponseEntity<ServiceResponse<AccountOpenResponse>> accountOpenResponse = template.exchange(
				env.getProperty("gw.open.account"), HttpMethod.POST, reEntiry,
				new ParameterizedTypeReference<ServiceResponse<AccountOpenResponse>>() {

				});
		if (accountOpenResponse.getBody().getErrorInfo().getErrorCode().equals("00")) {
			accountResponse = accountOpenResponse.getBody().getData();
			if (accountOpenRequest.getIntialCredit() != 0 && null != accountResponse.getAccountDetailResponse().getAccountDetails()) {
				for(Account acct:accountResponse.getAccountDetailResponse().getAccountDetails()){
					if(acct.getAccountType().equals("secondary")){
						secondaryAccount = acct.getAccountNumber();
						break;
					}
				}
			}
		}else{
			secondaryAccount ="Customer already has secondary account";
		}
				
		return secondaryAccount;
	}

	@Override
	public List<Account> getCustomerDetails() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", util.createJwtToken());
		headers.set("Content-Type", "application/json");
		HttpEntity reEntiry = new HttpEntity(headers);
		ResponseEntity<ServiceResponse<AccountOpenResponse>> accountOpenResponse = template.exchange(
				env.getProperty("gw.details"), HttpMethod.GET, reEntiry,
				new ParameterizedTypeReference<ServiceResponse<AccountOpenResponse>>() {

				});
		List<Account> accounts = new ArrayList<>();
		 Map<String, List<Account>> mapAccounts = accountOpenResponse.getBody().getData().getAccountDetailResponse().getAccounts();
		 
		 for(String custId: mapAccounts.keySet()){
			 List<Account> custAccounts = mapAccounts.get(custId);
			 for(Account acct :custAccounts ){
				 acct.setCustId(custId);
			 }
			 accounts.addAll(custAccounts);
		 }
		List<AccountTransactionDetails> totalAccountTransactions=accountOpenResponse.getBody().getData().getTransactionsDetailResponse().getTotalAccountTransactions();
		Map<String, AccountTransactionDetails> trnsdetails = new HashMap<>();
		
		for(AccountTransactionDetails acctTrnsdetail: totalAccountTransactions){
			trnsdetails.put(acctTrnsdetail.getAccountNumber(),acctTrnsdetail);
			
		}
		
		for(Account acct: accounts){
			AccountTransactionDetails tnsdetails = trnsdetails.get(acct.getAccountNumber());
			acct.setTotalamount(tnsdetails.getTotalAmount());
			acct.setTotalTransactions(tnsdetails.getTransactions().size()); 
		}
		
		return accounts;
	}

}
