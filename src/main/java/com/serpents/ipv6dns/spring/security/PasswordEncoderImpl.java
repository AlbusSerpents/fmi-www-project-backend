package com.serpents.ipv6dns.spring.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.arraycopy;
import static java.nio.CharBuffer.wrap;
import static java.nio.charset.Charset.forName;
import static java.security.MessageDigest.getInstance;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    private static final Charset CHARSET = forName("UTF-8");

    @Override
    public String encode(final CharSequence password) {
        if (password == null) {
            return null;
        }
        try {
            final MessageDigest digest = getInstance("SHA-512");
            final byte[] bytes = encodeBytes(password);
            final byte[] digestedData = digest.digest(bytes);

            return new String(digestedData);
        } catch (NoSuchAlgorithmException | CharacterCodingException exception) {
            throw new IllegalArgumentException("Unable to encode password. Reason: " + exception.getMessage(), exception);
        }
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        if (rawPassword == null) {
            return false;
        }
        final String password = encode(rawPassword);
        return password.equals(encodedPassword);
    }

    private byte[] encodeBytes(final CharSequence sequence) throws CharacterCodingException {
        final CharBuffer buffer = wrap(sequence);
        final ByteBuffer stream = CHARSET.newEncoder().encode(buffer);
        final byte[] bytes = new byte[stream.limit()];

        arraycopy(stream.array(), 0, bytes, 0, bytes.length);
        return bytes;
    }

}
