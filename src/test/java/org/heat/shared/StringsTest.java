package org.heat.shared;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StringsTest {
	@Test
	public void testParseEvenHexBytes() throws Exception {
		final String hex = "0123456789abcdef";

		final byte[] bytes = Strings.parseHexBytes(hex);

		assertThat(bytes.length, is(8));
		assertThat(bytes[0], is((byte) 0x01));
		assertThat(bytes[1], is((byte) 0x23));
		assertThat(bytes[2], is((byte) 0x45));
		assertThat(bytes[3], is((byte) 0x67));
		assertThat(bytes[4], is((byte) 0x89));
		assertThat(bytes[5], is((byte) 0xab));
		assertThat(bytes[6], is((byte) 0xcd));
		assertThat(bytes[7], is((byte) 0xef));
	}

    @Test
    public void testToHexBytes() throws Exception {
        final byte[] input = new byte[]{1, 2, 3};

        final String hex = Strings.toHexBytes(input);

        assertThat(hex.length(), is(6));
        assertThat(hex.substring(0, 2), is("01"));
        assertThat(hex.substring(2, 4), is("02"));
        assertThat(hex.substring(4, 6), is("03"));
    }

    @Test
	public void testParseHexBytes() throws Exception {
		final String hex = "123456789abcdef";

		final byte[] bytes = Strings.parseHexBytes(hex);

		assertThat(bytes.length, is(8));
		assertThat(bytes[0], is((byte) 0x01));
		assertThat(bytes[1], is((byte) 0x23));
		assertThat(bytes[2], is((byte) 0x45));
		assertThat(bytes[3], is((byte) 0x67));
		assertThat(bytes[4], is((byte) 0x89));
		assertThat(bytes[5], is((byte) 0xab));
		assertThat(bytes[6], is((byte) 0xcd));
		assertThat(bytes[7], is((byte) 0xef));
	}

    @Test
    public void testRandomPseudo() throws Exception {
        // given
        Random random = new Random();

        // when
        String pseudo = Strings.randomPseudo(random);

        // then
        assertTrue("minimum 3 length", pseudo.length() >= 3);
        assertTrue("titleized", Character.isUpperCase(pseudo.charAt(0)));

        int nVowels = 0, nConsons = 0;
        for (int i = 0; i < pseudo.length(); i++) {
            char c = pseudo.charAt(i);
            if (Strings.isVowel(c)) {
                nVowels++;
            } else if (Strings.isConson(c)) {
                nConsons++;
            } else {
                fail();
            }
        }

        assertTrue("minimum 1 vowel", nVowels >= 1);
        assertTrue("minimum 2 consons", nConsons >= 2);
    }

    @Ignore
    @Test
    public void printRandomPseudos() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(Strings.randomPseudo(random));
        }
    }
}
