package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.User;
import by.it.academy.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MiningService {

    private static final Logger logger = LoggerFactory.getLogger(MiningService.class);
    private static final int difficulty = 1;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BlockService blockService;

    public boolean startMining(User user) throws Exception {
        logger.info("Calling startMining");

        if (blockService.checkIfTableIsEmpty()) {
            createGenesisBlock();
        }

        Block previousBlock = blockService.getLastMinedBlock();
        logger.info("Received previous mined block : {}", previousBlock);
        Block newBlock = Block.builder()
                .previousHash(previousBlock.getHash())
                .build();
        newBlock.setTransactions(new ArrayList<>());
        newBlock.setHash(calculateHash(newBlock));

        List<Transaction> notConfirmedTransactions = transactionService.getNotConfirmedTransactions();
        logger.info("List of not confirmed transactions: {}", notConfirmedTransactions);
        if (notConfirmedTransactions.size() > 9) {
            for (int i = 0; i < 10; i++) {
                addTransaction(newBlock, notConfirmedTransactions.get(i));
            }
        } else {
            for (Transaction i : notConfirmedTransactions) {
                addTransaction(newBlock, i);
            }
        }
        newBlock.setTimeStamp(new Date().getTime());

        mineBlock(newBlock, difficulty);
        blockService.create(newBlock);

        return true;
    }

    public boolean addTransaction(Block block, Transaction notConfirmedTransaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (notConfirmedTransaction == null) return false;

        if ((!block.getPreviousHash().equals("0"))) {
            logger.info("Verifying transaction : {}", notConfirmedTransaction);
            if ((!verifySignature(notConfirmedTransaction))) {
                logger.info("Transaction failed to process. Discarded.");
                return false;
            }
        }
        notConfirmedTransaction.setTransactionStatus((byte) 1);
        notConfirmedTransaction.setBlock(block);
        block.getTransactions().add(notConfirmedTransaction);
        logger.info("Transaction Successfully added to Block");
        return true;
    }

    private boolean verifySignature(Transaction transaction) {
        logger.info("Inside verifySignature");
        if (transaction.getSenderPublicKey() == null) {
            return true;
        }
        String data = transaction.getSenderPublicKeyString()
                + transaction.getRecipientPublicKeyString()
                + transaction.getValue();
        logger.info("data = {}", data);
        return StringUtil.verifyECDSASig(transaction.getSenderPublicKey(), data, transaction.getSignature());
    }

    private static void mineBlock(Block block, int difficulty) {
        logger.info("Inside mineBlock()");
        String target = new String(new char[difficulty]).replace('\0', '0');
        logger.info("target = {}", target);
        int nonce = block.getNonce();
        logger.info("nonce = {}", nonce);
        logger.info("Started mining");
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            nonce++;
            logger.info("nonce = {}", nonce);
            block.setHash(calculateHash(block));
            logger.info("new hash = {}", block.getHash());
        }
        block.setNonce(nonce);
        logger.info("Block Mined! Hash : {}", block.getHash());
    }

    public static String calculateHash(Block block) {
        return StringUtil
                .applySha256(block.getPreviousHash() +
                        block.getTimeStamp() +
                        block.getNonce() +
                        block.getTransactions().toString()
                );
    }

    public void createGenesisBlock() {
        logger.info("Calling createGenesisBlock");
        Block genesisBlock = Block.builder()
                .previousHash("0")
                .timeStamp(new Date().getTime())
                .transactions(new ArrayList<>())
                .build();
        genesisBlock.setHash(calculateHash(genesisBlock));
        logger.info("Created genesis block, id = {}", genesisBlock.getHash());
        blockService.create(genesisBlock);
        logger.info("Saved genesis block");
    }

    //сверяет хэши блоков
//    private Boolean isChainValid() {
//        Block currentBlock;
//        Block previousBlock;
//        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
//
//        //loop through blockchain to check hashes:
//        for (int i = 1; i < blockchain.size(); i++) {
//            currentBlock = blockchain.get(i);
//            previousBlock = blockchain.get(i - 1);
//
//            //compare registered hash and calculated hash:
//            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
//                System.out.println("Current Hashes not equal");
//                return false;
//            }
//            //compare previous hash and registered previous hash
//            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
//                System.out.println("Previous Hashes not equal");
//                return false;
//            }
//            //check if hash is solved
//            if (!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
//                System.out.println("This block hasn't been mined");
//                return false;
//            }
//
//            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
//                Transaction currentTransaction = currentBlock.getTransactions().get(t);
//
//                if (!transactionService.verifySignature(currentTransaction)) {
//                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
//                    return false;
//                }
//            }
//        }
//        System.out.println("Blockchain is valid");
//
//        return true;
//    }

}
