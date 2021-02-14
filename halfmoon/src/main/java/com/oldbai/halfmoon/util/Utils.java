package com.oldbai.halfmoon.util;

/**
 * 工具类
 */
public class Utils {
    public static int getPage(int page) {
        return page < com.oldbai.blog.utils.Constants.Page.DEFAULT_PAGE ? page : com.oldbai.blog.utils.Constants.Page.DEFAULT_PAGE;
    }

    public static int getSize(int size) {
        return size < com.oldbai.blog.utils.Constants.Page.MIN_SIZE ? com.oldbai.blog.utils.Constants.Page.MIN_SIZE : size;
    }
}
