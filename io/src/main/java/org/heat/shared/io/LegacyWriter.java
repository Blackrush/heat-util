package org.heat.shared.io;

import java.math.BigInteger;

@SuppressWarnings("ForLoopReplaceableByForEach")
public abstract class LegacyWriter extends org.behaviorismanaged.core.io.DataWriter implements DataWriter {
    @Override
    public int plusPosition(int position) {
        return movePosition(position);
    }

    @Override
    public void resetPosition() {
        setPosition(0);
    }

    public void write_bool(boolean bool) {
        writeBoolean(bool);
    }
    public void write_array_bool(boolean[] array_bool) {
        for (int i = 0; i < array_bool.length; i++) {
            writeBoolean(array_bool[i]);
        }
    }
    public void write_i8(byte i8) {
        writeInt8(i8);
    }
    public void write_i8(int i8) {
        writeInt8((byte) i8);
    }
    public void write_array_i8(byte[] array_i8) {
        writeBytes(array_i8);
    }
    public void write_ui8(short ui8) {
        writeUInt8(ui8);
    }
    public void write_array_ui8(short[] array_ui8) {
        for (int i = 0; i < array_ui8.length; i++) {
            write_ui8(array_ui8[i]);
        }
    }
    public void write_i16(short i16) {
        writeInt16(i16);
    }
    public void write_array_i16(short[] array_i16) {
        for (int i = 0; i < array_i16.length; i++) {
            write_i16(array_i16[i]);
        }
    }
    public void write_ui16(int ui16) {
        writeUInt16(ui16);
    }
    public void write_array_ui16(int[] array_ui16) {
        for (int i = 0; i < array_ui16.length; i++) {
            write_ui16(array_ui16[i]);
        }
    }
    public void write_i32(int i32) {
        writeInt32(i32);
    }
    public void write_array_i32(int[] array_i32) {
        for (int i = 0; i < array_i32.length; i++) {
            write_i32(array_i32[i]);
        }
    }
    public void write_ui32(long ui32) {
        writeUInt32(ui32);
    }
    public void write_array_ui32(long[] array_ui32) {
        for (int i = 0; i < array_ui32.length; i++) {
            write_ui32(array_ui32[i]);
        }
    }
    public void write_i64(long i64) {
        writeInt64(i64);
    }
    public void write_array_i64(long[] array_i64) {
        for (int i = 0; i < array_i64.length; i++) {
            write_i64(array_i64[i]);
        }
    }
    public void write_ui64(BigInteger ui64) {
        writeUInt64(ui64);
    }
    public void write_array_ui64(BigInteger[] array_ui64) {
        for (int i = 0; i < array_ui64.length; i++) {
            write_ui64(array_ui64[i]);
        }
    }
    public void write_f32(float f32) {
        writeFloat(f32);
    }
    public void write_array_f32(float[] array_f32) {
        for (int i = 0; i < array_f32.length; i++) {
            write_f32(array_f32[i]);
        }
    }
    public void write_f64(double f64) {
        writeDouble(f64);
    }
    public void write_array_f64(double[] array_f64) {
        for (int i = 0; i < array_f64.length; i++) {
            write_f64(array_f64[i]);
        }
    }
    public void write_str(String str) {
        writeUTF(str);
    }
    public void write_array_str(String[] array_str) {
        for (int i = 0; i < array_str.length; i++) {
            write_str(array_str[i]);
        }
    }

    @Override
    public void write_vi16(short vi16) {
        short acc = vi16;
        do {
            byte b = (byte) (acc & 0x7f);
            acc >>= 7;

            if (acc == 0) {
                b |= 0x80;
            }

            write_i8(b);
        } while (acc != 0);
    }

    @Override
    public void write_vi32(int vi32) {
        int acc = vi32;
        do {
            byte b = (byte) (acc & 0x7f);
            acc >>= 7;

            if (acc == 0) {
                b |= 0x80;
            }

            write_i8(b);
        } while (acc != 0);
    }

    @Override
    public void write_vi64(long vi64) {
        long acc = vi64;
        do {
            byte b = (byte) (acc & 0x7f);
            acc >>= 7;

            if (acc == 0) {
                b |= 0x80;
            }

            write_i8(b);
        } while (acc != 0);
    }

    @Override
    public void write_array_vi16(short[] array_vi16) {
        for (int i = 0; i < array_vi16.length; i++) {
            write_vi16(array_vi16[i]);
        }
    }

    @Override
    public void write_array_vi32(int[] array_vi32) {
        for (int i = 0; i < array_vi32.length; i++) {
            write_vi32(array_vi32[i]);
        }
    }

    @Override
    public void write_array_vi64(long[] array_vi64) {
        for (int i = 0; i < array_vi64.length; i++) {
            write_vi64(array_vi64[i]);
        }
    }
}
