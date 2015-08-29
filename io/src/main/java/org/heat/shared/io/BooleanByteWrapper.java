package org.heat.shared.io;

public final class BooleanByteWrapper {
	private BooleanByteWrapper() {}

	public static int setFlag(int flag, boolean value, int offset) {
		if (offset >= 8)
			throw new IllegalArgumentException("offset must be >= 8");

		return value ? (byte) (flag | (1 << offset)) : (byte)(flag & 255 - (1 << offset));
	}

	public static boolean getFlag(int flag, int offset) {
		if (offset >= 8)
			throw new IllegalArgumentException("offset must be >= 8");

		return (flag & (byte) (1 << offset)) != 0;
	}
}
