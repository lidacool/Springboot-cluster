package com.lee.readJson.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.lee.util.jredis.JRedis;
import com.lee.util.json.JsonUtil;
import com.lee.util.string.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

@Component
public abstract class JsonData<T extends JsonConfig> {

    public final static String VERSION_KEY = "versionMap";
    private final static String CONFIG_KEY = "jsonConfig";

    protected final static String BASE_PATH = "json/";
    protected final ListMultimap<Integer, T> configMap = ArrayListMultimap.create();
    protected final Class<T> clazz;
    protected final String configName;
    private static final Map<String, JsonData> jsonNameKeyMap = new HashMap<>();
    protected int version;

    @Autowired
    private JRedis jRedis;

    public String getConfigKey(String configName) {
        return CONFIG_KEY + ":" + configName;
    }

    public JsonData() {
        clazz = (Class<T>) Generic.parse(this.getClass(), JsonData.class).getByName("T");
        ConfigFile configFile = getClass().getAnnotation(ConfigFile.class);
        if (configFile == null) {
            throw new RuntimeException("not add anno class:" + ConfigFile.class.getName());
        }
        configName = configFile.value();

        jsonNameKeyMap.put(configName, this);
    }

    /**
     * PostConstruct 注释用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化。
     * 此方法必须在将类放入服务之前调用。支持依赖关系注入的所有类都必须支持此注释。
     * 即使类没有请求注入任何资源，用 PostConstruct 注释的方法也必须被调用。
     * */
    @PostConstruct
    public void init() {
        init(readVersion());
    }

    public void init(int newVersion) {
        String jsonStr = readJsonStr(configName);
        initJson(jsonStr);
        version = newVersion;
    }

    public void initJson(String jsonStr) {
        configMap.clear();
        Map<String,T> jsonMap = JsonUtil.fromJSONToMap(jsonStr,clazz);

        for (Entry<String, T> entry:jsonMap.entrySet()){

            int id = Integer.parseInt(entry.getKey());
            Assert.isTrue(!configMap.containsKey(id), "存在相同的id=" + id);
            T config = entry.getValue();
            config.setId(id);
            configMap.put(id,config);
            config.check(configMap);
        }
    }

    protected String readJsonStr(String configName){
        String configKey = getConfigKey(configName);
        byte[] bytes = jRedis.get(configKey);
        String jsonStr;
        if (bytes == null) {
            jsonStr = readLocalJsonStr(configName);
            if (StringUtil.isEmpty(jsonStr)) {
                throw new RuntimeException("未在本地配置文件里找到configKey=" + configKey);
            }
            jRedis.set(configKey, StringUtil.encode(jsonStr));
        } else {
            jsonStr = StringUtil.encode(bytes);
        }
        return jsonStr;
    }

    protected String readLocalJsonStr(String configName){
        String path = BASE_PATH + configName;

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream(path);
        if (input == null) {
            throw new IllegalArgumentException("找不到配置文件" + path);
        }
        String jsonStr = null;
        try {
            jsonStr = IOUtils.toString(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public int readVersion() {
        String configKey=getConfigKey(configName);
        byte[] bytes = jRedis.hget(VERSION_KEY, configKey);
        int version;
        if (bytes == null) {
            version = jRedis.hincr(VERSION_KEY, configKey).intValue();
        } else {
            version = Integer.parseInt(StringUtil.encode(bytes));
        }
        return version;
    }

    public T getConfig(int id){
        return (T) configMap.get(id);
    }

    public int size(){
        return configMap.size();
    }

    public Collection<T> values() {
        return configMap.values();
    }

    public Set<Integer> ids() {
        return Sets.newHashSet(configMap.keySet());
    }

    public int max_id(){
        return Collections.max(ids());
    }

    public int min_id(){
        return Collections.min(ids());
    }

    public boolean isExpire(int version){
        return this.version<version;
    }

    public static JsonData findJsonData(String configName){
        return jsonNameKeyMap.get(configName);
    }

   /**手动更新
     JsonData.forEach((configFile, jsonData) -> {
			int version = jsonData.readVersion();
			if (jsonData.isExpire(version)) {
				jsonData.init(version);
			}
		});*/
    public static void foreach(BiConsumer<String,JsonData> consumer){
        Objects.requireNonNull(consumer);
        jsonNameKeyMap.forEach(consumer);
    }

}
