package org.heat.shared.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ByteBufferReaderTest.class,
        HeapDataReaderTest.class,
        InputStreamReaderTest.class,
})
public class DataReaderTestSuite {}
