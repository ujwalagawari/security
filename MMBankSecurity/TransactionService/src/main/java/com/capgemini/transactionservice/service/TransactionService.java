package com.capgemini.transactionservice.service;

import java.util.List;

import com.capgemini.transactionservice.app.entity.Transaction;

public interface TransactionService {

	//double saveTransaction(int accountNumber, double amount, double currentBalance, String transactionType);

	List<Transaction> listOfTransactions();

	double deposit(Integer accountNumber, Double amount, double currentBalance, String deposit);

	double withDraw(Integer accountNumber, Double amount, double currentBalance, String withdraw);

}
