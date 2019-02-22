package com.capgemini.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.capgemini.web.entity.Transaction;
import com.capgemini.web.service.BankAppService;

@Controller
@EnableCircuitBreaker
public class BankAppController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private BankAppService bankAppService;
	
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value="/deposit", method=RequestMethod.GET)
	public String depositForm() {
		return "DepositForm";
	}
	
	@RequestMapping(value="/deposit", method=RequestMethod.POST)
	public String deposit(@ModelAttribute Transaction transaction, Model model) {
		return bankAppService.deposit(transaction, model);
	}
	
	@RequestMapping(value="/fundTransfer", method=RequestMethod.GET)
	public String fundTransferForm() {
		return "fundTransfer";
	}
	
	@RequestMapping(value="/fundTransfer", method=RequestMethod.POST)
	public String fundTransfer(@RequestParam("senderAccountNo") int senderAccountNo, @RequestParam("receiverAccountNo") int receiverAccountNo,
			@RequestParam("amount") double amount, Model model) {
		return bankAppService.fundTransfer(senderAccountNo, receiverAccountNo, amount, model);
	}
	
	@RequestMapping(value="/withdraw", method=RequestMethod.GET)
	public String withdrawForm() {
		return "Withdraw";
	}
	
	@RequestMapping(value="/withdraw", method=RequestMethod.POST)
	public String withdraw(@ModelAttribute Transaction transaction, Model model) {
		return bankAppService.withdraw(transaction, model);
	}
	
	@RequestMapping("/statement")
	public ModelAndView getStatement(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		return bankAppService.getStatement(offset, size);
	}
	
	
}
