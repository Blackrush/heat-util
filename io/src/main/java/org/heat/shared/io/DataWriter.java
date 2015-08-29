package org.heat.shared.io;

import java.math.BigInteger;

public interface DataWriter extends BaseDataWriter {
    void write_bool(boolean bool);
    void write_array_bool(boolean[] array_bool);
    void write_i8(byte i8);
    void write_i8(int i8);
    void write_array_i8(byte[] array_i8);
    void write_ui8(short ui8);
    void write_array_ui8(short[] array_ui8);
    void write_i16(short i16);
    void write_array_i16(short[] array_i16);
    void write_ui16(int ui16);
    void write_array_ui16(int[] array_ui16);
    void write_i32(int i32);
    void write_array_i32(int[] array_i32);
    void write_ui32(long ui32);
    void write_array_ui32(long[] array_ui32);
    void write_i64(long i64);
    void write_array_i64(long[] array_i64);
    void write_ui64(BigInteger ui64);
    void write_array_ui64(BigInteger[] array_ui64);
    void write_f32(float f32);
    void write_array_f32(float[] array_f32);
    void write_f64(double f64);
    void write_array_f64(double[] f64);
    void write_str(String str);
    void write_array_str(String[] array_str);
    void write_vi16(short vi16);
    void write_array_vi16(short[] array_vi16);
    void write_vi32(int vi32);
    void write_array_vi32(int[] array_vi32);
    void write_vi64(long vi64);
    void write_array_vi64(long[] array_vi64);
}
