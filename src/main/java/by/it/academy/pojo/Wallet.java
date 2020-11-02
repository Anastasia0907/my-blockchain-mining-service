package by.it.academy.pojo;

import by.it.academy.utils.StringUtil;
import lombok.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String walletId;

    @Column(columnDefinition = "MEDIUMBLOB")
    private PrivateKey privateKey;

    @Column(columnDefinition = "MEDIUMBLOB")
    private PublicKey publicKey;

    private String privateKeyString;

    private String publicKeyString;

    @ManyToOne
    private User walletOwner;

    private String currency;

    private double balance;

    public Wallet(User user, String currency) {
        generateKeyPair();
        this.walletOwner = user;
        this.currency = currency;
        this.balance = 100;
    }

    private void generateKeyPair() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            this.setPrivateKey(keyPair.getPrivate());
            this.setPublicKey(keyPair.getPublic());
            this.setPrivateKeyString(StringUtil.getStringFromKey(getPrivateKey()));
            this.setPublicKeyString(StringUtil.getStringFromKey(getPublicKey()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId='" + walletId + '\'' +
                ", privateKey=" + privateKeyString +
                ", publicKey=" + publicKeyString +
                ", balance=" + balance +
                ", currency=" + currency +
                '}';
    }
}
