package org.heat.shared.io;

import org.behaviorismanaged.core.io.DataReader;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public abstract class AbstractDataReaderTest {
    protected abstract DataReader create(int... lit);

    protected DataReader empty(int len) {
        return create(new int[len]);
    }

    protected DataReader maxUInt8() {
        return create(-1);
    }
    protected DataReader maxUInt16() {
        return create(-1, -1);
    }
    protected DataReader maxUInt32() {
        return create(-1, -1, -1, -1);
    }
    protected DataReader maxUInt64() {
        return create(-1, -1, -1, -1, -1, -1, -1, -1);
    }

    @Test
    public void testReadUInt8() throws Exception {
        DataReader r = maxUInt8();
        assertThat(r.readUInt8(), equalTo((short) 255));
    }

    @Test
    public void testReadInt8() throws Exception {
        DataReader r = maxUInt8();
        assertThat(r.readInt8(), equalTo((byte) -1));
    }

    @Test
    public void testReadUInt16() throws Exception {
        DataReader r = maxUInt16();
        assertThat(r.readUInt16(), equalTo(0xFFFF));
    }

    @Test
    public void testReadInt16() throws Exception {
        DataReader r = maxUInt16();
        assertThat(r.readInt16(), equalTo((short) -1));
    }

    @Test
    public void testReadInt16_bugfix1() throws Exception {
        DataReader r = create(0, -3);
        assertThat(r.readInt16(), equalTo((short) 253));
    }

    @Test
    public void testReadUInt32() throws Exception {
        DataReader r = maxUInt32();
        assertThat(r.readUInt32(), equalTo(0xFFFFFFFFL));
    }

    @Test
    public void testReadInt32() throws Exception {
        DataReader r = maxUInt32();
        assertThat(r.readInt32(), equalTo(-1));
    }

    @Test
    public void testReadInt32_bugfix1() throws Exception {
        DataReader r = create(0, 0, 0, -3);
        assertThat(r.readInt32(), equalTo(253));
    }

    @Test
    public void testReadInt64() throws Exception {
        DataReader r = maxUInt64();
        assertThat(r.readInt64(), equalTo(-1L));
    }

    @Test
    public void testReadInt64_bugfix1() throws Exception {
        DataReader r = create(0, 0, 0, 0, 0, 0, 0, -3);
        assertThat(r.readInt64(), equalTo(253L));
    }

    @Test
    public void testReadUInt64() throws Exception {
        DataReader r = maxUInt64();
        assertThat(r.readUInt64(), equalTo(BigInteger.valueOf(0xFFFFFFFFL).shiftLeft(32).or(BigInteger.valueOf(0xFFFFFFFFL))));
    }

    @Test
    public void testRead() throws Exception {
        DataReader r = create(1, 2, 3, 4);

        DataReader sliced = r.read(2); // read a 2-bytes long slice
        assertThat(sliced.readInt8(), equalTo((byte) 1));
        assertThat(sliced.readInt8(), equalTo((byte) 2));

        sliced = r.read(2); // read one more slice
        assertThat(sliced.readInt8(), equalTo((byte) 3));
        assertThat(sliced.readInt8(), equalTo((byte) 4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void wellDefinedSliceBounds() throws Exception {
        DataReader r = create(1, 2, 3, 4);

        DataReader sliced = r.read(2);
        sliced.readInt8(); // ok
        sliced.readInt8(); // ok
        sliced.readInt8(); // not ok
    }

    @Test
    public void testGetPosition() throws Exception {
        DataReader r = create(42, 84, 21, 7);

        assertEquals(0, r.getPosition());
        assertEquals(42, r.readInt8());
        assertEquals(1, r.getPosition());
        assertEquals(84, r.readInt8());
        assertEquals(2, r.getPosition());
    }

    @Test
    public void testSetPosition() throws Exception {
        DataReader r = create(42, 84, 21, 7);

        assertEquals(0, r.getPosition());

        r.setPosition(1);
        assertEquals(1, r.getPosition());

        r.addPosition(2);
        assertEquals(3, r.getPosition());

        r.resetPosition();
        assertEquals(0, r.getPosition());
    }
}
