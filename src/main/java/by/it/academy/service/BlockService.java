package by.it.academy.service;

import by.it.academy.pojo.Block;
import by.it.academy.repository.BlockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockService {

    private static final Logger logger = LoggerFactory.getLogger(BlockService.class);

    @Autowired
    BlockRepository blockRepository;

    public Block getLastMinedBlock() {
        return blockRepository.getLastMinedBlock();
    }

    public void create(Block newBlock) {
        blockRepository.save(newBlock);
    }

    public boolean checkIfTableIsEmpty() {
        return blockRepository.checkIfTableIsEmpty() == 0;
    }

}
