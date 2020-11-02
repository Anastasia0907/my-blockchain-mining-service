package by.it.academy.pojo;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.security.PublicKey;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String transactionId;

    @Column(columnDefinition = "MEDIUMBLOB")
    private PublicKey senderPublicKey;

    @Column(columnDefinition = "MEDIUMBLOB")
    private PublicKey recipientPublicKey;

    private String senderPublicKeyString;

    private String recipientPublicKeyString;

    private double value;

    private String currency;

    private byte[] signature;

    @Column
    private byte transactionStatus;

    @ManyToOne
    @ToString.Exclude
    private Block block;

    private static int sequence = 0;

}
