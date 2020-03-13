package com.lee;

import com.lee.agent.Agent;
import com.lee.agent.ClassUtil;
import com.lee.agent.Scanner;
import com.lee.agent.UserAgent;
import com.lee.annotation.Person;
import com.lee.util.jredis.JRedis;
import com.lee.util.jredis.JRedisFactory;
import com.lee.util.json.JsonUtil;
import com.lee.util.string.MatchString;
import com.lee.util.string.Matching;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Spring Boot使用Jackson作为默认库，将Java对象序列化和反序列化为JSON。如果 在应用程序中添加“ spring-boot-starter ” ，
 * 它将包含在您的类路径中。这很棒，但有时您可能希望使用其他API，而不是Spring Boot自动配置中可用的API 。
 * eg:@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
 * 或者：
 * <dependency>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-web</artifactId>
 * // Exclude the default Jackson dependency
 * <exclusions>
 * <exclusion>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-json</artifactId>
 * </exclusion>
 * </exclusions>
 * </dependency>
 * <p>
 * Spring Boot的主要优点：
 * 为所有Spring开发者更快的入门
 * 开箱即用，提供各种默认配置来简化项目配置
 * 内嵌式容器简化web项目
 * 没有冗余代码生成和xml配置的要求
 * 尽可能的根据项目依赖来自动配置Spring框架。
 * 提供可以直接在生产环境中使用的功能，如性能指标，应用信息和应用健康检查。
 * Spring Boot的缺点
 * 依赖太多，随便的一个Spring Boot应用都有好几十M
 * 缺少服务的注册和发现等解决方案，可以结合springcloud的组件使用。
 * 缺少监控集成方案、安全管理方案（有但简单，满足不了生产的指标）
 */

//@MapperScan(value = {"com.lee"})//多项目mapper扫描, "com.lee.user.mapper"
@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
public class Application {

    public static void main(String args[]) {
        try {
            //*URLClassLoader用于加载其他项目的类文件可供url访问*//*
            processUsageClassPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<String> paths = getPaths();
        ClassUtil.processAnnoClasses("com.lee.*;*-SNAPSHOT.jar", paths, UserAgent.class, Agent.class);
        new SpringApplicationBuilder()
                .sources(Application.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    private static Set<String> getPaths() {
        String loaderPath = System.getProperty("loader.path");// jars dir
        if (loaderPath != null) {
            Set<String> pathes = new HashSet<>();
            File[] jarFiles = new File(loaderPath).listFiles();
            for (File jarFile : jarFiles) {
                pathes.add(jarFile.getAbsolutePath());
            }
            return pathes;
        }
        return Collections.emptySet();
    }

    private static void processUsageClassPath() throws Exception {
        String path = Scanner.fromURIPath(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (Files.isDirectory(Paths.get(path))) {
            Path xpath = Paths.get(path).getParent().getParent().getParent();// project-dir/target/classes
            List<URL> xurls = new ArrayList<>();
            for (File file : xpath.toFile().listFiles()) {
                if (file.isDirectory()&& file.getName().startsWith("learning")) {
                    xurls.add(Paths.get(file.getPath(), "target/classes").toUri().toURL());
                }
            }
            if (!xurls.isEmpty()) {
                URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Class<?> urlClass = URLClassLoader.class;
                Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
                method.setAccessible(true);
                for (Object url : xurls.toArray())
                    method.invoke(urlClassLoader, url);
            }
        }
    }

    private static void testJsonParse() {
        String testStr = "{\"name\":\"司马懿\",\"age\":12,\"sex\":\"女\"}";
        Person person = JsonUtil.fromJSON(testStr, Person.class);
        System.out.println(person.toString());

        String testStr2 = "[11,22,33,44]";//[]表示集合 {}表示一个对象  ,表示对象隔开  \"转义对象属性及值
        List<Integer> list = JsonUtil.fromJSONToList(testStr2, Integer.class);
        System.out.println(list.indexOf(44));

        String testStr3 = "[{\"name\":\"李岭左\",\"age\":12,\"sex\":\"男\"},{\"name\":\"司马懿\",\"age\":12,\"sex\":\"女\"}]";
        List<Person> persons = JsonUtil.fromJSONToList(testStr3, Person.class);
        System.out.println(persons.size());
        System.out.println(persons.get(0).toString());
    }

    private static void testJedis() {

        String redisMasterAddrs = "node1:127.0.0.1:6379";
        String redisSlaveAddrs = "127.0.0.1:6379";

        JRedis jRedis = JRedisFactory.createJredis(redisMasterAddrs, redisSlaveAddrs);

//        jRedis.set("jredistest", "jredistest".getBytes());
//        System.out.println(new String(jRedis.get("jredistest")));
//        jRedis.hset("lingzuo".getBytes(), "testhash1".getBytes(), BitUtil.long2bytes(123l));
//        System.out.println(BitUtil.bytes2long(jRedis.hget("lingzuo", "testhash1".getBytes())));

        jRedis.zadd("hh", 1, "h1".getBytes());
        System.out.println(jRedis.zrank("hh", "h2".getBytes()));
        System.out.println(jRedis.zrank("hh", "h1".getBytes()));

    }

//    public static void main(String s[]){
//        testJedis();
//    }

    private static void testMatchString() {
        MatchString matchString = new Matching(true);

        matchString.initString("史进,鲁达,郑屠", "***", ",");

        String new_s = "史进结识了渭州经略府提辖鲁达，二人来到酒楼饮酒。饮酒正酣，忽然隔壁传来啼哭声。鲁达顿生性鲁莽而又素好行侠仗义，叫酒保将啼哭之人带来。金氏父女被带到，女儿哭着说：因到渭州投亲无着，状元桥肉铺的郑屠乘人之危，要强娶小女为妾，今被赶出，那郑屠反要我父女给他银钱。鲁达听后大怒，决心惩治郑屠。";
        System.out.println(matchString.existString("史进"));
        System.out.println(matchString.repleace(new_s));

    }
}
