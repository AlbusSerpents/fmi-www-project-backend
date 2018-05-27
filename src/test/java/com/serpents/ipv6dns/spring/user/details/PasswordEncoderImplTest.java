package com.serpents.ipv6dns.spring.user.details;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordEncoderImplTest {

    private final PasswordEncoderImpl encoder = new PasswordEncoderImpl();

    @Test
    public void encode_NullArgument() {
        final String actual = encoder.encode(null);
        assertNull(actual);
    }

    @Test
    public void encode() {
        final CharSequence password = "1234";
        final String actual = encoder.encode(password);

        final String expected = "d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db";
        assertEquals(expected, actual);
    }

    @Test
    public void matches_RawPasswordIsNull() {
        final boolean result = encoder.matches(null, "anything");
        assertFalse(result);
    }

    @Test
    public void matches_EncodedPasswordIsNull() {
        final boolean result = encoder.matches("anything", null);
        assertFalse(result);
    }

    @Test
    public void matches() {
        final String rawPassword = "1234";
        final String encodedPassword = "d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db";
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }
}