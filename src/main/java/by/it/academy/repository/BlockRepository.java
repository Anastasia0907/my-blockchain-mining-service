package by.it.academy.repository;

import by.it.academy.pojo.Block;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlockRepository extends CrudRepository<Block, String> {

    @Query("select b from Block b WHERE b.timeStamp = (SELECT MAX(b.timeStamp) FROM Block b)")
    Block getLastMinedBlock();

    @Query("select count(*) from Block")
    int checkIfTableIsEmpty();
}
