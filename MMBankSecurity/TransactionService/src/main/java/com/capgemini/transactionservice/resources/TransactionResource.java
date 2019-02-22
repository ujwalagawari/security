package com.capgemini.transactionservice.resources;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.capgemini.transactionservice.app.entity.CurrentDataSet;
import com.capgemini.transactionservice.app.entity.Transaction;
import com.capgemini.transactionservice.app.entity.TransactionType;
import com.capgemini.transactionservice.service.TransactionService;

/**
 * @author ugawari
 *
 */
@RestController
@RequestMapping("/transactions")
public class TransactionResource {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	TransactionService service;
	
	/*
	 * @GetMapping public String s() { return "Ujwala"; }
	 */
	
	
	@PostMapping
	public ResponseEntity<Transaction> deposit(@RequestBody Transaction transaction){
		ResponseEntity<Double> balance = restTemplate.getForEntity("http://account-service/accounts/"+transaction.getAccountNumber()+"/balance", Double.class);
		  double currentBalance = balance.getBody();
		  double updatedBalance = service.deposit(transaction.getAccountNumber(), transaction.getAmount(), currentBalance, TransactionType.DEPOSIT);
		restTemplate.put("http://account-service/accounts/"+transaction.getAccountNumber()+"?currentBalance="+updatedBalance, null);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<Transaction> withDraw(@RequestBody Transaction transaction){
		ResponseEntity<Double> balance = restTemplate.getForEntity("http://account-service/accounts/"+transaction.getAccountNumber()+"/balance", Double.class);
		  double currentBalance = balance.getBody();
		  double updatedBalance =service.withDraw(transaction.getAccountNumber(), transaction.getAmount(), currentBalance, TransactionType.WITHDRAW);
		 restTemplate.put("http://account-service/accounts/"+transaction.getAccountNumber()+"?currentBalance="+updatedBalance, null);
		 return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/*
	 * @PostMapping public ResponseEntity<Transaction> deposit(@RequestParam int
	 * accountNumber, @RequestParam double amount){ ResponseEntity<Double> balance =
	 * restTemplate.getForEntity("http://localhost:8080/accounts/"+accountNumber+
	 * "/balance", Double.class); double currentBalance = balance.getBody();
	 * currentBalance=currentBalance+amount;
	 * service.deposit(accountNumber,amount,currentBalance);
	 * restTemplate.getForEntity("http://localhost:8080/accounts/"+accountNumber+
	 * "?currentBalance="+currentBalance, null); return null; }
	 */
	
	
	@GetMapping
	public ResponseEntity<CurrentDataSet> listOfTransactions(){
		CurrentDataSet currentDataSet =new CurrentDataSet();
		List<Transaction> transactions = service.listOfTransactions();
		currentDataSet.setTransactions(transactions);
		return new ResponseEntity<>(currentDataSet, HttpStatus.OK);
	}
}
