package by.it.academy.rest;

import by.it.academy.pojo.User;
import by.it.academy.service.MiningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiningController {

    private static final Logger logger = LoggerFactory.getLogger(MiningController.class);

    @Autowired
    MiningService miningService;

    @PostMapping(value = "/start-mining")
    public ResponseEntity startMining(
            @RequestBody User user
    ) throws Exception {
        logger.info("Calling startMining - POST");
        logger.info("Miner - user {}", user.getUserName());

        if(miningService.startMining(user)){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
