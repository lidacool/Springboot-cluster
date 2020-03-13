package com.lee.JPython;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JPyDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Process proc;
        try {
            String[] cmdArr = new String[] {"python", "/Users/hoolai/ideaWorkSpace/py/BullshitGenerator/bullshit.py"};
            proc = Runtime.getRuntime().exec(cmdArr);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
