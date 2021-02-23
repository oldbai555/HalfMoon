package com.oldbai.halfmoon.vo;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchResult implements Serializable {
    //    blog_content,blog_create_time,blog_labels,blog_url,blog_title,blog_view_count
    @Field("id")
    private String id;
    @Field("blog_content")
    private String blogContent;
    @Field("blog_create_timme")
    private Date blogCreateTime;
    @Field("blog_labels")
    private String blogLabels;
    @Field("blog_url")
    private String blogUrl;
    @Field("blog_title")
    private String blogTitle;
    @Field("blog_view_count")
    private int blogViewCount;
}
