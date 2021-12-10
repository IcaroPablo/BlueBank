package com.bluebank.project.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bluebank.project.dtos.DepositDTO;
import com.bluebank.project.dtos.LoanDTO;
import com.bluebank.project.dtos.TransactionDTO;
import com.bluebank.project.dtos.WithdrawDTO;
import com.bluebank.project.dtos.TransferenceDTO;
import com.bluebank.project.models.Loan;
import com.bluebank.project.models.Transaction;

@Service
public class TransactionMapper {

	public List <TransactionDTO> updateDtoFromTransacoes(List <Transaction> transactions) {
		List <TransactionDTO> transactionDTO = new ArrayList<>();
		for (Transaction transaction : transactions) {
			switch (transaction.getTransactionType()) {
			case DEP:
				DepositDTO depositDTO = new DepositDTO();
				updateDepositDtoFromTransaction(transaction, depositDTO);
				transactionDTO.add(depositDTO);
				break;
			case SAQ:
				WithdrawDTO withdrawDTO = new WithdrawDTO();
				updateWithdrawDtoFromTransaction(transaction, withdrawDTO);
				transactionDTO.add(withdrawDTO);
				break;
			case TRA:
				TransferenceDTO transferenceDTO = new TransferenceDTO();
				updateTransferenceDtoFromTransaction(transaction, transferenceDTO);
				transactionDTO.add(transferenceDTO);
				break;
			}		
		}
		return transactionDTO;
	}	
	
	public DepositDTO updateDepositDtoFromTransaction(Transaction transaction, DepositDTO depositDTO) {
		depositDTO.setTransactionDate(transaction.getTransactionDate().toString());
		depositDTO.setTransactionType("DEPÓSITO");
		depositDTO.setPreviousBalance(transaction.getPreviousBalance());
		depositDTO.setCurrentBalance(transaction.getCurrentBalance());
		depositDTO.setDepositValue(transaction.getValue());
		return depositDTO;
	}

	public LoanDTO updateLoanDtoFromLoan(Loan loan, LoanDTO loanDTO) {
		loanDTO.setContractNumber(loan.getId());
		loanDTO.setStartDate(loan.getStartDate().toString());
		loanDTO.setEndDate(loan.getEndDate().toString());
		loanDTO.setBorrowedAmount(loan.getBorrowedAmount());
		loanDTO.setFees(loan.getFees());
		loanDTO.setInstallments(loan.getInstallments());
		return loanDTO;
	}
	
	public WithdrawDTO updateWithdrawDtoFromTransaction(Transaction transaction, WithdrawDTO withdrawDTO) {
		withdrawDTO.setTransactionType("SAQUE");
		withdrawDTO.setTransactionDate(transaction.getTransactionDate().toString());
		withdrawDTO.setPreviousBalance(transaction.getPreviousBalance());
		withdrawDTO.setCurrentBalance(transaction.getCurrentBalance());
		withdrawDTO.setWithdrawValue(transaction.getValue());
		return withdrawDTO;
	}

	public TransferenceDTO updateTransferenceDtoFromTransaction(Transaction transaction, TransferenceDTO transferenceDTO) {
		transferenceDTO.setAccount(transaction.getAccount().getId());
		transferenceDTO.setTransactionType("TRANSFERÊNCIA");
		transferenceDTO.setTransactionDate(transaction.getTransactionDate().toString());
		transferenceDTO.setPreviousBalance(transaction.getPreviousBalance());
		transferenceDTO.setCurrentBalance(transaction.getCurrentBalance());
		transferenceDTO.setDestinationAccount(transaction.getDestinationAccount().getId());
//		transferenceDTO.setScheduledDate(transaction.getDataAgendTransacao().toString());
//		transferenceDTO.setDataExecTransacao(transaction.getDataExecTransacao().toString());
		transferenceDTO.setValue(transaction.getValue());
		return transferenceDTO;
	}

}
