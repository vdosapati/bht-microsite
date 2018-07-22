package com.bht.microsite.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionRequest {
	private List<String> accountNumbers;

}
