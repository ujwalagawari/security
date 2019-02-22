/**
 * 
 */
package com.capgemini.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.capgemini.web.controller.BankAppController;
import com.capgemini.web.entity.CurrentDataSet;
import com.capgemini.web.entity.Transaction;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author ugawari
 *
 */
@Service
public class BankAppService {

	@Autowired
	private RestTemplate restTemplate;
	
	private static CurrentDataSet currentDataSet;
	
	@HystrixCommand(fallbackMethod = "depositeCallback")
	public String deposit(Transaction transaction, Model model) {
		//restTemplate.postForEntity("http://transaction-service/transactions", transaction, null);
		restTemplate.postForEntity("http://bank-zuul/banktransaction/transactions", transaction, null);
		model.addAttribute("message","Success!");
		return "DepositForm";
	}
	
	public String depositeCallback(Transaction transaction, Model model) {
		model.addAttribute("message", "Sorry, Try Again!");
		return "DepositForm";
	}
		
	@HystrixCommand(fallbackMethod = "fundTransferCallback")
	public String fundTransfer(int senderAccountNo,int receiverAccountNo,double amount, Model model) {
		Transaction senderTransaction = new Transaction(senderAccountNo, amount);
		restTemplate.postForEntity("http://bank-zuul/banktransaction/transactions/withdraw", senderTransaction, null);
		Transaction receiverTransaction = new Transaction(receiverAccountNo, amount);
		restTemplate.postForEntity("http://bank-zuul/banktransaction/transactions", receiverTransaction, null);
		model.addAttribute("message","Success!");
		return "fundTransfer";
	}
	
	public String fundTransferCallback(int senderAccountNo,int receiverAccountNo,double amount, Model model) {
		model.addAttribute("message", "Sorry, Try Again!");
		return "fundTransfer";
	}
		
	@HystrixCommand(fallbackMethod = "withdrawCallback")
	public String withdraw(Transaction transaction, Model model) {
		restTemplate.postForEntity("http://bank-zuul/banktransaction/transactions/withdraw", transaction, null);
		model.addAttribute("message","Success!");
		return "Withdraw";
	}
	
	public String withdrawCallback(Transaction transaction, Model model) {
		model.addAttribute("message", "Sorry, Try Again!");
		return "Withdraw";
	}
	
	@HystrixCommand(fallbackMethod = "getStatementCallback")
	public ModelAndView getStatement(int offset, int size) {
		int currentSize = size==0?5:size;
		int currentOffset = offset==0?1:offset;
		Link previous = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BankAppController.class).getStatement(currentOffset-currentSize, currentSize)).withRel("previous");
		Link next = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BankAppController.class).getStatement(currentOffset+currentSize, currentSize)).withRel("next");
		//CurrentDataSet currentDataSet = restTemplate.getForObject("http://transaction-service/transactions", CurrentDataSet.class);
		currentDataSet = restTemplate.getForObject("http://bank-zuul/banktransaction/transactions", CurrentDataSet.class);
		List<Transaction> transactionList = currentDataSet.getTransactions();
		List<Transaction> transactions = new ArrayList<Transaction>();
		for(int value=currentOffset-1; value<currentOffset+currentSize-1; value++) {
			if((transactionList.size() <= value && value > 0) || currentOffset < 1)
				break;
			Transaction transaction = transactionList.get(value);
			transactions.add(transaction);		
		}
		currentDataSet.setPreviousLink(previous);
		currentDataSet.setNextLink(next);
		currentDataSet.setTransactions(transactions);
		return new ModelAndView("DepositForm", "currentDataSet", currentDataSet);
	}
	
	public ModelAndView getStatementCallback(int offset, int size) {
		return new ModelAndView("DepositForm", "currentDataSet", currentDataSet);
	}
}
