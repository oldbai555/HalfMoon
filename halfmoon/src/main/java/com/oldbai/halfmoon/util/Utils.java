package com.oldbai.halfmoon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工具类
 */
public class Utils {
    //打散标签
    public static List<String> noLabelsArr(String labels) {

        List<String> labelList = new ArrayList<>();
        if (!labels.contains("-")) {
            labelList.add(labels);
        } else {
            labelList.addAll(Arrays.asList(labels.split("-")));
        }

        return labelList;
    }


    public static int getPage(int page) {
        return page < Constants.Page.DEFAULT_PAGE ? Constants.Page.DEFAULT_PAGE : page;
    }

    public static int getSize(int size) {
        return size < Constants.Page.MIN_SIZE ? Constants.Page.MIN_SIZE : size;
    }
}
