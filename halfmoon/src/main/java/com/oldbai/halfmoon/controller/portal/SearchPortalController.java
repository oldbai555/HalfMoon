package com.oldbai.halfmoon.controller.portal;

import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ArticleService;
import com.oldbai.halfmoon.solr.SolrService;
import com.oldbai.halfmoon.solr.SolrTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/portal/search")
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

    @GetMapping("/test/solr/add")
    public ResponseResult solrAddTest() {
        solrTestService.solrAdd();
        return ResponseResult.SUCCESS("添加成功");
    }

    @GetMapping("/test/solr/addAll")
    public ResponseResult solrAddAllTest() {
        solrTestService.importAll();
        return ResponseResult.SUCCESS("添加所有成功");
    }

    @GetMapping("/test/solr/update")
    public ResponseResult solrUpdateTest() {
        solrTestService.solrUpdate();
        return ResponseResult.SUCCESS("更新成功");
    }

    @GetMapping("/test/solr/delete")
    public ResponseResult solrDeleteTest() {
        solrTestService.solrDelete();
        return ResponseResult.SUCCESS("删除成功");
    }

    @GetMapping("/test/solr/deleteAll")
    public ResponseResult solrDeleteAllTest() {
        solrTestService.solrDeleteAll();
        return ResponseResult.SUCCESS("删除所有成功");
    }

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
