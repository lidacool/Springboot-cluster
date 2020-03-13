package com.lee.controllerWeb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.lee.readJson.util.JsonData;
import com.lee.util.jredis.JRedis;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/uploadController")
public class UploadController {


    /**
     * 此处注入JRedis如果名字不是jRedis会报错（同一个Type找到了多个bean）
     *
     * @Autowired 注解的注入规则：
     * 经过一些代码的的测试，Autowired默认先按Type，如果同一个Type找到多个bean，则，又按照Name方式比对，如果还有多个，则报出异常。
     */
    @Autowired
    private JRedis jRedis;


    /**
     * @ResponseBody 用于对方法进行注释，
     * 表示该方法的返回的结果将直接写入 HTTP 响应正文（Http Response Body）中。
     * 我之前控制层代码里有返回值，类型为 CommonReturnType 的通用返回值类型，
     * 我把返回值去掉后，方法改为 void，无任何返回值， 虽然在前端的调试中，各种数据依然正常，
     * 但是前端页面获取不到控制层方法的返回值，在 response body 中无内容，
     * 导致点击前端页面的按钮不会有任何现象发生，虽然此时后端逻辑正确无误，数据库入库操作都实现了。
     * 所以我这里必须给该方法一个返回值，且要通过注解形式将返回值加到 Http response body 中。
     * 如果没有这个注解，就会抛出上面的 responseText 异常信息。
     */
    @ResponseBody
    @RequestMapping(value = "/upload")
    public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile[] files) {

        System.out.println("开始处理上传文件...");

        Map<String, String> fileMap = new HashMap<>();
        Map<String, String> errorMap = new HashMap<>();
//        if (files == null || files.length == 0) {
//            throw new BusinessException(-1,"files is null!");
//        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            // String fileName = new Date().getTime()+".jpg";
            System.out.println("fileName=" + fileName);

            int jsonIndex = fileName.lastIndexOf(".json");
            if (jsonIndex == -1) {
                System.out.println(fileName + " 不是json文件不处理...");
                continue;
            }

            byte[] bytes = null;
            try {
                InputStream input = file.getInputStream();
                bytes = IOUtils.toByteArray(input);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            String jsonStr = new String(bytes, Charset.forName("UTF-8"));
            System.out.println(jsonStr);

            String key = JsonData.VERSION_KEY;
            JsonData jsonData = JsonData.findJsonData(fileName);
            if (jsonData == null) {
                System.out.println(fileName + " 不是游戏内json配置文件不处理...");
                errorMap.put(fileName, "不是游戏内json配置文件不处理...");
                continue;
            }

            String configKey = jsonData.getConfigKey(fileName);
            try {
                jsonData.initJson(jsonStr);
            } catch (Exception e) {
                errorMap.put(fileName, e.getMessage());
                e.printStackTrace();
                continue;
            }
            jRedis.syncPipeline(pipeline -> {
                        pipeline.set(configKey, jsonStr);
                        pipeline.hincrBy(key, configKey, 1);
                    }
                    , key, configKey);

            fileMap.put(fileName, jsonStr);
        }

        // String path = request.getSession().getServletContext().getRealPath("upload");
        // System.out.println(path);
        // File targetFile = new File(path, fileName);
        // if (!targetFile.exists()) {
        // targetFile.mkdirs();
        // }
        //
        // // 保存
        // try {
        // file.transferTo(targetFile);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // model.addAttribute("fileUrl", request.getContextPath() + "/upload/" +
        // fileName);
        Map<String, Object> result = new HashMap<>();
        result.put("fileMap", fileMap);
        result.put("errorMap", errorMap);
        // attr.addAttribute("fileMap", fileMap);
        // attr.addFlashAttribute("fileMap", fileMap);

        System.out.println("结束处理上传文件...");


        return result;
    }

}
