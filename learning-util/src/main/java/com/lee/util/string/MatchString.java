package com.lee.util.string;

import java.util.Collection;

public interface MatchString {

    /**要求使用者以 自定义一个分隔符separatorChars拆分*/
    void initString(String init_string, String repleace_str, final String separatorChars);

    void initString(Collection<String> init_string_arr, String repleace_str);

    boolean existString(String new_check_string);

    String repleace(String new_string);

}
