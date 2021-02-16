package nl.tudelft.sem.sem54.mainservice.config;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import com.auth0.jwt.interfaces.RSAKeyProvider;

public class KeyProviderImp implements RSAKeyProvider {

    private transient KeyFactory kf;
    private transient String publicKey;

    /**
     * Provide the RSA keys from the publicKey and privateKey files.
     *
     * @param publicKey the location of the public key
     */
    public KeyProviderImp(String publicKey) {
        try {
            this.kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.publicKey = publicKey;
    }

    @Override
    public RSAPublicKey getPublicKeyById(String keyId) {
        try {

            byte[] keyBytes;

            URL pubResource = Thread.currentThread().getContextClassLoader()
                    .getResource(publicKey);

            if (pubResource == null) {
                keyBytes = Files.readAllBytes(
                        Paths.get(publicKey));

            } else {
                keyBytes = Files.readAllBytes(
                        Paths.get(pubResource.toString().split("file:")[1]));
            }


            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            return null;
        }

    }


    @Override
    public RSAPrivateKey getPrivateKey() {
        // The mainService does not use a private key, so return null
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
