package org.heat.shared.io;

public interface BaseDataReader {
    long getPosition();
    void setPosition(long position);
    long plusPosition(long position);
    void resetPosition();
    boolean canRead(int n);
}
