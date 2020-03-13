package com.lee.util.string;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

public class StringUtil {
    public static final String ELEMENT_SEPARATOR_COMMA = ",";
    public static final String ELEMENT_SEPARATOR_DOT = ".";
    public static final String ELEMENT_SEPARATOR_SEMICOLON = ":";
    public static final String ELEMENT_SEPARATOR_UNDERLINE = "_";
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static Map<Class<?>, Method> valueOfMap = new HashMap();
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static String HMAC_SHA256_ALGORITHM;

    public StringUtil() {
    }

    public static String addElement(String x, String element) {
        return x != null && !x.isEmpty() ? x + "," + element : element;
    }

    public static String removeElement(String x, String element) {
        if (x == null) {
            return x;
        } else if (x.equals(element)) {
            return "";
        } else if (firstElement(x).equals(element)) {
            return x.substring(element.length() + 1);
        } else {
            return lastElement(x).equals(element) ? x.substring(0, x.length() - element.length() - 1) : x.replace("," + element + ",", ",");
        }
    }

    public static String removeFirst(String x) {
        int index = x.indexOf(",");
        return index == -1 ? "" : x.substring(index + 1);
    }

    public static int elementCount(String x) {
        if (isEmpty(x)) {
            return 0;
        } else {
            int count = 0;

            for(int index = 0; index != -1; ++count) {
                index = x.indexOf(",", index + 1);
            }

            return count;
        }
    }

    public static boolean existsElement(String x, String element) {
        if (x == null) {
            return false;
        } else if (firstElement(x).equals(element)) {
            return true;
        } else if (lastElement(x).equals(element)) {
            return true;
        } else {
            return x.indexOf("," + element + ",") >= 0;
        }
    }

    public static Map<Integer, Integer> splitToIntMap(String src) {
        return splitToIntMap(src, ",", ";");
    }

    public static Map<Integer, Integer> splitToIntMap(String src, String major, String minor) {
        Map<Integer, Integer> map = new HashMap();
        if (!isEmpty(src)) {
            String[] tmps = StringUtils.split(src, minor);
            String[] var5 = tmps;
            int var6 = tmps.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String tmp = var5[var7];
                String[] xx = StringUtils.split(tmp, major);
                map.put(Integer.parseInt(xx[0]), Integer.parseInt(xx[1]));
            }
        }

        return map;
    }

    public static <T> List<T> split(String x, Class<T> clazz) {
        return split(x, clazz, ",");
    }

    public static <T> List<T> split(String x, Class<T> clazz, String elementSeparator) {
        List<T> members = new ArrayList();
        if (isEmpty(x)) {
            return members;
        } else {
            int beginIndex = 0;

            while(beginIndex != -1) {
                int endIndex = x.indexOf(elementSeparator, beginIndex);
                if (endIndex != -1) {
                    members.add(valueOf(x.substring(beginIndex, endIndex), clazz));
                    beginIndex = endIndex + 1;
                } else {
                    members.add(valueOf(x.substring(beginIndex), clazz));
                    beginIndex = -1;
                }
            }

            return members;
        }
    }

    public static int[] splitAsInt(String x) {
        return splitAsInt(x, ",");
    }

    public static int[] splitAsInt(String x, String splitStr) {
        if (isEmpty(x)) {
            return new int[0];
        } else {
            String[] split = x.split(splitStr);
            return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
        }
    }

    public static String firstElement(String x) {
        int index = x.indexOf(",");
        return index == -1 ? x : x.substring(0, index);
    }

    public static String lastElement(String x) {
        int index = x.lastIndexOf(",");
        return index == -1 ? x : x.substring(index + 1);
    }

    public static List<Long> randomMemberByLong(int randomNum, long exceptId, String members) {
        int maxTryCount = randomNum * 5;
        Random r = new Random();
        List<Long> randomMembers = new ArrayList();
        if (isEmpty(members)) {
            return randomMembers;
        } else {
            int membersSize = 0;
            int len = members.length();
            int tryCount = 0;
            int beginIndex;
            int endIndex;
            long randomMember;
            if (len < 300) {
                for(beginIndex = 0; tryCount++ < maxTryCount && membersSize < randomNum; beginIndex = endIndex + 1) {
                    endIndex = members.indexOf(",", beginIndex);
                    if (endIndex == -1) {
                        randomMember = Long.parseLong(members.substring(beginIndex));
                        if (randomMember != exceptId) {
                            randomMembers.add(randomMember);
                        }

                        return randomMembers;
                    }

                    randomMember = Long.parseLong(members.substring(beginIndex, endIndex));
                    if (randomMember != exceptId) {
                        randomMembers.add(randomMember);
                        ++membersSize;
                    }
                }
            } else {
                while(tryCount++ < maxTryCount && membersSize < randomNum) {
                    beginIndex = r.nextInt(len);
                    beginIndex = members.indexOf(",", beginIndex);
                    if (beginIndex != -1) {
                        endIndex = members.indexOf(",", beginIndex + 1);
                        if (endIndex != -1) {
                            randomMember = Long.parseLong(members.substring(beginIndex + 1, endIndex));
                            if (randomMember != exceptId && !randomMembers.contains(randomMember)) {
                                randomMembers.add(randomMember);
                                ++membersSize;
                            }
                        }
                    }
                }
            }

            return randomMembers;
        }
    }

    public static <T> T valueOf(String value, Class<T> clazz) {
        if (value == null) {
            return null;
        } else {
            Method valueOfMethod = (Method)valueOfMap.get(clazz);
            if (valueOfMethod == null) {
                return (T) value;
            } else {
                try {
                    return (T) valueOfMethod.invoke((Object)null, value);
                } catch (Exception var4) {
                    throw new RuntimeException(var4);
                }
            }
        }
    }

    public static boolean isNumber(String x) {
        int len;
        if (x != null && (len = x.length()) != 0) {
            char temp;
            do {
                if (len-- <= 0) {
                    return true;
                }

                temp = x.charAt(len);
            } while(temp <= '9' && temp >= '0');

            return false;
        } else {
            return false;
        }
    }

    public static int asciiLength(String s) {
        int len = 0;
        char[] var2 = s.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int c = var2[var4];
            if ((c < 1 || c > '~') && ('｠' > c || c > 'ﾟ')) {
                len += 2;
            } else {
                ++len;
            }
        }

        return len;
    }

    /** @deprecated */
    @Deprecated
    public static String asciiSubstring(String s, int beginIndex, int endIndex) {
        char[] destChars = new char[endIndex - beginIndex];
        char[] chars = s.toCharArray();
        int i = beginIndex;

        for(int j = 0; i < endIndex; ++j) {
            destChars[j] = chars[i];
            ++i;
        }

        return new String(destChars);
    }

    public static final int sumASCII(String s) {
        int sumASCII = 0;
        char[] var2 = s.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            sumASCII += c;
        }

        return sumASCII;
    }

    public static void main(String[] args) {
        String a = "abcd1234";
        System.out.println(a.length());
        System.out.println(asciiLength(a));
        System.out.println(a.substring(0, 6));
        System.out.println(asciiSubstring(a, 0, 6));
        a = "ab123测试中文测";
        System.out.println(a.length());
        System.out.println(asciiLength(a));
        System.out.println(a.substring(0, 6));
        System.out.println(asciiSubstring(a, 0, 6));
    }

    public static int bytelength(String data) {
        try {
            return data.getBytes("GBK").length;
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String encryptToMd5(String x) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(x.getBytes("UTF-8"));
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }

        byte[] byteArray = messageDigest.digest();
        StringBuilder md5SB = new StringBuilder();

        for(int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5SB.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5SB.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5SB.toString();
    }

    public static String encryptNumber(long number) {
        List<Integer> arr = convertNumberList(number);
        int[] newArr = convertByMapping(arr);
        swapPerTowElement(newArr);
        return arrayToString(newArr);
    }

    private static void swapPerTowElement(int[] arr) {
        int len = arr.length;

        for(int i = 1; i < len; i += 2) {
            swap(arr, i, i - 1);
        }

    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    private static String arrayToString(int[] arr) {
        int len = arr.length;
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < len; ++i) {
            sb.append(arr[i]);
        }

        return sb.toString();
    }

    private static int[] convertByMapping(List<Integer> arr) {
        int len = arr.size();
        int[] newArr = new int[len];

        int m;
        for(int i = len; i-- > 0; newArr[len - 1 - i] = m) {
            int n = (Integer)arr.get(i);
            m = 9 - n;
        }

        return newArr;
    }

    private static List<Integer> convertNumberList(long number) {
        ArrayList arr = new ArrayList();

        do {
            int t = (int)(number % 10L);
            arr.add(t);
            number /= 10L;
        } while(number > 0L);

        return arr;
    }

    public static List<String> randomMembers(int randomNum, String exceptMember, String members) {
        int maxTryCount = randomNum * 5;
        List<String> randomMembers = new ArrayList();
        if (isEmpty(members)) {
            return randomMembers;
        } else {
            int membersSize = 0;
            int len = members.length();
            int tryCount = 0;
            int endIndex;
            if (len < 300) {
                for(int beginIndex = 0; tryCount++ < maxTryCount && membersSize < randomNum; beginIndex = endIndex + 1) {
                    endIndex = members.indexOf(",", beginIndex);
                    String randomMember;
                    if (endIndex == -1) {
                        randomMember = members.substring(beginIndex);
                        if (!randomMember.equals(exceptMember)) {
                            randomMembers.add(randomMember);
                        }

                        return randomMembers;
                    }

                    randomMember = members.substring(beginIndex, endIndex);
                    if (!randomMember.equals(exceptMember)) {
                        randomMembers.add(randomMember);
                        ++membersSize;
                    }
                }
            } else {
                Random r = new Random();

                while(tryCount++ < maxTryCount && membersSize < randomNum) {
                    endIndex = r.nextInt(len);
                    endIndex = members.indexOf(",", endIndex);
                    if (endIndex != -1) {
                         endIndex = members.indexOf(",", endIndex + 1);
                        if (endIndex != -1) {
                            String randomMember = members.substring(endIndex + 1, endIndex);
                            if (!randomMember.equals(exceptMember) && !randomMembers.contains(randomMember)) {
                                randomMembers.add(randomMember);
                                ++membersSize;
                            }
                        }
                    }
                }
            }

            return randomMembers;
        }
    }

    public static List<String> getFistMembers(int num, String exceptMember, String members) {
        List<String> rMembers = new ArrayList();
        int beginIndex = 0;

        int endIndex;
        for(int membersSize = 0; membersSize < num; beginIndex = endIndex + 1) {
            endIndex = members.indexOf(",", beginIndex);
            String randomMember;
            if (endIndex == -1) {
                randomMember = members.substring(beginIndex);
                if (randomMember != exceptMember) {
                    rMembers.add(randomMember);
                }

                return rMembers;
            }

            randomMember = members.substring(beginIndex, endIndex);
            if (randomMember != exceptMember) {
                rMembers.add(randomMember);
                ++membersSize;
            }
        }

        return rMembers;
    }

    public static String join(List<String> strList) {
        return join(strList, ",");
    }

    public static String join(List<String> strList, String separator) {
        if (strList != null && !strList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int size = strList.size();

            for(int i = 0; i < size; ++i) {
                String str = (String)strList.get(i);
                sb.append(str);
                if (i != size - 1) {
                    sb.append(separator);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static boolean isNotEmpty(String x) {
        return !isEmpty(x);
    }

    public static boolean isEmpty(String x) {
        int len;
        if (x != null && (len = x.length()) != 0) {
            do {
                if (len-- <= 0) {
                    return true;
                }
            } while(Character.isWhitespace(x.charAt(len)));

            return false;
        } else {
            return true;
        }
    }

    public static String trim(String x) {
        return x == null ? null : x.trim();
    }

    public static String encrypt(String x) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(x.getBytes("UTF-8"));
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }

        byte[] byteArray = messageDigest.digest();
        StringBuilder md5SB = new StringBuilder();

        for(int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5SB.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5SB.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5SB.toString();
    }

    public static String hmacSHA1(String key, String data) {
        BigInteger hash = new BigInteger(1, hmacSHA1ToBytes(key, data));
        String hmac = hash.toString(16);
        if (hmac.length() % 2 != 0) {
            hmac = "0" + hmac;
        }

        return hmac;
    }

    public static byte[] hmacSHA1ToBytes(String key, String data) {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return mac.doFinal(data.getBytes());
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }

    public static String hmacsha256(String key, String data) {
        try {
            return new String(Base64.encodeBase64(hmacsha256ToBytes(key, data)), DEFAULT_CHARSET);
        } catch (Exception var3) {
            return null;
        }
    }

    public static String decodeBase64Url(String context) {
        context = fixPaddingBase64String(fixBase64String(context));
        return new String(Base64.decodeBase64(context.getBytes()));
    }

    private static String fixPaddingBase64String(String base64) {
        int paddingLength = base64.length() % 4 == 0 ? 0 : 4 - base64.length() % 4;

        for(int i = 0; i < paddingLength; ++i) {
            base64 = base64 + "=";
        }

        return base64;
    }

    private static String fixBase64String(String context) {
        return context.replaceAll("-", "+").replaceAll("_", "/");
    }

    public static String encodeBase64Url(String context) {
        return (new String(Base64.encodeBase64(context.getBytes()))).replaceAll("\\+", "-").replaceAll("/", "_").replaceAll("=", "");
    }

    public static String encodeBase64(String txt) {
        return new String(Base64.encodeBase64(txt.getBytes()));
    }

    public static String decodeBase64(String txt) {
        return new String(Base64.decodeBase64(txt.getBytes()));
    }

    public static byte[] hmacsha256ToBytes(String key, String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);
            return mac.doFinal(data.getBytes());
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static String hmacSHA1ToBase64(String key, String data) {
        byte[] encodeBase64 = Base64.encodeBase64(hmacSHA1ToBytes(key, data));
        return new String(encodeBase64, DEFAULT_CHARSET);
    }

    public static List<Integer> str2IntList(String str) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] formationStrs = str.split(",");
            List<Integer> formation = new ArrayList(formationStrs.length);
            String[] var3 = formationStrs;
            int var4 = formationStrs.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String s = var3[var5];
                formation.add(Integer.parseInt(s.trim()));
            }

            return formation;
        }
    }

    public static String intList2str(List<Integer> intList) {
        StringBuilder sb = new StringBuilder();
        int size = intList.size();

        for(int i = 0; i < size; ++i) {
            int value = (Integer)intList.get(i);
            sb.append(value);
            if (i != size - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static List<Long> str2LongList(String str) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] formationStrs = str.split(",");
            List<Long> formation = new ArrayList(formationStrs.length);
            String[] var3 = formationStrs;
            int var4 = formationStrs.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String s = var3[var5];
                formation.add(Long.parseLong(s.trim()));
            }

            return formation;
        }
    }

    public static byte[] encode(String str) {
        if (str == null) {
            throw new NullPointerException();
        } else {
            return str.getBytes(DEFAULT_CHARSET);
        }
    }

    public static String encode(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException();
        } else {
            return new String(bytes, DEFAULT_CHARSET);
        }
    }

    public static List<int[]> splitAsIntList(String x) {
        List<String> split = split(x, String.class, ";");
        return (List)split.stream().map(StringUtil::splitAsInt).collect(Collectors.toList());
    }

    public static int[][] splitAsInts(String s) {
        if (isEmpty(s)) {
            return new int[0][];
        } else {
            String[] split = s.split(";");
            int[][] result = new int[split.length][];

            for(int i = 0; i < split.length; ++i) {
                result[i] = splitAsInt(split[i]);
            }

            return result;
        }
    }

    public static String format(String pattern, Object arg1, Object arg2) {
        return StringFormatter.format(pattern, arg1, arg2);
    }

    public static String format(String pattern, Object[] argArray) {
        return StringFormatter.arrayFormat(pattern, argArray);
    }

    public static String format(String pattern, Object arg) {
        return StringFormatter.format(pattern, arg);
    }

    static {
        try {
            Method l = Long.class.getMethod("valueOf", String.class);
            valueOfMap.put(Long.class, l);
            valueOfMap.put(Long.TYPE, l);
            Method i = Integer.class.getMethod("valueOf", String.class);
            valueOfMap.put(Integer.class, i);
            valueOfMap.put(Integer.TYPE, i);
            Method f = Float.class.getMethod("valueOf", String.class);
            valueOfMap.put(Float.class, f);
            valueOfMap.put(Float.TYPE, f);
            Method d = Double.class.getMethod("valueOf", String.class);
            valueOfMap.put(Double.class, d);
            valueOfMap.put(Double.TYPE, d);
        } catch (SecurityException var4) {
            var4.printStackTrace();
        } catch (NoSuchMethodException var5) {
            var5.printStackTrace();
        }

        HMAC_SHA256_ALGORITHM = "HMACSHA256";
    }
}
