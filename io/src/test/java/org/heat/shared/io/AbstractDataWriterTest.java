package org.heat.shared.io;

import org.behaviorismanaged.core.io.DataWriter;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public abstract class AbstractDataWriterTest<T extends DataWriter> {
    protected abstract T writer(int n);

    protected abstract byte[] read(T w);
    protected final byte[] bytes(int... lit) {
        byte[] b = new byte[lit.length];
        for (int i = 0; i < lit.length; i++) {
            b[i] = (byte) lit[i];
        }
        return b;
    }

    @Test
    public void testWriteBytes() {
        T w = writer(4);
        w.writeBytes(bytes(-1, -1, -1, -1));

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 4);
        assertEquals(b[0], -1);
        assertEquals(b[1], -1);
        assertEquals(b[2], -1);
        assertEquals(b[3], -1);
    }

    @Test
    public void testWriteInt8() {
        T w = writer(1);
        w.writeInt8((byte) -1);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 1);
        assertEquals(b[0], (byte) -1);
    }

    @Test
    public void testWriteUInt8() {
        T w = writer(1);
        w.writeUInt8((short) 0xFF);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 1);
        assertEquals(b[0], (byte) -1);
    }

    @Test
    public void testWriteInt16() {
        T w = writer(2);
        w.writeInt16((short) -1);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 2);
        assertEquals(b[0], (byte) -1);
        assertEquals(b[1], (byte) -1);
    }

    @Test
    public void testWriteUInt16() {
        T w = writer(2);
        w.writeUInt16(0xFFFF);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 2);
        assertEquals(b[0], (byte) -1);
        assertEquals(b[1], (byte) -1);
    }

    @Test
    public void testWriteInt32() {
        T w = writer(4);
        w.writeInt32(-1);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 4);
        assertEquals(b[0], (byte) -1);
        assertEquals(b[1], (byte) -1);
        assertEquals(b[2], (byte) -1);
        assertEquals(b[3], (byte) -1);
    }

    @Test
    public void testWriteUInt32() {
        T w = writer(4);
        w.writeUInt32(0xFFFFFFFF);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 4);
        assertEquals(b[0], (byte) -1);
        assertEquals(b[1], (byte) -1);
        assertEquals(b[2], (byte) -1);
        assertEquals(b[3], (byte) -1);
    }

    @Test
    public void testWriteInt64() {
        T w = writer(8);
        w.writeInt64(-1);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 8);
        assertEquals(b[0], (byte) -1);
        assertEquals(b[1], (byte) -1);
        assertEquals(b[2], (byte) -1);
        assertEquals(b[3], (byte) -1);
        assertEquals(b[4], (byte) -1);
        assertEquals(b[5], (byte) -1);
        assertEquals(b[6], (byte) -1);
        assertEquals(b[7], (byte) -1);
    }

    public static final BigInteger MAX_UINT64 = BigInteger.valueOf(0xFFFFFFFFL).shiftLeft(32)
                                            .or(BigInteger.valueOf(0xFFFFFFFFL));

    @Test
    public void testWriteUInt64() {
        T w = writer(8);
        w.writeUInt64(MAX_UINT64);

        byte[] b = read(w);
        assertEquals("wrong array length", b.length, 8);
        assertEquals(b[0], (byte) -1);
        assertEquals(b[1], (byte) -1);
        assertEquals(b[2], (byte) -1);
        assertEquals(b[3], (byte) -1);
        assertEquals(b[4], (byte) -1);
        assertEquals(b[5], (byte) -1);
        assertEquals(b[6], (byte) -1);
        assertEquals(b[7], (byte) -1);
    }

    @Test
    public void testGetPosition() {
        T w = writer(1);
        assertEquals("wrong position", w.getPosition(), 0);
        w.writeInt8((byte) 0);
        assertEquals("wrong position", w.getPosition(), 1);
    }

    @Test
    public void testSetPosition() {
        T w = writer(1);
        assertEquals("wrong position", w.getPosition(), 0);
        w.setPosition(1);
        assertEquals("wrong position", w.getPosition(), 1);
    }

    @Test
    public void testSlice() throws Exception {
        T w = writer(3);
        w.slice(1, 1);

        assertEquals("wrong amount of remaining bytes", w.getRemaining(), 1);
    }
}
