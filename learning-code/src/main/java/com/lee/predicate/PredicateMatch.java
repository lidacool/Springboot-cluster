package com.lee.predicate;

import java.util.List;

/**
 * 可以通过预先设置好的暗示(predicate) 匹配需要的暗示对象
 * <p>
 * eg:根据不同的道具类型自动匹配添加到不同的仓库
 * <p>
 * 普通道具-->添加至道具仓库
 * 召唤兽-->添加至召唤兽仓库
 * 游戏币，经验-->一般在用户名下
 * 坐骑-->坐骑仓库
 * 技能-->技能仓库
 */
public interface PredicateMatch {

    boolean test(int a);

    static PredicateMatch add(List<Integer> arr) {

        return a -> arr.contains(a);//test实现
    }

    /**
     * 默认方法可以不被实现，可以被实现类覆盖
     */
    default boolean test(){
        throw new UnsupportedOperationException("Unsupported Operation!");
    }
}
