package com.oldbai.halfmoon.util;

/**
 * 工具类
 */
public class Utils {
    public static int getPage(int page) {
        return page < Constants.Page.DEFAULT_PAGE ? Constants.Page.DEFAULT_PAGE : page;
    }

    public static int getSize(int size) {
        return size < Constants.Page.MIN_SIZE ? Constants.Page.MIN_SIZE : size;
    }
}
