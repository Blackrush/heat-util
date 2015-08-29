package org.heat.shared.io;

import org.behaviorismanaged.core.io.DataReader;

public class ByteBufferReaderTest extends AbstractDataReaderTest {
    @Override
    protected DataReader create(int... lit) {
        return ByteBufferReader.of(lit);
    }
}
