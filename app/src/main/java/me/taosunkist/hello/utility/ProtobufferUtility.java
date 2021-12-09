package me.taosunkist.hello.utility;

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

    /**
     * 将int转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n
     *         int
     * @return byte[]
     */
    public static byte[] intToByteBig(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组（小端）
     *
     * @param n
     *         int
     * @return byte[]
     */
    public static byte[] intToByteLittle(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * byte数组到int的转换(小端)
     *
     * @param bytes
     * @return
     */
    public static int bytes2IntLittle(byte[] bytes) {
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * byte数组到int的转换(大端)
     *
     * @param bytes
     * @return
     */
    public static int bytes2IntBig(byte[] bytes) {
        int int1 = bytes[3] & 0xff;
        int int2 = (bytes[2] & 0xff) << 8;
        int int3 = (bytes[1] & 0xff) << 16;
        int int4 = (bytes[0] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n
     *         short
     * @return byte[]
     */
    public static byte[] shortToByteBig(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组(小端)
     *
     * @param n
     *         short
     * @return byte[]
     */
    public static byte[] shortToByteLittle(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 读取小端byte数组为short
     *
     * @param b
     * @return
     */
    public static short byteToShortLittle(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    /**
     * 读取大端byte数组为short
     *
     * @param b
     * @return
     */
    public static short byteToShortBig(byte[] b) {
        return (short) (((b[0] << 8) | b[1] & 0xff));
    }

    /**
     * long类型转byte[] (大端)
     *
     * @param n
     * @return
     */
    public static byte[] longToBytesBig(long n) {
        byte[] b = new byte[8];
        b[7] = (byte) (n & 0xff);
        b[6] = (byte) (n >> 8 & 0xff);
        b[5] = (byte) (n >> 16 & 0xff);
        b[4] = (byte) (n >> 24 & 0xff);
        b[3] = (byte) (n >> 32 & 0xff);
        b[2] = (byte) (n >> 40 & 0xff);
        b[1] = (byte) (n >> 48 & 0xff);
        b[0] = (byte) (n >> 56 & 0xff);
        return b;
    }

    /**
     * long类型转byte[] (小端)
     *
     * @param n
     * @return
     */
    public static byte[] longToBytesLittle(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        b[4] = (byte) (n >> 32 & 0xff);
        b[5] = (byte) (n >> 40 & 0xff);
        b[6] = (byte) (n >> 48 & 0xff);
        b[7] = (byte) (n >> 56 & 0xff);
        return b;
    }

    /**
     * byte[]转long类型(小端)
     *
     * @param array
     * @return
     */
    public static long bytesToLongLittle(byte[] array) {
        return ((((long) array[0] & 0xff) << 0)
                | (((long) array[1] & 0xff) << 8)
                | (((long) array[2] & 0xff) << 16)
                | (((long) array[3] & 0xff) << 24)
                | (((long) array[4] & 0xff) << 32)
                | (((long) array[5] & 0xff) << 40)
                | (((long) array[6] & 0xff) << 48)
                | (((long) array[7] & 0xff) << 56));
    }

    /**
     * byte[]转long类型(大端)
     *
     * @param array
     * @return
     */
    public static long bytesToLongBig(byte[] array) {
        return ((((long) array[0] & 0xff) << 56)
                | (((long) array[1] & 0xff) << 48)
                | (((long) array[2] & 0xff) << 40)
                | (((long) array[3] & 0xff) << 32)
                | (((long) array[4] & 0xff) << 24)
                | (((long) array[5] & 0xff) << 16)
                | (((long) array[6] & 0xff) << 8)
                | (((long) array[7] & 0xff) << 0));
    }
}