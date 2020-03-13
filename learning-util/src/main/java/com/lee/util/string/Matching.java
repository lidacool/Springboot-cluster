package com.lee.util.string;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Matching implements MatchString {

    private final Map<Character, Matching> childs = new HashMap<>();
    private String old_str;
    private String new_str;
    private boolean chinese;

    public Matching(boolean chinese) {
        this.chinese = chinese;
    }

    private static boolean isChineseCharacter(char c) {
        return (19968 <= c) && (c <= 171941);
    }

    @Override
    public void initString(String init_string, String repleace_str, final String separatorChars) {
        String[] s = StringUtils.split(init_string, separatorChars);
        for (int i = 0; i < s.length; ++i) {
            add(s[i], repleace_str);
        }
    }

    public void add(String oldString, String newString) {
        add(oldString, 0, newString);
    }

    private void add(String oldString, int index, String newString) {

        if (oldString.length() == index) {
            this.new_str = newString;
            this.old_str = oldString;
            return;
        }

        char c = oldString.charAt(index);
        Matching matching = childs.get(c);
        if (matching == null) {
            matching = new Matching(this.chinese);
            childs.put(c, matching);
        }

        matching.add(oldString, index + 1, newString);

    }

    @Override
    public void initString(Collection<String> init_string_arr, String repleace_str) {

        for (String init_string : init_string_arr) {
            add(init_string, repleace_str);
        }

    }

    @Override
    public boolean existString(String new_check_string) {
        return findWords(new_check_string, 0) != null;
    }

    private FindResultPosInfo findWords(String new_check_string, int idx) {
        for (int i = idx; i < new_check_string.length(); ++i) {
            FindResultPosInfo resultPosInfo = compareWords(new_check_string, i, 0, 0);
            if (resultPosInfo != null)
                return resultPosInfo;
        }
        return null;
    }

    public static class FindResultPosInfo {

        private Matching matching;
        private int match_pos;
        private int length;

        public FindResultPosInfo(Matching matching, int match_pos, int length) {
            this.matching = matching;
            this.match_pos = match_pos;
            this.length = length;
        }
    }

    private FindResultPosInfo compareWords(String new_check_string, int index, int ignoreCount, int deep) {

        //如果是最长匹配
        if (this.old_str != null && childs.size() == 0) {
            int n = this.old_str.length() + ignoreCount;
            return new FindResultPosInfo(this, index - n, n);
        }

        if (index >= new_check_string.length()) {
            return null;
        }

        //跳过非中文 < >
        char c = new_check_string.charAt(index);
        if (this.chinese && deep > 0 && !isChineseCharacter(c) && c != '<' && c != '>') {
            int i = 0;

            for (; !isChineseCharacter(c) && c != '<' && c != '>' && index + i < new_check_string.length(); ++i)
                c = new_check_string.charAt(index + i);

            if (i > 0) {
//                --i;
                index += i;
                ignoreCount += i;
            }
        }

        Matching matching = childs.get(c);
        if (matching == null) {
            return null;
        }

        FindResultPosInfo resultPosInfo = matching.compareWords(new_check_string, index + 1, ignoreCount, deep + 1);

        if (resultPosInfo == null && matching.old_str != null) {
            int n = matching.old_str.length() + ignoreCount;
            return new FindResultPosInfo(matching, index + 1 - n, n);
        }

        return resultPosInfo;

    }

    @Override
    public String repleace(String s) {
        FindResultPosInfo wi = findWords(s, 0); // 查找第一个替换的位置
        if (wi == null)
            return s;
        StringBuilder sb = new StringBuilder(s.length());
        int writen = 0;
        for (; wi != null && wi.match_pos < s.length(); ) {
            sb.append(s, writen, wi.match_pos); // append 原字符串不需替换部分
            sb.append(wi.matching.new_str); // append 新字符串
            writen = wi.match_pos + wi.length; // 忽略原字符串需要替换部分
            wi = findWords(s, writen); // 查找下一个替换位置
        }
        sb.append(s, writen, s.length()); // 替换剩下的原字符串
        return sb.toString();
    }

}
