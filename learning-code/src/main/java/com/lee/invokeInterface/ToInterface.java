package com.lee.invokeInterface;

import com.lee.util.random.RandomUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 调用接口
 * 接口url访问
 */

public class ToInterface {
    /**
     * 调用对方接口方法
     *
     * @param path 对方或第三方提供的路径
     * @param data 向对方或第三方发送的数据，大多数情况下给对方发送JSON数据让对方解析
     */
    public static void interfaceUtil(String path, String data, String oneHead) {
        try {
            URL url = new URL(path);
            //打开和url之间的连接

            //参数中文乱码处理
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;
            //请求方式
            conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");


            //设置响应头
            conn.setRequestProperty("userId", oneHead);
            conn.setRequestProperty("marketId", "1");
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数即数据
            out.print(data);
            //缓冲数据
            out.flush();
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            System.out.println("完整结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void excu(int id) {
        order(id);
    }

    private static void order(int id) {
        String data = "{\"mod\":\"festivalActivityService\",\"do\":\"friendAsisst\",\"p\":[1000001]}";
//        interfaceUtil("https://g.nuojuekeji.com/online/dispatch.sl", data);
        interfaceUtil("http://10.2.80.130:9001/dispatch.sl", data, id + "");

    }

    public static void main(String[] args) {
//        String data = "{\"mod\":\"marketService\",\"do\":\"marketDecorate\",\"p\":[[{\"oldKey\": 14019, \"isReversal\": false, \"newKey\": 15019, \"xmlId\": 2033303, \"oldReversal\": false}], [], 2004501, 0,192]}";

        String data = "{\"mod\":\"orderService\",\"do\":\"getOrderInfo\",\"p\":[]}";

        startRunable();
//            interfaceUtil("http://10.2.80.130:9001/dispatch.sl", data);

//            interfaceUtil("https://g.nuojuekeji.com/online/dispatch.sl", data);
//            interfaceUtil("http://10.2.0.202/dispatch.sl",data);

    }

    public static void startRunable() {
        Runnable runnable = () -> excu2();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MILLISECONDS);
        Runnable runnable3 = () -> excu3();
        ScheduledExecutorService executor3 = Executors.newScheduledThreadPool(1);
        executor3.scheduleAtFixedRate(runnable3, 10, 10, TimeUnit.MILLISECONDS);
    }

    public static void excu2() {
        String data = "{\"mod\":\"festivalActivityService\",\"do\":\"getActivityInfo\",\"p\":[]}";
        interfaceUtil("http://10.2.80.130:9001/dispatch.sl", data, 1000001 + "");
    }

    public static void excu3() {
        String data = "{\"mod\":\"festivalActivityService\",\"do\":\"getFriendInfo\",\"p\":[1000001]}";
        interfaceUtil("http://10.2.80.130:9001/dispatch.sl", data, 1000000+ RandomUtil.randomIntCon(2,4) + "");
    }
}
