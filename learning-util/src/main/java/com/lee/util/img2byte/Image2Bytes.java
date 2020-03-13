package com.lee.util.img2byte;

import org.apache.commons.lang3.CharEncoding;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class Image2Bytes {

    private static byte[] image2Bytes(String imgSrc) throws Exception {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes = new byte[fin.available()];
        //将文件内容写入字节数组，提供测试的case
        fin.read(bytes);

        fin.close();
        return bytes;
    }

    public static void main(String[] arg) {
        byte[] b1 = new byte[0];
        try {
            b1 = image2Bytes("/Users/hoolai/uploadImg/" + "1001.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(new String(b1));
        System.out.println("编码前的字节数组" + Arrays.toString(b1));
        //编、解码
        byte[] b2 = Base64.getEncoder().encode(b1);
        System.out.println("编码后的字节数组 = " + Arrays.toString(b2));

        try {
            String mes = new String(b2, CharEncoding.ISO_8859_1);
            System.out.println("编码后的字节数组对应的ascil码值 = " + mes);
            System.out.println("ascil码值转回编码后的字节数组 = " + Arrays.toString(mes.getBytes(CharEncoding.ISO_8859_1)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] b3 = Base64.getDecoder().decode(b2);
        System.out.println("解码后的字节数组 = " + Arrays.toString(b3));
        try {
            ByteToFile(b3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void ByteToFile(byte[] bytes)throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bi1 = ImageIO.read(bais);
        try {
            File w2 = new File("/Users/hoolai/uploadImg/007.jpg");//可以是jpg,png,gif格式
            ImageIO.write(bi1, "png", w2);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            bais.close();
        }
    }
}
