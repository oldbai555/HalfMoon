package com.oldbai.halfmoon.solr;


import com.oldbai.halfmoon.response.ResponseResult;

public interface SolrService {
    ResponseResult doSearch(String keyword, int page, int size, String categoryId, Integer sort);
}
