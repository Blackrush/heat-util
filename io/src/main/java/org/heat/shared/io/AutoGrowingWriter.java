package org.heat.shared.io;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public final class AutoGrowingWriter extends LegacyWriter {
    static class Node {
        Node next;
        byte val;
    }
    
    Node head = new Node();
    Node cur = head;
    int pos = 0;
    int length = 0;
    
    private void add(byte b) {
        cur.val = b;
        if (cur.next == null) {
            cur.next = new Node();
            length++;
        }
        cur = cur.next;
        pos++;
    }

    public byte[] toByteArray() {
        byte[] res = new byte[length];
        Node it = head;
        for (int i = 0; i < length; i++) {
            res[i] = it.val;
            it = it.next;
        }
        return res;
    }
    
    @Override
    public void writeBytes(byte[] bytes) {
        for (byte b : bytes) {
            add(b);
        }
    }

    @Override
    public void writeInt8(byte int8) {
        add(int8);
    }

    @Override
    public void writeUInt8(short uint8) {
        add((byte) (uint8 & 0xFF));
    }

    @Override
    public void writeInt16(short int16) {
        add((byte) ((int16 >> 8) & 0xFF));
        add((byte) (int16 & 0xFF));
    }

    @Override
    public void writeUInt16(int uint16) {
        add((byte) ((uint16 >> 8) & 0xFF));
        add((byte) (uint16 & 0xFF));
    }

    @Override
    public void writeInt32(int int32) {
        add((byte) ((int32 >> 24) & 0xFF));
        add((byte) ((int32 >> 16) & 0xFF));
        add((byte) ((int32 >> 8) & 0xFF));
        add((byte) (int32 & 0xFF));
    }

    @Override
    public void writeUInt32(long uint32) {
        add((byte) ((uint32 >> 24) & 0xFF));
        add((byte) ((uint32 >> 16) & 0xFF));
        add((byte) ((uint32 >> 8) & 0xFF));
        add((byte) (uint32 & 0xFF));
    }

    @Override
    public void writeInt64(long int64) {
        add((byte) ((int64 >> 56) & 0xFF));
        add((byte) ((int64 >> 48) & 0xFF));
        add((byte) ((int64 >> 40) & 0xFF));
        add((byte) ((int64 >> 32) & 0xFF));
        add((byte) ((int64 >> 24) & 0xFF));
        add((byte) ((int64 >> 16) & 0xFF));
        add((byte) ((int64 >> 8) & 0xFF));
        add((byte) (int64 & 0xFF));
    }

    @Override
    public void writeUInt64(BigInteger uint64) {
        add(uint64.shiftRight(56).byteValue());
        add(uint64.shiftRight(48).byteValue());
        add(uint64.shiftRight(40).byteValue());
        add(uint64.shiftRight(32).byteValue());
        add(uint64.shiftRight(24).byteValue());
        add(uint64.shiftRight(16).byteValue());
        add(uint64.shiftRight(8).byteValue());
        add(uint64.byteValue());
    }

    @Override
    public int getRemaining() {
        return length - pos;
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Override
    public void setPosition(int position) {
        cur = head;
        for (int i = 0; i < position; i++) {
            if (cur.next == null) cur.next = new Node();
            cur = cur.next;
        }
        this.pos = position;
    }

    @Override
    public void slice(int offset, int length) {
        // shift to right "offset"-th times
        for (int i = 0; i < offset; i++) {
            if (head.next == null) {
                head = new Node();
                break;
            }
            head = head.next;
        }
        cur = head;

        // ensure we have "length"-th elements
        Node it = head;
        for (int i = 0; i < length; i++) {
            if (it.next == null) {
                it.next = new Node();
            }
            it = it.next;
        }

        // forget overflowing elements
        it.next = null;

        // update stats
        this.pos = 0;
        this.length = length;
    }

    @Override
    public HeapDataReader reader() {
        return new HeapDataReader(toByteArray(), 0, length);
    }

    public InputStream toInputStream() {
        return new InputStream() {
            final Node end = cur;
            Node node = head;

            @Override
            public int read() throws IOException {
                int result = -1;
                if (node != end) {
                    result = node.val & 0xFF;
                    node = node.next;
                }
                return result;
            }
        };
    }
}
