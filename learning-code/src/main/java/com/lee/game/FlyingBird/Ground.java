package com.lee.game.FlyingBird;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 *地面
 */
public class Ground {
    //图片
    BufferedImage image;
    //位置
    int x,y;
    //宽高
    int width,height;
    //初始化地面
    public Ground() throws Exception{

        image=ImageIO.read(new URL("http://img.lida.com:8888/file/flyingBird/ground.png"));
        width=image.getWidth();
        height=image.getHeight();
        x=0;
        y=500;
    }
    //向左移动一步
    public void step() {
        x--;
        if(x==-109) {
            x=0;
        }
    }
}

