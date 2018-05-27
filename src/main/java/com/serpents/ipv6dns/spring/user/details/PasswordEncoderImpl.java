package com.serpents.ipv6dns.spring.user.details;

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
class PasswordEncoderImpl implements PasswordEncoder {

    private static final Charset UTF_8 = forName("UTF-8");
    private static final char[] HEX_SYMBOLS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    @Override
    public String encode(CharSequence password) {
        if (password == null) {
            return null;
        }
        try {
            final MessageDigest digest = getInstance("SHA-512");
            final byte[] encodedBytes = toUtf8Bytes(password);
            final byte[] digestedData = digest.digest(encodedBytes);
            final char[] encodedPassword = toChars(digestedData);
            return new String(encodedPassword);
        } catch (NoSuchAlgorithmException | CharacterCodingException e) {
            throw new IllegalArgumentException("Unable to encode password. Reason: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        final String password = encode(rawPassword);
        return password.equals(encodedPassword);
    }

    private byte[] toUtf8Bytes(CharSequence sequence) throws CharacterCodingException {
        final CharBuffer buffer = wrap(sequence);
        final ByteBuffer bytes = UTF_8.newEncoder().encode(buffer);
        final byte[] bytesCopy = new byte[bytes.limit()];
        arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());

        return bytesCopy;
    }

    private static char[] toChars(byte[] bytes) {
        final int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];

        int j = 0;
        for (byte aByte : bytes) {
            result[j++] = HEX_SYMBOLS[(0xF0 & aByte) >>> 4];
            result[j++] = HEX_SYMBOLS[(0x0F & aByte)];
        }

        return result;
    }

}
