package net.dkahn.starter.tools.cipher;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.Security;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Component
public class PasswordBasedEncoder {

    {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    private final SecureRandom random = new SecureRandom();

    private final PasswordBasedEncoderConfig config;

    public PasswordBasedEncoder(PasswordBasedEncoderConfig config) {
        this.config = config;
    }

    public byte [] encrypt(String passphrase, String plaintext) throws Exception {
        return encrypt(passphrase,plaintext.getBytes());
    }

    public byte [] encrypt(String passphrase, byte[] toEncode) throws Exception {
        SecretKey key = generateKey(passphrase);

        Cipher cipher = Cipher.getInstance(config.getCrypt());
        cipher.init(Cipher.ENCRYPT_MODE, key, generateIV(cipher), random);
        return cipher.doFinal(toEncode);
    }

    public String decryptAsString(String passphrase, byte [] toDecode) throws Exception {
        return new String(decrypt(passphrase,toDecode));
    }

    public byte[] decrypt(String passphrase, byte [] toDecode) throws Exception {
        SecretKey key = generateKey(passphrase);

        Cipher cipher = Cipher.getInstance(config.getCrypt());
        cipher.init(Cipher.DECRYPT_MODE, key, generateIV(cipher), random);
        return cipher.doFinal(toDecode);
    }

    private SecretKey generateKey(String passphrase) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), config.getSalt().getBytes(), config.getIterations(), config.getKeyLength());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(config.getSecret());
        return keyFactory.generateSecret(keySpec);
    }

    private IvParameterSpec generateIV(Cipher cipher) throws Exception {
        byte [] ivBytes = new byte[cipher.getBlockSize()];
        random.nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }
}
