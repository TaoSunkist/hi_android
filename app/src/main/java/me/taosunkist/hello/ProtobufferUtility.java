package me.taosunkist.hello;

public class ProtobufferUtility {
    public static <T> byte[] getBytes(T t) {

        byte[] bytes = null;

        if ((t instanceof Short)) {

            short s = ((Short) t).shortValue();
            bytes = new byte[2];
            bytes[0] = (byte) (s);
            bytes[1] = (byte) (s >> 8);
        } else if (t instanceof Integer) {

            int i = ((Integer) t).intValue();
            bytes = new byte[4];
            bytes[0] = (byte) (i);
            bytes[1] = (byte) (i >> 8);
            bytes[2] = (byte) (i >> 2 * 8);
            bytes[3] = (byte) (i >> 3 * 8);
        } else if (t instanceof Long) {

            long l = ((Long) t).longValue();
            bytes = new byte[8];
            bytes[0] = (byte) (l);
            bytes[1] = (byte) (l >> 8);
            bytes[2] = (byte) (l >> 2 * 8);
            bytes[3] = (byte) (l >> 3 * 8);
            bytes[4] = (byte) (l >> 4 * 8);
            bytes[5] = (byte) (l >> 5 * 8);
            bytes[6] = (byte) (l >> 6 * 8);
            bytes[7] = (byte) (l >> 7 * 8);
        } else if (t instanceof Float) {

            int f = Float.floatToIntBits((Float) t);
            bytes = new byte[4];
            bytes[0] = (byte) (f);
            bytes[1] = (byte) (f >> 8);
            bytes[2] = (byte) (f >> 2 * 8);
            bytes[3] = (byte) (f >> 3 * 8);
        } else if (t instanceof Double) {

            long d = Double.doubleToLongBits((Double) t);
            bytes = new byte[8];
            bytes[0] = (byte) (d);
            bytes[1] = (byte) (d >> 8);
            bytes[2] = (byte) (d >> 2 * 8);
            bytes[3] = (byte) (d >> 3 * 8);
            bytes[4] = (byte) (d >> 4 * 8);
            bytes[5] = (byte) (d >> 5 * 8);
            bytes[6] = (byte) (d >> 6 * 8);
            bytes[7] = (byte) (d >> 7 * 8);
        } else if (t instanceof Character) {
            char c = ((Character) t).charValue();
            bytes = new byte[2];
            bytes[0] = (byte) (c);
            bytes[1] = (byte) (c >> 8);
        }

        return bytes;
    }
}