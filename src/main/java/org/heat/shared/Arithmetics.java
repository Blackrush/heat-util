package org.heat.shared;

public final class Arithmetics {
    private Arithmetics() {}

    /**
     * Safely add two shorts.
     * @param shorts a non-null array
     * @return a short guaranteed to not overflow/underflow
     * @throws java.lang.ArithmeticException when addition overflows/underflows
     */
    public static short addShorts(short... shorts) {
        if (shorts.length <= 0) return 0;
        if (shorts.length == 1) return shorts[0];
        if (shorts.length == 2) return addShorts(shorts[0], shorts[1]);

        int result = shorts[0];
        for (int i = 1; i < shorts.length; i++) {
            result += shorts[i];

            if (result > Short.MAX_VALUE) {
                throw new ArithmeticException("addition overflows");
            }
            if (result < Short.MIN_VALUE) {
                throw new ArithmeticException("addition underflows");
            }
        }

        return (short) result;
    }

    public static short addShorts(short a, short b) {
        int result = a + b;
        if (result > Short.MAX_VALUE) {
            throw new ArithmeticException("addition overflows");
        }
        if (result < Short.MIN_VALUE) {
            throw new ArithmeticException("addition underflows");
        }
        return (short) result;
    }

    public static int addInts(int a, int b) {
        long result = (long) a + (long) b;
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("addition overflows");
        }
        if (result < Integer.MIN_VALUE) {
            throw new ArithmeticException("addition underflows");
        }
        return (int) result;
    }
}
