package com.kanbanchallenge.kanbanchallengeservice.service;

import com.kanbanchallenge.kanbanchallengeservice.config.Configuration;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class Cipher {

    private final Configuration configuration;
    private final Charset chosenCharSet = StandardCharsets.US_ASCII;

    /**
     * This number ensures whe know where to start the start repeating the cipher since our cipher consists of
     * all ASCII characters.
     */
    private final int asciiNumberOfChars = 126;

    public Cipher(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @param value will consist of the contents of the entire file after it has been decoded.
     * @return converts the byte array back to a string so it can be stored in the file.
     *
     * Using a byte array as each element represents the characters number in the ASCII charset.
     * This natural sequence provides us with a means to increment each character by its position
     * in the ASCII charset.
     *
     * Should the incrementation put us out of bounds of the ASCII charset, we collect the remainder value after
     * dividing the length of the ASCII charset to place the converted value back in bounds.
     */
    public String encrypt(String value) {
        byte[] bytes = value.getBytes(chosenCharSet);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] += configuration.getShift();
            if (bytes[i] > asciiNumberOfChars) {
                bytes[i] = (byte) (bytes[i] % asciiNumberOfChars);
            }
        }
        return new String(bytes, chosenCharSet);
    }

    /**
     * The reverse of encryption.
     *
     * If our converted value should fall below zero, we will add this value to the ASCII charsets length
     * so we end up back on top of the cipher.
     */
    public String decrypt(String value) {
        byte[] bytes = value.getBytes(chosenCharSet);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] -= configuration.getShift();
            if (bytes[i] < 0) {
                bytes[i] = (byte) (asciiNumberOfChars + bytes[i]);
            }
        }
        return new String(bytes, chosenCharSet);
    }

    /**
     * Used to prevent errors and lost data when storing and retrieving the encrypted data.
     */
    public String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(chosenCharSet));
    }

    /**
     * Will undo encoding so the user can use the data.
     */
    public String decode(String value) {
        return new String(Base64.getDecoder().decode(value), chosenCharSet);
    }
}
