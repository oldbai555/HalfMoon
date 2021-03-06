package com.oldbai.halfmoon.util;

/**
 * 设置默认属性
 *
 * @author 老白
 */
public interface Constants {

    /**
     * 用户的初始化
     */
    interface User {
        //初始化管理员角色
        String ROLE_ADMIN = "role_admin";
        String ROLE_NORMAL = "role_normal";
        //头像
        String DEFAULT_AVATAR = "https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/07/466e33e357c546dcadc9fd938e6f468d%E5%A4%B4%E5%83%8F.gif";
        //状态
        String DEFAULT_STATE = "1";
        //以下是redis的key
        //验证码的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        //验证码的key
        String KEY_EMAIL_CODE_CONTENT = "key_email_code_content_";
        //email邮件IP地址的key
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        //email邮件发送邮箱地址的key
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";
        //md5的token kdy
        String KEY_TOKEN = "key_token_";
        //登陆后返回给cookid的token的名字
        String KEY_TOKEN_NAME = "blog_token";
        //登陆后返回给cookid的名字
        String COOKIE_TOKE_KEY = "blog_token";
        //登陆后返回给cookid的时间
        int COOKIE_TOKE_AGE = 60 * 60;
        //TODO redis存活时间 1 天
        int REDIS_AGE_DAY = 60 * 60;
        //判断是否重复提交
        String KEY_COMMIT_TOKEN_RECORD = "key_commit_token_record_";
    }

    /**
     * setting的初始化
     */
    interface Settings {
        //初始化管理员账号
        String HAS_MANAGER_ACCOUNT_INIT_STATE = "has_manager_init_state";
        //网站标题
        String WEB_SIZE_TITLE = "web_size_title";
        //网站描述
        String WEB_SIZE_DESCRIPTION = "web_size_description";
        //键，关键字
        String WEB_SIZE_KEYWORDS = "web_size_keywords";
        //网站浏览量
        String WEB_SIZE_VIEW_COUNT = "web_size_view_count";
    }

    /**
     * JWT 时间类
     * 单位 毫秒
     */
    interface RedisTime {
        int init = 1000;
        int MIN = 60 * init;
        int HOUR = 60 * MIN;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
        int YEAR = 365 * DAY;
    }

    /**
     * 统一时间
     * 单位 秒
     */
    interface TimeValue {
        int MIN = 60;
        int HALFT_MIN = 15;
        int HOUR = 60 * MIN;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
        int YEAR = 365 * DAY;
    }

    /**
     * 分页配制
     */
    interface Page {
        int DEFAULT_PAGE = 1;
        int MIN_SIZE = 5;
    }

    /**
     * 图片格式限制
     */
    interface ImageType {
        String PREFIX = "image/";
        String TYPE_JGP = "jpg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPEG = "jpeg";
        String TYPE_JGP_WITH_PREFIX = PREFIX + TYPE_JGP;
        String TYPE_PNG_WITH_PREFIX = PREFIX + TYPE_PNG;
        String TYPE_GIF_WITH_PREFIX = PREFIX + TYPE_GIF;
        String TYPE_JPEG_WITH_PREFIX = PREFIX + TYPE_JPEG;
    }

    /**
     * 文章
     */
    interface Article {
        // 0表示删除 、1表示发布 、2表示草稿 、3表示置顶
        String STATE_DELETE = "0";
        String STATE_PUBLISH = "1";
        String STATE_DRAFT = "2";
        String STATE_TOP = "3";
        int TITLE_MAX_LENGTH = 128;
        int SUMMARY_MAX_LENGTH = 256;
        String TYPE_MARKDOWN = "1";
        String TYPE_RICH_TEXT = "0";
        //文章缓存的key
        String KEY_ARTICLE_CACHE = "key_article_cache_";
        //文章浏览次数缓存的key
        String KEY_ARTICLE_VIEW_COUNT = "key_article_view_count_";
        //第一页缓存的key
        String KEY_ARTICLE_FIRST_PAGE = "key_article_first_page_";
    }

    /**
     * 评论的一个通用处理
     */
    interface Comment {

        String STATE_PUBLISH = "1";
        String STATE_TOP = "3";
        //第一页缓存的key
        String KEY_COMMENT_FIRST_PAGE = "key_comment_first_page_";
    }
}
