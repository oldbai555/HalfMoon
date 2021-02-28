package com.oldbai.halfmoon.controller.portal;

import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ArticleService;
import com.oldbai.halfmoon.solr.SolrService;
import com.oldbai.halfmoon.solr.SolrTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/halfmoon/portal/search")
@CrossOrigin
@RestController
@Api(description = "门户-搜索")
public class SearchPortalController {

    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrTestService solrTestService;

    @Autowired
    private ArticleService articleService;
    
    @ApiOperation("搜索功能")
    @GetMapping("/solr/select")
    public ResponseResult solrSelectAllTest(@RequestParam("keyword") String keyword,
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam(value = "categoryId", required = false) String categoryId,
                                            @RequestParam(value = "sort", required = false) Integer sort) {

        return solrService.doSearch(keyword, page, size, categoryId, sort);
    }
}
