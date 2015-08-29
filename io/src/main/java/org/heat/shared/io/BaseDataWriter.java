package org.heat.shared.io;

public interface BaseDataWriter {
    int getPosition();
    void setPosition(int position);
    int plusPosition(int position);
    void resetPosition();

    void slice(int position, int length);
}
