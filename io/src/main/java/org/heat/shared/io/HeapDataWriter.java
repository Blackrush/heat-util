package org.heat.shared.io;

import java.math.BigInteger;

public final class HeapDataWriter extends LegacyWriter {
	private final byte[] buf;
	private int baseIndex, length, index;

	public HeapDataWriter(byte[] buf, int index, int length) {
		this.buf = buf;
		this.baseIndex = index;
		this.length = length;
		this.index = 0;
	}

    public static HeapDataWriter of(byte[] buf, int index, int length) {
        return new HeapDataWriter(buf, index, length);
    }

    public static HeapDataWriter of(byte[] buf) {
        return of(buf, 0, buf.length);
    }

    public static HeapDataWriter allocate(int n) {
        return of(new byte[n]);
    }

	private void checkIndex(int index) {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException();
        }
	}

	private void failFast(int minBytes) {
		checkIndex(index + minBytes);
	}

    public byte[] getUnderlyingBuf() {
        return buf;
    }

    public int getUnderlyingIndex() {
        return baseIndex;
    }

    public int getUnderlyingLength() {
        return length;
    }

	@Override
	public void writeBytes(byte[] bytes) {
		failFast(bytes.length);
		System.arraycopy(bytes, 0, buf, index, bytes.length);
		index += bytes.length;
	}

	@Override
	public void writeInt8(byte int8) {
		failFast(1);
		buf[baseIndex + index++] = int8;
	}

	@Override
	public void writeUInt8(short uint8) {
		failFast(1);
		buf[baseIndex + index++] = (byte) (uint8 & 0xFF);
	}

	@Override
	public void writeInt16(short int16) {
		failFast(2);
		buf[baseIndex + index++] = (byte) (int16 >> 8);
		buf[baseIndex + index++] = (byte) int16;
	}

	@Override
	public void writeUInt16(int uint16) {
		failFast(2);
		buf[baseIndex + index++] = (byte) (uint16 >> 8);
		buf[baseIndex + index++] = (byte) uint16;
	}

	@Override
	public void writeInt32(int int32) {
		failFast(4);
		buf[baseIndex + index++] = (byte) (int32 >> 24);
		buf[baseIndex + index++] = (byte) (int32 >> 16);
		buf[baseIndex + index++] = (byte) (int32 >> 8);
		buf[baseIndex + index++] = (byte) int32;
	}

	@Override
	public void writeUInt32(long uint32) {
		failFast(4);
		buf[baseIndex + index++] = (byte) (uint32 >> 24);
		buf[baseIndex + index++] = (byte) (uint32 >> 16);
		buf[baseIndex + index++] = (byte) (uint32 >> 8);
		buf[baseIndex + index++] = (byte) uint32;
	}

	@Override
	public void writeInt64(long int64) {
		failFast(8);
		buf[baseIndex + index++] = (byte) (int64 >> 56);
		buf[baseIndex + index++] = (byte) (int64 >> 48);
		buf[baseIndex + index++] = (byte) (int64 >> 40);
		buf[baseIndex + index++] = (byte) (int64 >> 32);
		buf[baseIndex + index++] = (byte) (int64 >> 24);
		buf[baseIndex + index++] = (byte) (int64 >> 16);
		buf[baseIndex + index++] = (byte) (int64 >> 8);
		buf[baseIndex + index++] = (byte) int64;
	}

	@Override
	public void writeUInt64(BigInteger uint64) {
		failFast(8);
		buf[baseIndex + index++] = uint64.shiftRight(56).byteValue();
		buf[baseIndex + index++] = uint64.shiftRight(48).byteValue();
		buf[baseIndex + index++] = uint64.shiftRight(40).byteValue();
		buf[baseIndex + index++] = uint64.shiftRight(32).byteValue();
		buf[baseIndex + index++] = uint64.shiftRight(24).byteValue();
		buf[baseIndex + index++] = uint64.shiftRight(16).byteValue();
		buf[baseIndex + index++] = uint64.shiftRight(8).byteValue();
		buf[baseIndex + index++] = uint64.byteValue();
	}

    @Override
    public int getRemaining() {
        return length - index;
    }

    @Override
	public int getPosition() {
		return index;
	}

	@Override
	public void setPosition(int position) {
		checkIndex(position);
		this.index = position;
	}

	@Override
	public void slice(int offset, int length) {
		// check lower and upper bounds
		checkIndex(offset);
		checkIndex(offset + length);

        this.baseIndex += offset;
        this.length = length;
        this.index = 0;
	}

    @Override
    public HeapDataReader reader() {
        return new HeapDataReader(buf, baseIndex, length);
    }
}
