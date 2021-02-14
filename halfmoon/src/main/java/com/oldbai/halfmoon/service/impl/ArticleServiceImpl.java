package com.oldbai.halfmoon.service.impl;

import com.oldbai.halfmoon.entity.Article;
import com.oldbai.halfmoon.mapper.ArticleMapper;
import com.oldbai.halfmoon.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
