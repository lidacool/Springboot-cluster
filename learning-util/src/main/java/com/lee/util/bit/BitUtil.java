package com.lee.util.bit;

import java.util.*;

public class BitUtil {
    public BitUtil() {
    }

    public static byte[] int2bytes(int intValue) {
        byte[] result = new byte[4];

        for(int i = 3; i >= 0; --i) {
            result[i] = (byte)(intValue & 255);
            intValue >>= 8;
        }

        return result;
    }

    public static int bytes2oneInt(byte[] bytes) {
        return bytes2int(bytes, 0);
    }

    public static int bytes2int(byte[] bytes, int pos) {
        int result = 0;
        result = result | bytes[pos] & 255;

        for(int i = pos; i < pos + 4; ++i) {
            result <<= 8;
            result |= bytes[i] & 255;
        }

        return result;
    }

    public static byte[][] stringCol2bytesArray(Collection<String> stringCol) {
        return stringArray2bytesArray((String[])stringCol.toArray(new String[stringCol.size()]));
    }

    public static byte[][] stringArray2bytesArray(String... stringArray) {
        byte[][] bytesArray = new byte[stringArray.length][];

        for(int i = 0; i < stringArray.length; ++i) {
            bytesArray[i] = stringArray[i].getBytes();
        }

        return bytesArray;
    }

    public static List<byte[]> longCol2bytesCol(Collection<Long> longCol) {
        return longArray2bytesList((Long[])longCol.toArray(new Long[longCol.size()]));
    }

    public static byte[][] longCol2bytesArray(Collection<Long> longCol) {
        return longArray2bytesArray((Long[])longCol.toArray(new Long[longCol.size()]));
    }

    public static byte[][] longArray2bytesArray(Long... longArray) {
        return (byte[][])longArray2bytesList(longArray).toArray(new byte[longArray.length][]);
    }

    public static List<byte[]> longArray2bytesList(Long... longArray) {
        List<byte[]> bytesList = new ArrayList(longArray.length);

        for(int i = 0; i < longArray.length; ++i) {
            bytesList.add(long2bytes(longArray[i]));
        }

        return bytesList;
    }

    public static byte[][] bytesCol2bytesArray(Collection<byte[]> bytesCol) {
        byte[][] bytesArray = new byte[bytesCol.size()][];
        Iterator<byte[]> it = bytesCol.iterator();

        for(int var3 = 0; it.hasNext(); bytesArray[var3++] = (byte[])it.next()) {
        }

        return bytesArray;
    }

    public static List<String> bytesCol2stringList(Collection<byte[]> bytesCol) {
        List<String> stringList = new ArrayList(bytesCol.size());
        Iterator it = bytesCol.iterator();

        while(it.hasNext()) {
            stringList.add(new String((byte[])it.next()));
        }

        return stringList;
    }

    public static List<Long> bytesCol2longList(Collection<byte[]> bytesCol) {
        List<Long> longList = new ArrayList(bytesCol.size());
        Iterator var2 = bytesCol.iterator();

        while(var2.hasNext()) {
            byte[] bytes = (byte[])var2.next();
            longList.add(bytes2long(bytes));
        }

        return longList;
    }

    public static Set<Long> bytesCol2longSet(Collection<byte[]> bytesCol) {
        return new HashSet(bytesCol2longList(bytesCol));
    }

    public static byte[] long2bytes(long input) {
        byte[] result = new byte[8];

        for(int i = 7; i >= 0; --i) {
            result[i] = (byte)((int)(input & 255L));
            input >>= 8;
        }

        return result;
    }

    public static void main(String[] args) {
    }

    public static long bytes2long(byte[] bytes) {
        return bytes2long(bytes, 0);
    }

    public static long bytes2long(byte[] bytes, int pos) {
        long result = 0L;
        result |= (long)(bytes[pos] & 255);

        for(int i = pos; i < pos + 8; ++i) {
            result <<= 8;
            result |= (long)(bytes[i] & 255);
        }

        return result;
    }

    public static int[] bytes2int(byte[] bytes) {
        int[] results = new int[bytes.length / 4];
        int pos = 0;

        for(int i = 0; i < results.length; ++i) {
            results[i] = bytes2int(bytes, pos);
            pos += 4;
        }

        return results;
    }
}

