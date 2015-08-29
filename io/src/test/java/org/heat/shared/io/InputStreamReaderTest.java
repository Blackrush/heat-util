package org.heat.shared.io;

import org.behaviorismanaged.core.io.DataReader;
import org.junit.Ignore;
import org.junit.Test;

public class InputStreamReaderTest extends AbstractDataReaderTest {

    @Override
    protected DataReader create(int... lit) {
        return InputStreamReader.of(lit);
    }

    @Test
    @Ignore("InputStreamReader does not implement #getPosition since InputStream has not any position-related operations")
    @Override
    public void testGetPosition() throws Exception {

    }

    @Test
    @Ignore("InputStreamReader does not implement #setPosition since InputStream has not any position-related operations")
    @Override
    public void testSetPosition() throws Exception {

    }
}
