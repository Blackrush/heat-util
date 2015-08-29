package org.heat.shared.io;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class AutoGrowingWriterTest extends AbstractDataWriterTest<AutoGrowingWriter> {
    @Override
    protected AutoGrowingWriter writer(int n) {
        return new AutoGrowingWriter();
    }

    @Override
    protected byte[] read(AutoGrowingWriter w) {
        return w.toByteArray();
    }

    @Test
    public void testToInputStream() throws Exception {
        AutoGrowingWriter writer = new AutoGrowingWriter();
        writer.writeInt64(-1L);
        InputStream is = writer.toInputStream();

        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(0xFF, is.read());
        assertEquals(-1, is.read());
    }

    @Test
    public void testEmptyToInputStream() throws Exception {
        AutoGrowingWriter writer = new AutoGrowingWriter();
        InputStream is = writer.toInputStream();
        assertEquals(-1, is.read());
    }

    @Test
    public void testConcurrentToInputStream() throws Exception {
        AutoGrowingWriter writer = new AutoGrowingWriter();
        writer.writeInt8(Byte.MAX_VALUE);
        InputStream is = writer.toInputStream();
        writer.writeInt8(Byte.MAX_VALUE);

        assertEquals(Byte.MAX_VALUE, is.read());
        assertEquals(-1, is.read());
    }
}