package org.heat.shared;

import org.heat.shared.stream.Streams;

import java.util.Random;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Strings {

    public static final int PSEUDO_LEN = 7;
    public static final String VOWELS = "aeiouy";
    public static final String CONSONS = "bcdfghjklmnpqrstvwxz";
    public static final String DEFAULT_DICTIONARY = "abcdefghijklmnopqrstuvwxyz";

    private Strings() {}

    public static boolean isVowel(int c) {
        return VOWELS.indexOf(Character.toLowerCase(c)) >= 0;
    }

    public static boolean isConson(int c) {
        return CONSONS.indexOf(Character.toLowerCase(c)) >= 0;
    }

    public static Collector<Character, ?, String> join() {
        return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
    }

	public static Stream<Character> randomChars(Random random, String dictionary) {
		return random.ints()
					 .map(i -> (i > 0 ? i : -i) % dictionary.length())
				     .mapToObj(dictionary::charAt);
	}

	public static Stream<Character> randomChars(Random random) {
		return randomChars(random, DEFAULT_DICTIONARY);
	}

	public static Stream<Character> randomChars(String dictionary) {
		return randomChars(new Random(), dictionary);
	}

	public static Stream<Character> randomChars() {
		return randomChars(new Random(), DEFAULT_DICTIONARY);
	}

	public static String randomString(Random random, String dictionary, int len) {
		return randomChars(random, dictionary)
			  .limit(len)
              .collect(join());
	}

	public static String randomString(String dictionary, int len) {
		return randomString(new Random(), dictionary, len);
	}

	public static String randomString(Random random, int len) {
		return randomString(random, DEFAULT_DICTIONARY, len);
	}

	public static String randomString(int len) {
		return randomString(new Random(), DEFAULT_DICTIONARY, len);
	}

    public static Stream<String> split(String s, String sep) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<String>(Long.MAX_VALUE, 0) {
            String current = s;

            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                int index = current.indexOf(sep);
                if (index < 0) {
                    return false;
                }

                String part = current.substring(0, index);
                current = current.substring(index + sep.length());

                action.accept(part);

                return true;
            }
        }, false);
    }

    public static String toHexBytes(String data) {
        StringBuilder b = new StringBuilder(data.length() * 2);
        for (int i = 0; i < data.length(); i++) {
            String s = Integer.toHexString(data.charAt(i));
            if (s.length() < 2) b.append('0');
            b.append(s);
        }
        return b.toString();
    }

    public static String toHexBytes(byte[] xs) {
        StringBuilder res = new StringBuilder(xs.length * 2);
        for (byte x : xs) {
            String s = Integer.toHexString(x);
            if (s.length() < 2) {
                res.append('0');
            }
            if (s.length() > 2) {
                s = s.substring(s.length() - 2, s.length());
            }
            res.append(s);
        }
        return res.toString();
    }

	public static byte[] parseHexBytes(String hex) {
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}

		byte[] result = new byte[hex.length() / 2];
		for (int i = 0, j = 0; i < result.length; i++, j += 2) {
			result[i] = (byte) (Integer.parseInt(hex.substring(j, j + 2), 16) & 0xff);
		}

		return result;
	}

    public static <T> Function<T, String> startingWith(String s) {
        return x -> s + x;
    }

    public static <T> Function<T, String> endingWith(String s) {
        return x -> x + s;
    }

    public static <T> Function<T, String> surroundWith(String s) {
        return startingWith(s).compose(endingWith(s));
    }

    public static String titleize(String in) {
        char[] chars = in.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static String randomPseudo(Random rnd) {
        int nVowels = rnd.nextInt(2) + 2; // [2,3]
        int nConsons = rnd.nextInt(5) + 3; // [3,8]

        Stream<Character> vowels = randomChars(rnd, VOWELS).limit(nVowels);
        Stream<Character> consons = randomChars(rnd, CONSONS).limit(nConsons);

        Stream<Character> pseudo = rnd.nextBoolean()
                ? Streams.alternate(vowels.spliterator(), consons.spliterator())
                : Streams.alternate(consons.spliterator(), vowels.spliterator())
                ;

        return pseudo.collect(Collectors.collectingAndThen(join(), Strings::titleize));
    }
}
