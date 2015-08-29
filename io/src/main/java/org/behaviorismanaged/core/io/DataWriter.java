package org.behaviorismanaged.core.io;

import java.math.BigInteger;

public abstract class DataWriter {
	public abstract void writeBytes(byte[] bytes);

	public abstract void writeInt8(byte int8);
	public abstract void writeUInt8(short uint8);
	public abstract void writeInt16(short int16);
	public abstract void writeUInt16(int uint16);
	public abstract void writeInt32(int int32);
	public abstract void writeUInt32(long uint32);
	public abstract void writeInt64(long int64);
	public abstract void writeUInt64(BigInteger uint64);

	public void writeFloat(float f) {
		writeInt32(Float.floatToIntBits(f));
	}

	public void writeDouble(double d) {
		writeInt64(Double.doubleToLongBits(d));
	}

	public void writeBoolean(boolean b) {
		writeInt8((byte) (b ? 1 : 0));
	}

	public void writeChar(char c) {
		writeUInt8((short) c);
	}

	public void writeUTF(String utf) {
		writeUInt16(utf.length());
		writeUTFBytes(utf);
	}

	public void writeUTFBytes(String utf) {
		for (int i = 0; i < utf.length(); i++) {
			writeChar(utf.charAt(i));
		}
	}

    public abstract int getRemaining();
	public abstract int getPosition();
	public abstract void setPosition(int position);
	public abstract void slice(int offset, int length);

	public int movePosition(int length) {
		int current = getPosition();
		setPosition(current + length);
		return getPosition();
	}

    public abstract DataReader reader();
}
