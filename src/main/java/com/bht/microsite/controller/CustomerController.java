package com.bht.microsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bht.microsite.service.IAccountService;
import com.bht.microsite.vo.AccountOpenRequest;
import com.bht.microsite.vo.AccountOpenResponse;
import com.bht.vo.FGRequestContext;

/**
 * Created by shakeelosmani on 24/12/16.
 */

@Controller
public class CustomerController {

    @Autowired
    IAccountService acctService;


   /*@RequestMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model){
        model.addAttribute("product", productRepository.findOne(id));
        return "product";
    }*/

    @RequestMapping(value = "/customers",method = RequestMethod.GET)
    public String productsList(Model model){
        model.addAttribute("accounts", acctService.getCustomerDetails());
        return "customers";
    }

    @RequestMapping(value = "/open-secondary-account", method = RequestMethod.POST)
    @ResponseBody
    public String saveProduct(@RequestBody AccountOpenRequest accountOpenRequest) {
    	return acctService.openAccount(new FGRequestContext(), accountOpenRequest);
    			
    }

}
