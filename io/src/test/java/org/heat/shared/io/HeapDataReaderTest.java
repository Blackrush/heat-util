package org.heat.shared.io;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

public class HeapDataReaderTest extends AbstractDataReaderTest {
    @Override
    protected HeapDataReader create(int... lit) {
        byte[] res = new byte[lit.length];
        for (int i = 0; i < lit.length; i++) {
            res[i] = (byte) lit[i];
        }
        return new HeapDataReader(res, 0, res.length);
    }

    short vi16(int... lit) {
        return create(lit).read_vi16();
    }
    int vi32(int... lit) {
        return create(lit).read_vi32();
    }
    long vi64(int... lit) {
        return create(lit).read_vi64();
    }

    @Test
    public void testReadVarInt16() throws Exception {
        assertEquals("vi16", 0x7f, vi16(0xff));
        assertEquals("vi16", 0x00, vi16(0x80));
        assertEquals("vi16", 0x3fff, vi16(0x7f, 0xff));

        HeapDataReader r = create(0xff, 0xff);
        assertEquals("vi16", 0x7f, r.read_vi16());
        assertEquals("vi16", 0x7f, r.read_vi16());
        assertFalse("vi16", r.canRead(1));
    }

    @Test
    public void testReadVarInt32() throws Exception {
        assertEquals("vi32", 0x7f, vi32(0xff));
        assertEquals("vi32", 0x00, vi32(0x80));
        assertEquals("vi32", 0x0fffffff, vi32(0x7f, 0x7f, 0x7f, 0xff));

        HeapDataReader r = create(0xff, 0xff, 0xff, 0xff);
        assertEquals("vi32", 0x7f, r.read_vi32());
        assertEquals("vi32", 0x7f, r.read_vi32());
        assertEquals("vi32", 0x7f, r.read_vi32());
        assertEquals("vi32", 0x7f, r.read_vi32());
        assertFalse("vi32", r.canRead(1));
    }

    @Test
    public void testReadVarInt64() throws Exception {
        assertEquals("vi64", 0x7fL, vi64(0xff));
        assertEquals("vi64", 0x00L, vi64(0x80));
        assertEquals("vi64", 0x00ffffffffffffffL, vi64(0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0xff));

        HeapDataReader r = create(0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff);
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertEquals("vi64", 0x7fL, r.read_vi64());
        assertFalse("vi64", r.canRead(1));
    }
}
