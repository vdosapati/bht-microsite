package com.bht.microsite.service;

import java.util.List;

import com.bht.microsite.vo.Account;
import com.bht.microsite.vo.AccountDetailRequest;
import com.bht.microsite.vo.AccountOpenRequest;
import com.bht.microsite.vo.AccountOpenResponse;
import com.bht.vo.FGRequestContext;

public interface IAccountService {

	String openAccount(FGRequestContext reqContext,AccountOpenRequest accountOpenRequest);
	List<Account> getCustomerDetails();

	
}