package org.heat.shared.io;

import org.behaviorismanaged.core.io.DataReader;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public final class InputStreamReader extends LegacyReader {
    private final InputStream in;

    private InputStreamReader(InputStream in) {
        this.in = in;
    }

    public static InputStreamReader of(int... i) {
        byte[] b = new byte[i.length];
        for (int j = 0; j < i.length; j++) {
            b[j] = (byte) i[j];
        }
        return of(b);
    }

    public static InputStreamReader of(byte[] b, int offset, int length) {
        return new InputStreamReader(new ByteArrayInputStream(b, offset, length));
    }

    public static InputStreamReader of(byte[] b) {
        return new InputStreamReader(new ByteArrayInputStream(b));
    }

    public static InputStreamReader of(InputStream in) {
        return new InputStreamReader(in);
    }

    @Override
    public long getPosition() {
        throw new Error("not implemented");
    }

    @Override
    public void setPosition(long position) {
        throw new Error("not implemented");
    }

    @Override
    public boolean readable(int n) {
        try {
            return in.available() >= n;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] readBytes(int n) {
        byte[] res = new byte[n];
        try {
            int totalRead = 0;
            while (totalRead < n) {
                int read = in.read(res, totalRead, n - totalRead);
                if (read <= 0) throw new EOFException();
                totalRead += read;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public DataReader read(int n) {
        return of(readBytes(n));
    }

    @Override
    public ByteBuffer all() {
		return IO.readAllDirect(in::read).asReadOnlyBuffer();
    }

    @Override
    public byte[] allBytes() {
        return IO.readAll(in::read);
    }

    @Override
    public short readUInt8() {
        int val;
        try {
            val = in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (val == -1) {
            throw new IndexOutOfBoundsException();
        }
        return (short) val;
    }

    @Override
    public byte readInt8() {
        return (byte) readUInt8();
    }

    @Override
    public int readUInt16() {
        return readUInt8() << 8 | readUInt8();
    }

    @Override
    public short readInt16() {
        return (short) readUInt16();
    }

    @Override
    public long readUInt32() {
        return (long) (readUInt8()) << 24 | (long) (readUInt8()) << 16 | (long) (readUInt8()) << 8 | (long) readUInt8();
    }

    @Override
    public int readInt32() {
        return (int) readUInt32();
    }

    @Override
    public long readInt64() {
        return readUInt32() << 32 | readUInt32();
    }

    @Override
    public BigInteger readUInt64() {
        return BigInteger.valueOf(readUInt32()).shiftLeft(32).or(BigInteger.valueOf(readUInt32()));
    }
}
