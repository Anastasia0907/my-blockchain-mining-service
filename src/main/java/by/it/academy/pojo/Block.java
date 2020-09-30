package by.it.academy.pojo;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Block {

    @Id
    private String hash;

    private String previousHash;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    private long timeStamp; //as number of milliseconds since 1/1/1970

    private int nonce;

}