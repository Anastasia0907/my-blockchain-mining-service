package by.it.academy.service;

import by.it.academy.pojo.Transaction;
import by.it.academy.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<Transaction> getNotConfirmedTransactions() {
        return transactionRepository.findByTransactionStatus((byte) 0);
    }
}
