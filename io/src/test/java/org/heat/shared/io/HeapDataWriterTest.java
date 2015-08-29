package org.heat.shared.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeapDataWriterTest extends AbstractDataWriterTest<HeapDataWriter> {
    @Override
    protected HeapDataWriter writer(int n) {
        return new HeapDataWriter(new byte[n], 0, n);
    }

    @Override
    protected byte[] read(HeapDataWriter w) {
        byte[] b = new byte[w.getUnderlyingLength()];
        System.arraycopy(w.getUnderlyingBuf(), w.getUnderlyingIndex(), b, 0, w.getUnderlyingLength());
        return b;
    }

    @Test
    public void testWriteVarInt16() throws Exception {
        HeapDataWriter w = writer(4);
        w.write_vi16((short) 0);
        w.write_vi16((short) 0x7f);
        w.write_vi16((short) 0x3fff);

        byte[] bytes = read(w);
        assertEquals("bytes[0]", 0x80, bytes[0] & 0xff);
        assertEquals("bytes[1]", 0xff, bytes[1] & 0xff);
        assertEquals("bytes[2]", 0x7f, bytes[2] & 0xff);
        assertEquals("bytes[3]", 0xff, bytes[3] & 0xff);

        HeapDataReader r = new HeapDataReader(bytes, 0, bytes.length);
        assertEquals(0x00, r.read_vi16());
        assertEquals(0x7f, r.read_vi16());
        assertEquals(0x3fff, r.read_vi16());
    }

    @Test
    public void testWriteVarInt32() throws Exception {
        HeapDataWriter w = writer(6);
        w.write_vi32(0);
        w.write_vi32(0x7f);
        w.write_vi32(0x0fffffff);

        byte[] bytes = read(w);
        assertEquals("bytes[0]", 0x80, bytes[0] & 0xff);
        assertEquals("bytes[1]", 0xff, bytes[1] & 0xff);
        assertEquals("bytes[2]", 0x7f, bytes[2] & 0xff);
        assertEquals("bytes[3]", 0x7f, bytes[3] & 0xff);
        assertEquals("bytes[4]", 0x7f, bytes[4] & 0xff);
        assertEquals("bytes[5]", 0xff, bytes[5] & 0xff);

        HeapDataReader r = new HeapDataReader(bytes, 0, bytes.length);
        assertEquals(0x00, r.read_vi32());
        assertEquals(0x7f, r.read_vi32());
        assertEquals(0x0fffffff, r.read_vi32());
    }

    @Test
    public void testWriteVarInt64() throws Exception {
        HeapDataWriter w = writer(10);
        w.write_vi64(0);
        w.write_vi64(0x7f);
        w.write_vi64(0x00ffffffffffffffL);

        byte[] bytes = read(w);
        assertEquals("bytes[0]", 0x80, bytes[0] & 0xff);
        assertEquals("bytes[1]", 0xff, bytes[1] & 0xff);
        assertEquals("bytes[2]", 0x7f, bytes[2] & 0xff);
        assertEquals("bytes[3]", 0x7f, bytes[3] & 0xff);
        assertEquals("bytes[4]", 0x7f, bytes[4] & 0xff);
        assertEquals("bytes[5]", 0x7f, bytes[5] & 0xff);
        assertEquals("bytes[6]", 0x7f, bytes[6] & 0xff);
        assertEquals("bytes[7]", 0x7f, bytes[7] & 0xff);
        assertEquals("bytes[8]", 0x7f, bytes[8] & 0xff);
        assertEquals("bytes[9]", 0xff, bytes[9] & 0xff);

        HeapDataReader r = new HeapDataReader(bytes, 0, bytes.length);
        assertEquals(0x00, r.read_vi64());
        assertEquals(0x7f, r.read_vi64());
        assertEquals(0x00ffffffffffffffL, r.read_vi64());
    }
}
