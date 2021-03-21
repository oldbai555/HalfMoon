/*
Navicat MySQL Data Transfer

Source Server         : docker_mysql
Source Server Version : 50732
Source Host           : 192.168.30.133:3306
Source Database       : halfmoon

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-03-21 15:46:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `user_id` varchar(255) NOT NULL COMMENT '用户ID',
  `user_avatar` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `category_id` varchar(255) NOT NULL COMMENT '分类ID',
  `content` mediumtext NOT NULL COMMENT '文章内容',
  `type` varchar(1) NOT NULL COMMENT '类型（0表示富文本，1表示markdown）',
  `state` varchar(1) NOT NULL COMMENT '状态（0表示已发布，1表示草稿，2表示删除）',
  `summary` text NOT NULL COMMENT '摘要',
  `labels` varchar(128) NOT NULL COMMENT '标签',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '阅读数量',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) DEFAULT '0' COMMENT '(0表示不删除,1表示删除)',
  `cover` varchar(1024) DEFAULT NULL COMMENT '封面',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES ('18fe3101-c087-4333-8981-0d5a9ef1687a', '路由跳转的方式', '0feed2d15abb871103ba9ee846aca914', null, null, 'b316fb0a5dd63ec177dc572f28f24b90', '场景：A页面跳转到B页面并携带参数\n\n##### 方案一：声明式导航router-link\n\n1.1不带参数：\n\n\n\n```bash\n// 注意：router-link中链接如果是\'/\'开始就是从根路由开始，如果开始不带\'/\'，则从当前路由开始。\n<router-link :to=\"{name:\'home\'}\">  \n<router-link :to=\"{path:\'/home\'}\"> //name,path都行, 建议用name \n```\n\n1.2带参数：\n\n\n\n```xml\n<router-link :to=\"{name:\'home\', params: {id:1}}\">\n<router-link :to=\"{name:\'home\', query: {id:1}}\">  \n<router-link :to=\"/home/:id\">  \n//传递对象\n<router-link :to=\"{name:\'detail\', query: {item:JSON.stringify(obj)}}\"></router-link> \n```\n\n##### 方案二：编程式导航 this.$router.push()\n\n2.1不带参数：\n\n\n\n```kotlin\nthis.$router.push(\'/home\')\nthis.$router.push({name:\'home\'})\nthis.$router.push({path:\'/home\'})\n```\n\n2.2 query传参\n\n\n\n```kotlin\n1.路由配置：\nname: \'home\',\npath: \'/home\'\n2.跳转：\nthis.$router.push({name:\'home\',query: {id:\'1\'}})\nthis.$router.push({path:\'/home\',query: {id:\'1\'}})\n3.获取参数\nhtml取参: $route.query.id\nscript取参: this.$route.query.id\n```\n\n2.3 params传参\n\n\n\n```csharp\n1.路由配置：\nname: \'home\',\npath: \'/home/:id\'(或者path: \'/home:id\')\n2.跳转：\nthis.$router.push({name:\'home\',params: {id:\'1\'}})\n注意：\n// 只能用 name匹配路由不能用path\n// params传参数(类似post)  路由配置 path: \"/home/:id\" 或者 path: \"/home:id\"否则刷新参数消失\n3.获取参数\nhtml取参:$route.params.id \nscript取参:this.$route.params.id\n```\n\n2.4 直接通过path传参\n\n\n\n```kotlin\n1.路由配置：\nname: \'home\',\npath: \'/home/:id\'\n2.跳转：\nthis.$router.push({path:\'/home/123\'}) \n或者：\nthis.$router.push(\'/home/123\') \n3.获取参数：\nthis.$route.params.id\n```\n\n2.5 传递对象(obj不能过长否则会报错)\n\n\n\n```tsx\nquery传递对象 (类似get,url后面会显示参数) JSON.stringify(obj) 转一下.\n接收参数：\nJS:JSON.parse(decodeURIComponent(this.$route.query.obj)) \nHTML:JSON.parse(decodeURIComponent($route.query.obj))\n```\n\n2.6 params和query的区别\n\n\n\n```csharp\nquery类似 get，跳转之后页面 url后面会拼接参数，类似?id=1。\n非重要性的可以这样传，密码之类还是用params，刷新页面id还在。\nparams类似 post，跳转之后页面 url后面不会拼接参数。\n```\n\n##### 方案三.this.$router.replace()\n\n用法同上：`this.$router.push()`\n\n##### 方案四this.$router.go(n)\n\n向前或者向后跳转n个页面，n可为正整数或负整数\n\n##### 区别:\n\n1.`this.$router.push`\n 跳转到指定url路径，并在history栈中添加一个记录，点击后退会返回到上一个页面\n\n1. `this.$router.replace`\n    跳转到指定url路径，但是history栈中不会有记录，点击返回会跳转到上上个页面 (就是直接替换了当前页面)\n\n3.`this.$router.go(n)`\n 向前或者向后跳转n个页面，n可为正整数或负整数\n\n### 总结：\n\n**1.`query`可以用`name`和`path`匹配路由，通过`this.$route.query.id`获取参数，刷新浏览器参数丢失\n 2.`params`只能用`name`匹配路由，否则获取不到参数，对应的路由配置`path:\'/home/:id\'`或者`path:\'home:id\'`，否则刷新浏览器参数丢失\n 3.直接通过url传参，`push({path: \'/home/123\'})`或者`push(\'/home/123\')`，对应的路由配置`path:\'/home/:id\'`，刷新浏览器参数不会丢失(推荐)**', '1', '1', 'Vue的路由跳转', 'java-vue', '0', '2021-03-21 12:55:11', '2021-03-21 12:55:11', '0', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/1e6510c902c648739f620a4db4e6056aQQ图片20210320140517.jpg');
INSERT INTO `tb_article` VALUES ('2d89133a-3452-42e9-90bf-9c59d2c27cb9', '自定义模板-HTML部分', '0feed2d15abb871103ba9ee846aca914', null, null, '4ef92691411d967075fb5e057e988dc8', '##  HTML部分\n\n###  添加按钮\n\n```vue\n添加按钮\n<div class=\"loop-action-bar\">\n      <el-button type=\"primary\" plain @click=\"addView()\">添加</el-button>\n    </div>\n```\n\n###  数据展示列表\n\n```vue\n数据展示列表\n<div class=\"loop-list-box\">\n      <el-table :data=\"dataBody\" style=\"width: 100%\" v-loading=\"loading\">\n      \n        <el-table-column prop=\"data_1\" label=\"数据一\" width=\"120\">\n        </el-table-column>\n        \n        <el-table-column prop=\"data_2\" label=\"数据二\" width=\"120\">\n        </el-table-column>\n        \n        <el-table-column label=\"state\" width=\"120\">\n          <template slot-scope=\"scope\">\n            <div v-if=\"scope.row.state === \'1\'\">\n              <el-tag type=\"success\">正常</el-tag>\n            </div>\n          </template>\n        </el-table-column>\n        \n        <el-table-column prop=\"data_3\" label=\"数据三\"> </el-table-column>\n        \n        <el-table-column label=\"创建时间\">\n          <!-- 插槽用法 -->\n          <template slot-scope=\"scope\">\n            <span v-text=\"formatDate(scope.row.createTime)\"></span>\n          </template>\n        </el-table-column>\n        \n        <el-table-column label=\"更新时间\">\n          <template slot-scope=\"scope\">\n            <span v-text=\"formatDate(scope.row.updateTime)\"></span>\n          </template>\n        </el-table-column>\n        \n        <el-table-column fixed=\"right\" label=\"操作\" width=\"200\">\n        \n          <template slot-scope=\"scope\">\n          \n            <el-button type=\"primary\" size=\"mini\" @click=\"editView(scope.row)\"\n              >编辑</el-button\n            >\n            \n            <el-button type=\"danger\" size=\"mini\" @click=\"deleteView(scope.row)\"\n              >删除</el-button\n            >\n            \n          </template>\n          \n        </el-table-column>\n      </el-table>\n    </div>\n```\n\n###  删除对话框\n\n```vue\n<!-- 删除对话框 -->\n    <div class=\"loop-dialog-box\">\n      <el-dialog\n        title=\"删除提示\"\n        :visible.sync=\"delete_show_view\"\n        width=\"30%\"\n        :before-close=\"handleClose\"\n      >\n        <span>你确定要删除：{{ delete_body_name }}这个分类吗？</span>\n        <span slot=\"footer\" class=\"dialog-footer\">\n          <el-button @click=\"delete_show_view = false\" type=\"danger\" size=\"mini\"\n            >取 消</el-button\n          >\n          <el-button type=\"primary\" @click=\"doDeleteItem()\" size=\"mini\"\n            >确 定</el-button\n          >\n        </span>\n      </el-dialog>\n    </div>\n```\n\n###  添加或更新的对话框\n\n```vue\n<!-- 添加或更新的对话框 -->\n    <div class=\"loop-dialog-addOrUpdate-box\">\n      <el-dialog\n        :title=\"editTitle\"\n        :visible.sync=\"add_edit_show_view\"\n        width=\"30%\"\n        :before-close=\"handleClose\"\n      >\n        <el-form\n          :label-position=\"labelPosition\"\n          label-width=\"80px\"\n          :model=\"dataForm\"\n        >\n          <el-form-item label=\"表单数据一\">\n            <el-input v-model=\"dataForm.data_1\"></el-input>\n          </el-form-item>\n          <el-form-item label=\"表单数据二\">\n            <el-input v-model=\"dataForm.data_2\"></el-input>\n          </el-form-item>\n          <el-form-item label=\"表单数据三\">\n            <el-input\n              type=\"textarea\"\n              resize=\"none\"\n              :rows=\"2\"\n              v-model=\"dataForm.data_3\"\n            ></el-input>\n          </el-form-item>\n        </el-form>\n\n        <div slot=\"footer\" class=\"dialog-footer\">\n        \n          <el-button @click=\"initForm()\" type=\"danger\">取 消</el-button>\n          \n          <el-button type=\"primary\" @click=\"doEdit()\">{{\n            editorCommitTitle\n          }}</el-button>\n          \n        </div>\n      </el-dialog>\n    </div>\n```\n\n###  分页部分\n\n```vue\n   分页部分\n   \n   <el-row :gutter=\"20\">\n      <el-col :span=\"8\" :offset=\"8\">\n        <div class=\"navigation-bar\" style=\"padding-top: 40px\">\n          <!-- 分页部分 -->\n          <el-pagination\n            background\n            layout=\"total, prev, pager, next, jumper\"\n            :hide-on-single-page=\"true\"\n            :page-size=\"pageformData.size\"\n            :current-page=\"pageformData.currentPage\"\n            :total=\"pageformData.total\"\n            @prev-click=\"prevPage\"\n            @next-click=\"nextPage\"\n            @current-change=\"currentPageChange\"\n          >\n          </el-pagination>\n        </div>\n      </el-col>\n    </el-row>\n```\n\n###  上传图片\n\n```vue\n<el-button\n              type=\"primary\"\n              icon=\"el-icon-edit\"\n              @click=\"viewUpload()\"\n              circle\n            ></el-button>\n<el-dialog\n            title=\"提示\"\n            :visible.sync=\"show\"\n            width=\"30%\"\n            :before-close=\"handleClose\"\n          >\n            <el-form :label-position=\"labelPosition\" label-width=\"100px\">\n              <el-form-item label=\"头像\">\n                <el-upload\n                  class=\"avatar-uploader\"\n                  action=\"http://localhost:8078/halfmoon/images/upload\"\n                  :http-request=\"uploadImg\"\n                  :show-file-list=\"false\"\n                  :with-credentials=\"true\"\n                  :before-upload=\"beforeAvatarUpload\"\n                  accept=\".png,.jpg,.gif\"\n                >\n                  <img v-if=\"imageUrl !== \'\'\" :src=\"imageUrl\" class=\"avatar\" />\n                  <i v-else class=\"el-icon-plus avatar-uploader-icon\"></i>\n                </el-upload>\n              </el-form-item>\n            </el-form>\n            <span slot=\"footer\" class=\"dialog-footer\">\n              <el-button @click=\"show = false\">取 消</el-button>\n              <el-button type=\"primary\" @click=\"viewUpload()\">确 定</el-button>\n            </span>\n          </el-dialog>\n```\n\n```vue\n<el-form-item label=\"封面\">\n            <el-upload\n              class=\"avatar-uploader\"\n              action=\"http://localhost:8078/halfmoon/images/upload\"\n              :http-request=\"uploadImg\"\n              :show-file-list=\"false\"\n              :with-credentials=\"true\"\n              :before-upload=\"beforeAvatarUpload\"\n              accept=\".png,.jpg,.gif\"\n            >\n              <img\n                v-if=\"dataForm.imageUrl !== \'\'\"\n                :src=\"dataForm.imageUrl\"\n                class=\"avatar\"\n              />\n              <i v-else class=\"el-icon-plus avatar-uploader-icon\"></i>\n            </el-upload>\n          </el-form-item>\n```\n\n\n\n###  带跳转链接的表格 column\n\n```vue\n<el-table-column label=\"标题\" width=\"120\">\n          <template slot-scope=\"scope\">\n            <el-link\n              :href=\"scope.row.targetUrl\"\n              target=\"_blank\"\n              :underline=\"false\"\n              >{{ scope.row.title }}</el-link\n            >\n          </template>\n        </el-table-column>\n```\n\n###  带图片的表格 column\n\n```vue\n<el-table-column label=\"轮播图\" width=\"250\">\n          <template slot-scope=\"scope\">\n            <el-image :src=\"scope.row.imageUrl\" fit=\"contain\">\n              <div slot=\"placeholder\" class=\"image-slot\">\n                加载中<span class=\"dot\">...</span>\n              </div>\n            </el-image>\n          </template>\n        </el-table-column>\n```\n\n###  条件搜索框\n\n```vue\n    <div class=\"loop-action-bar\">\n      <el-button type=\"warning\" plain disabled>搜索用户</el-button>\n      <el-form :inline=\"true\" :model=\"formInline\" class=\"demo-form-inline\">\n        <el-form-item label=\"用户名\">\n          <el-input\n            v-model=\"formInline.userName\"\n            placeholder=\"请输入用户名\"\n          ></el-input>\n        </el-form-item>\n        <el-form-item label=\"邮箱\">\n          <el-input\n            v-model=\"formInline.email\"\n            placeholder=\"请输入邮箱\"\n          ></el-input>\n        </el-form-item>\n        <el-form-item>\n          <el-button type=\"primary\" @click=\"onSubmit\">查询</el-button>\n          <el-button @click=\"resetForm()\">重置</el-button>\n        </el-form-item>\n      </el-form>\n    </div>\n```\n\n', '1', '1', 'Vue管理模块', 'java-vue', '0', '2021-03-21 12:57:56', '2021-03-21 12:58:44', '0', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/1e6510c902c648739f620a4db4e6056aQQ图片20210320140517.jpg');
INSERT INTO `tb_article` VALUES ('53fd4e2c-ee1f-4d89-a05f-1dda603f5f8e', '布局模板（可以直接用）', '0feed2d15abb871103ba9ee846aca914', null, null, '4ef92691411d967075fb5e057e988dc8', '##  整体布局\n\n```vue\n//layout布局\n<template>\n  <div>\n    <el-container>\n      <el-header id=\"admin-header-box\">\n        <topHeader></topHeader>\n      </el-header>\n      <el-container>\n        <el-aside id=\"left-menu-list-box\" width=\"220px\">\n          <leftMenu></leftMenu>\n        </el-aside>\n        <el-container>\n          <el-main>\n            <router-view></router-view>\n          </el-main>\n        </el-container>\n      </el-container>\n    </el-container>\n  </div>\n</template>\n\n<script>\nexport default {\n  //import 引入的组件需要注入到对象中才能使用\n  components: {},\n  props: {},\n  data() {\n    //这里存数据\n    return {};\n  },\n  //计算属性\n  computed: {},\n  //监控data中数据变化\n  watch: {},\n  //方法\n  methods: {},\n  //声明周期 - 创建完成（可以访问当前this实例）\n  created() {},\n  //生命周期 - 挂载完成（可以访问DOM元素）\n  mounted() {\n    //动态设置侧边栏高度 left-menu-list-box\n    let leftMenuBox = document.getElementById(\"left-menu-list-box\");\n    let adminHeaderBox = document.getElementById(\"admin-header-box\");\n\n    if (leftMenuBox && adminHeaderBox) {\n      leftMenuBox.style.height =\n        window.innerHeight - adminHeaderBox.offsetHeight + \"px\";\n    }\n  },\n  beforeCreate() {}, //生命周期 - 创建之前\n  beforeMount() {}, //生命周期 - 挂载之前\n  beforeUpdate() {}, //声明周期 - 更新之前\n  updated() {}, //生命周期 - 更新之后\n  beforeDestroy() {}, //生命周期 - 销毁之前\n  destroyed() {}, //生命周期 - 销毁之后\n  activated() {}, //缓存keep-alive\n};\n</script>\n\n<style scoped>\n.el-header {\n  background-color: dodgerblue;\n  color: #333;\n  line-height: 46px;\n  height: 46px !important;\n}\n\n.el-aside {\n  border-right: solid 1px #e6e6e6;\n}\n\n#left-menu-list-box .el-menu {\n  border-right: none;\n}\n.el-main{\n  padding: 0 !important;\n}\n</style>\n```\n\n##  左边布局(动态路由加载列表)\n\n```vue\n<template>\n  <div>\n    <el-menu\n      :default-active=\"$route.path\"\n      class=\"el-menu-vertical-demo\"\n      :unique-opened=\"true\"\n    >\n      <!-- 遍历路由表 得到菜单内容 -->\n      <!-- 有两种：1.第一种没有子菜单 2.第二种有子菜单 -->\n      <template v-for=\"(item, index) in menuList\">\n        <!-- 1.第一种没有子菜单 -->\n        <router-link\n          v-if=\"!item.children && !item.hidden\"\n          :key=\"index\"\n          :index=\"item.path\"\n          :to=\"item.path\"\n        >\n          <el-menu-item>\n            <i :class=\"item.icon\"></i>\n            <span slot=\"title\">{{ item.title }}</span>\n          </el-menu-item>\n        </router-link>\n\n        <!-- 2.第二种有子菜单 -->\n        <el-submenu\n          v-if=\"item.children && !item.hidden\"\n          :key=\"index\"\n          :index=\"item.path\"\n        >\n          <template slot=\"title\">\n            <i :class=\"item.icon\"></i>\n            <span>{{ item.title }}</span>\n          </template>\n          <!-- 遍历子类 -->\n          <router-link\n            v-for=\"(subItem, subIndex) in item.children\"\n            :key=\"subIndex\"\n            :to=\"subItem.path\"\n          >\n            <el-menu-item :index=\"subItem.path\" v-if=\"!subItem.hidden\">\n              <i :class=\"subItem.icon\"></i>\n              <span>{{ subItem.title }}</span>\n            </el-menu-item>\n          </router-link>\n        </el-submenu>\n      </template>\n    </el-menu>\n  </div>\n</template>\n\n<script>\nimport { routes } from \"../router/routes\";\nexport default {\n  //import 引入的组件需要注入到对象中才能使用\n  components: {},\n  props: {},\n  data() {\n    //这里存数据\n    return {\n      menuList: [],\n    };\n  },\n  //计算属性\n  computed: {},\n  //监控data中数据变化\n  watch: {},\n  //方法\n  methods: {},\n  //声明周期 - 创建完成（可以访问当前this实例）\n  created() {},\n  //生命周期 - 挂载完成（可以访问DOM元素）\n  mounted() {\n    let menuList = routes[0];\n    this.menuList = menuList.children;\n    // console.log(this.menuList);\n  },\n  beforeCreate() {}, //生命周期 - 创建之前\n  beforeMount() {}, //生命周期 - 挂载之前\n  beforeUpdate() {}, //声明周期 - 更新之前\n  updated() {}, //生命周期 - 更新之后\n  beforeDestroy() {}, //生命周期 - 销毁之前\n  destroyed() {}, //生命周期 - 销毁之后\n  activated() {}, //缓存keep-alive\n};\n</script>\n\n<style scoped>\n.el-menu-vertical-demo a {\n  text-decoration: none;\n}\n</style>\n```\n\n##  右边布局\n\n```vue\n<template>\n  <div>\n    <!-- <div class=\"content-title-box\" v-text=\"this.$route.name\"></div> -->\n    <el-page-header\n      @back=\"goBack\"\n      :content=\"this.$route.name\"\n      style=\"margin: 10px; font-weight: 600\"\n    >\n    </el-page-header>\n    <div class=\"content-container-box\">\n      <router-view />\n    </div>\n  </div>\n</template>\n\n<script>\nexport default {\n  //import 引入的组件需要注入到对象中才能使用\n  components: {},\n  props: {},\n  data() {\n    //这里存数据\n    return {};\n  },\n  //计算属性\n  computed: {},\n  //监控data中数据变化\n  watch: {},\n  //方法\n  methods: {\n    goBack() {\n      this.$router.go(-1);\n    },\n  },\n  //声明周期 - 创建完成（可以访问当前this实例）\n  created() {},\n  //生命周期 - 挂载完成（可以访问DOM元素）\n  mounted() {\n    console.log(this.$route.name);\n  },\n  beforeCreate() {}, //生命周期 - 创建之前\n  beforeMount() {}, //生命周期 - 挂载之前\n  beforeUpdate() {}, //声明周期 - 更新之前\n  updated() {}, //生命周期 - 更新之后\n  beforeDestroy() {}, //生命周期 - 销毁之前\n  destroyed() {}, //生命周期 - 销毁之后\n  activated() {}, //缓存keep-alive\n};\n</script>\n\n<style scoped>\n.content-title-box {\n  padding: 10px;\n  border-bottom: solid 1px #e6e6e6;\n}\n.content-container-box {\n  padding: 10px;\n}\n</style>\n```\n\n##  头部布局\n\n```vue\n<template>\n  <div>\n    <div class=\"header-left-box\">\n      <div class=\"header-logo\">月半湾博客管理中心</div>\n    </div>\n    <div class=\"header-right-box\"></div>\n  </div>\n</template>\n\n<script>\nexport default {\n  //import 引入的组件需要注入到对象中才能使用\n  components: {},\n  props: {},\n  data() {\n    //这里存数据\n    return {};\n  },\n  //计算属性\n  computed: {},\n  //监控data中数据变化\n  watch: {},\n  //方法\n  methods: {},\n  //声明周期 - 创建完成（可以访问当前this实例）\n  created() {},\n  //生命周期 - 挂载完成（可以访问DOM元素）\n  mounted() {},\n  beforeCreate() {}, //生命周期 - 创建之前\n  beforeMount() {}, //生命周期 - 挂载之前\n  beforeUpdate() {}, //声明周期 - 更新之前\n  updated() {}, //生命周期 - 更新之后\n  beforeDestroy() {}, //生命周期 - 销毁之前\n  destroyed() {}, //生命周期 - 销毁之后\n  activated() {}, //缓存keep-alive\n};\n</script>\n\n<style scoped>\n.header-logo {\n  color: #fff;\n  font-size: 20px;\n  font-weight: 600;\n}\n</style>\n```\n\n##  路由具体配置\n\n```javascript\nfunction load(component) {\n    return resolve => require([`../views/${component}`], resolve);\n}\n//layout 布局\nconst baseView = () => import(\"@/layout/base-view\")\nconst rightView = () => import(\"@/layout/right-content\")\n\nexport const routes = [\n    {\n        path: \'\',\n        redirect: \'/index\',\n        name: \'baseView\',\n        component: baseView,\n        meta: {\n            title: \'layout布局\'\n        },\n        children: [\n            {\n                path: \'/index\',\n                name: \'index\',\n                hidden: false,//是否隐藏，用来做动态\n                component: load(\'index\'),\n                title: \'首页\',\n                // requireLogin: true, //是否拦截\n                icon: \'el-icon-s-home\',\n\n            },\n            {\n                path: \'/content\',\n                name: \'rightView\',\n                hidden: false,//是否隐藏\n                requireLogin: true,\n                component: rightView,\n                title: \'内容\',\n                icon: \'el-icon-tickets\',\n                children: [\n                    {\n                        path: \'/post-article\',\n                        name: \'post-article\',\n                        hidden: false,//是否隐藏\n                        component: load(\'content/post-article\'),\n                        title: \'文章发表\',\n                        icon: \'el-icon-edit\',\n\n                    },\n                    {\n                        path: \'/manage-article\',\n                        name: \'manage-article\',\n                        hidden: false,//是否隐藏\n                        component: load(\'content/manage-article\'),\n                        title: \'文章管理\',\n                        icon: \'el-icon-document\',\n\n                    },\n                    {\n                        path: \'/manage-comment\',\n                        name: \'manage-comment\',\n                        hidden: false,//是否隐藏\n                        component: load(\'content/manage-comment\'),\n                        title: \'评论管理\',\n                        icon: \'el-icon-chat-dot-round\',\n\n                    },\n                    {\n                        path: \'/manage-image\',\n                        name: \'manage-image\',\n                        hidden: false,//是否隐藏\n                        component: load(\'content/manage-image\'),\n                        title: \'图片管理\',\n                        icon: \'el-icon-picture-outline\',\n\n                    },\n                ]\n            },\n            {\n                path: \'/opation\',\n                name: \'rightView\',\n                hidden: false,//是否隐藏\n                component: rightView,\n                title: \'运营\',\n                icon: \'el-icon-more\',\n\n                children: [\n                    {\n                        path: \'/category\',\n                        name: \'category\',\n                        hidden: false,//是否隐藏\n                        component: load(\'opation/category\'),\n                        title: \'分类管理\',\n                        icon: \'el-icon-s-unfold\',\n\n                    },\n                    {\n                        path: \'/loop\',\n                        name: \'loop\',\n                        hidden: false,//是否隐藏\n                        component: load(\'opation/loop\'),\n                        title: \'轮播图管理\',\n                        icon: \'el-icon-picture-outline-round\',\n\n                    }\n                ]\n            },\n            {\n                path: \'/user\',\n                name: \'rightView\',\n                hidden: false,//是否隐藏\n                component: rightView,\n                title: \'用户\',\n                icon: \'el-icon-user\',\n\n                children: [\n                    {\n                        path: \'/list\',\n                        name: \'list\',\n                        hidden: false,//是否隐藏\n                        component: load(\'user/list\'),\n                        title: \'用户列表\',\n                        icon: \'el-icon-user-solid\',\n\n                    },\n                    {\n                        path: \'/email\',\n                        name: \'email\',\n                        hidden: false,//是否隐藏\n                        title: \'管理员邮箱地址\',\n                        icon: \'el-icon-message\',\n                        component: load(\'user/email\'),\n\n                    },\n                    {\n                        path: \'/info\',\n                        name: \'info\',\n                        hidden: false,//是否隐藏\n                        component: load(\'user/info\'),\n                        title: \'管理员信息\',\n                        icon: \'el-icon-warning-outline\',\n\n                    }\n                ]\n            },\n            {\n                path: \'/settings\',\n                name: \'rightView\',\n                hidden: false,//是否隐藏\n                component: rightView,\n                title: \'设置\',\n                icon: \'el-icon-setting\',\n\n                children: [\n                    {\n                        path: \'/websize-info\',\n                        name: \'websize-info\',\n                        hidden: false,//是否隐藏\n                        title: \'网站信息\',\n                        icon: \'el-icon-eleme\',\n                        component: load(\'settings/websize-info\'),\n\n                    },\n                    {\n                        path: \'/friend-link\',\n                        name: \'friend-link\',\n                        title: \'友情链接\',\n                        icon: \'el-icon-link\',\n                        hidden: false,//是否隐藏\n                        component: load(\'settings/friend-link\'),\n\n                    }\n                ]\n            },\n        ]\n    },\n    {\n        path: \'/login\',\n        name: \'login\',\n        title: \'登陆页面\',\n        icon: \'el-icon-arrow-right\',\n        component: load(\'login/index\'),\n        // requireLogin: false,\n    }\n\n];\n\nexport default routes;\n```\n\n##  路由的拦截配置\n\n```javascript\nimport Vue from \'vue\';\nimport VueRouter from \'vue-router\';\nimport routes from \'./routes\';\nimport { checkToken } from \"@/api/axios\";\n\nVue.use(VueRouter);\n\nconst router = new VueRouter({\n    mode: \'hash\',\n    base: process.env.BASE_URL,\n    routes,\n    scrollBehavior(to, from, savePosition) {\n        if (savePosition) {\n            return savePosition;\n        } else {\n            return { x: 0, y: 0 };\n        }\n    }\n});\n//前置拦截\nrouter.beforeEach((to, from, next) => {\n    // console.log(\"beforeEach-to==>\", to)\n    // console.log(\"beforeEach-from==>\", from)\n    // console.log(\"beforeEach-next==>\", next)\n    if (to.path === \'/login\') {\n        //如果要 做得更好一点，如果用户要跳转到登 录页面\n        //当前已经登录了，则没必要再到登录界面了，除非用户点击退出登录\n        //如果已经登录， 根据角色判断页面跳转。\n\n        checkToken().then(resp => {\n            if (resp.code === 10000) {\n                console.log(\"登陆成功,不让进来了\")\n                next({\n                    path: \'/index\'\n                })\n            } else {\n                console.log(\"登陆失败,让进来了\")\n                next()\n            }\n        })\n    } else {\n        //进行角色检查\n        checkToken().then(resp => {\n            // console.log(resp)\n            if (resp.code === 10000) {\n                console.log(\"登陆成功\")\n                //登陆成功\n                //判断角色\n                if (resp.data.roles === \'role_admin\') {\n                    console.log(\"g管理\")\n                    next()\n                } else {\n                    //跳转到首页\n                    console.log(\"非管理\")\n                    location.href = \'http://oldbai.top\'\n                }\n                next()\n            } else {\n                console.log(\"登陆失败\")\n                //登陆失败,跳转到登陆页面\n                next({\n                    path: \'/login\'\n                })\n            }\n        }).catch(err => {\n            console.log(err)\n        })\n    }\n    // next();\n});\n//后置拦截\nrouter.afterEach((to, from) => {\n    // console.log(\"afterEach-to==>\", to)\n    // console.log(\"afterEach-from==>\", from)\n    window.scrollTo(0, 0);\n});\n\nexport default router;\n```\n\n#  ', '1', '1', '布局', 'java-vue', '0', '2021-03-21 12:56:51', '2021-03-21 12:56:51', '0', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/1e6510c902c648739f620a4db4e6056aQQ图片20210320140517.jpg');
INSERT INTO `tb_article` VALUES ('6c1e8dfa-9079-4bb2-8430-818868e60c67', '自定义模板-JS部分', '0feed2d15abb871103ba9ee846aca914', null, null, '4ef92691411d967075fb5e057e988dc8', '###  搜索框的方法以及数据\n\n```javascript\n//开始搜索\n    onSubmit() {\n      this.getList();\n    },\n    //重置搜索框\n    resetForm() {\n      //重置数据\n      this.articleRule.categoryId = \"\";\n      this.articleRule.keyword = \"\";\n      this.articleRule.state = \"\";\n      //然后获取默认的\n      this.getList();\n    },\n```\n\n```\narticleRule: {\n        categoryId: \"\",\n        keyword: \"\",\n        state: \"\",\n      },\n```\n\n\n\n###  data 部分\n\n```js\ndata() {\n    //这里存数据\n    return {\n      delete_body_name: \"\",\n      delete_body_data: \"\",\n      delete_show_view: false,\n      add_edit_show_view: false,\n      add_or_edit_view: \"edit\",\n      labelPosition: \"right\",\n      loading: true,\n      dataBody: [\n        //   这里放列表数据\n      ],\n      dataForm: {\n        //   这里放想要修改的数据\n        id: \"\",\n        name: \"\",\n        logo: \"\",\n        url: \"\",\n        state: \"\",\n        createTime: \"\",\n        updateTime: \"\",\n      },\n      editorCommitTitle: \"修改分类\",\n      editTitle: \"编辑分类\",\n      pageformData: {\n        size: 5,\n        currentPage: 1,\n        total: 5,\n      },\n	  imageUrl:\"\",\n      show:false,\n    };\n  },\n```\n\n\n\n###  日期转换\n\n```javascript\n//日期转换\n    formatDate(dateStr) {\n      var date = new Date(dateStr);\n      return dateUtils.formatDate(date, \"yyyy-MM-dd hh:mm:ss\");\n    },\n```\n\n###  确认关闭对话框\n\n```javascript\n//确认关闭对话框\n    handleClose(done) {\n      this.$confirm(\"确认关闭？\")\n        .then((_) => {\n          this.initForm();\n          done();\n        })\n        .catch((_) => {});\n    },\n```\n\n###  初始化表单对象\n\n```javascript\n//初始化表单对象\n    initForm() {\n      //给表单对象 this.dataForm 初始化\n      this.dialogVisible_addOrUpdate = false;\n    },\n```\n\n###  获取分类列表\n\n```javascript\n//获取分类列表\n    getList() {\n      //去获取分类列表\n      //给 this.dataBody 赋值对象数据\n      this.loading = false;\n    },\n```\n\n###  获取分类列表 - 分页\n\n```javascript\n//获取对象列表\n    getList(currentPage) {\n         let _this = this;\n      //去获取对象列表\n      this.pageformData.currentPage =\n        currentPage === undefined ? this.pageformData.currentPage : currentPage;\n      //给 this.bodyData 赋值对象数据\n      api.getUserList(page,size).then((resp) => {\n        // console.log(resp);\n        if (resp.code === api.success_code) {\n          _this.bodyData = resp.data.records;\n          _this.pageformData.size = resp.data.size;\n            _this.pageformData.total = resp.data.total;\n            _this.pageformData.currentPage = resp.data.current;\n          api.toast_succ(resp.message);\n        } else {\n          api.toast_err(resp.message);\n        }\n      });\n      _this.loading = false;\n    },\n```\n\n\n\n###  获取当前对象\n\n```javascript\n//获取当前对象\n    getdataBody(categoryId) {\n      //防止多人同时修改，可以用mybatis-plus的version 乐观锁\n      //也就是获取当前对象 发送axios请求吧\n      this.dialogVisible_addOrUpdate = false;\n    },\n```\n\n###  删除 ,打开删除窗口\n\n```javascript\n//删除 ,打开删除窗口\n    deleteView(item) {\n      //打开删除窗口，\n      //进行数据的获取，获取当前对象和删除的对象的名称\n      //为了执行删除操作可以获取到id\n      this.delete_show_view = true;\n      console.log(item);\n    },\n```\n\n###  执行删除操作\n\n```javascript\n//执行删除操作\n    doDeleteItem() {\n        let _this = this;\n      //执行删除请求\n      //完了记得获取最新数据列表\n      _this.getList(1);\n      this.delete_show_view = false;\n    },\n```\n\n### 打开编辑分类 的对话框\n\n```javascript\n//打开编辑分类 的对话框\n    editView(item) {\n      (this.editorCommitTitle = \"修改分类\"),\n        (this.editTitle = \"编辑分类\"),\n        (this.addOrEdit = \"edit\");\n      //调用方法获取最新的数据\n      this.getdataBody(item.id);\n      this.add_edit_show_view = true;\n      // console.log(item);\n    },\n```\n\n###  打开添加对话框\n\n```javascript\n//打开添加对话框\n    addView() {\n      (this.editorCommitTitle = \"添加分类\"),\n        (this.editTitle = \"添加分类\"),\n        (this.addOrEdit = \"add\");\n      //初始化表单对象\n      this.initForm();\n      this.add_edit_show_view = true;\n    },\n```\n\n###  执行编辑或执行添加操作\n\n```javascript\n//执行编辑或执行添加操作\n    doEdit() {\n      console.log(\"修改或添加分类\");\n      if (this.addOrEdit === \"edit\") {\n        //执行修改\n        //完了记得获取最新数据列表\n      } else {\n        //先判断数据是否为空\n        //然后进行添加\n        //完了记得获取最新数据列表\n      }\n      this.add_edit_show_view = false;\n    },\n```\n\n###  下一页\n\n```javascript\n//下一页\n    nextPage() {\n      this.getList(this.pageformData.currentPage + 1);\n    },\n```\n\n###  上一页\n\n```javascript\n//上一页\n    prevPage() {\n      this.getList(this.pageformData.currentPage - 1);\n    },\n```\n\n###  页码改变\n\n```javascript\n//currentPage 改变时会触发\n    currentPageChange(page) {\n      this.getList(page);\n    },\n```\n\n###  覆盖上传方法\n\n```js\n //覆盖上传方法\n    uploadImg(param) {\n      // console.log(param);\n      const formData = new FormData();\n      formData.append(\"file\", param.file);\n      formData.append(\"overwrite\", false);\n      api.uploadImg(formData).then((res) => {\n        if (res.code === api.success_code) {\n          api.toast_succ(res.message);\n          // console.log(res.data.url);\n          this.imageUrl = res.data.url;\n        } else {\n          api.toast_err(res.message);\n        }\n      });\n    },\n```\n\n###  修改用户头像,使用上传组件\n\n```js\n//修改用户头像,使用上传组件\n    viewUpload() {\n      this.userInfo.avatar = this.imageUrl;\n      this.show = !this.show;\n    },\n```\n\n###  上传图片前的检查\n\n```js\n//上传图片前回调函数\n    beforeAvatarUpload(file) {\n      const isJpeg = file.type === \"image/jpeg\";\n      const isJpg = file.type === \"image/jpg\";\n      const isPng = file.type === \"image/png\";\n      const isGif = file.type === \"image/gif\";\n      const isLt5M = file.size / 1024 / 1024 < 5;\n      var isOkUpload = true;\n\n      if (!isLt5M) {\n        this.$message.error(\"上传头像图片大小不能超过 5MB!\");\n      }\n      if (!(isJpg || isJpeg || isPng || isGif)) {\n        this.$message.error(\"上传头像图片只能是 JPG / jpeg / png / gif 格式!\");\n        isOkUpload = false;\n      }\n      return isOkUpload && isLt5M;\n    },\n```\n\n###  axios返回通用模板\n\n```js\n.then((resp) => {\n        if (resp.code === api.success_code) {\n          api.toast_succ(resp.message);\n        } else {\n          api.toast_err(resp.message);\n        }\n      });\n```\n\n###  路由跳转\n\n```javascript\n1.路由配置：\nname: \'home\',\npath: \'/home\'\n2.跳转：\nthis.$router.push({name:\'home\',query: {id:\'1\'}})\nthis.$router.push({path:\'/home\',query: {id:\'1\'}})\n3.获取参数\nhtml取参: $route.query.id\nscript取参: this.$route.query.id\n```\n\n', '1', '3', 'Js部分', 'java-vue', '0', '2021-03-21 12:59:35', '2021-03-21 14:38:23', '0', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/1e6510c902c648739f620a4db4e6056aQQ图片20210320140517.jpg');
INSERT INTO `tb_article` VALUES ('d317fc01-6b58-4b3b-bef2-62b66e914c01', '月半湾博客管理页面笔记', '0feed2d15abb871103ba9ee846aca914', null, null, 'b316fb0a5dd63ec177dc572f28f24b90', '###  日期转换\n\n```javascript\nexport function formatDate(date, format) {\n    if (/(y+)/.test(format))\n        format = format.replace(\n            RegExp.$1,\n            (date.getFullYear() + \"\").substr(4 - RegExp.$1.length)\n        );\n    var o = {\n        \"M+\": date.getMonth() + 1, //month\n        \"d+\": date.getDate(), //day\n        \"h+\": date.getHours(), //hour\n        \"m+\": date.getMinutes(), //minute\n        \"s+\": date.getSeconds(), //second\n    };\n    for (var k in o) {\n        if (new RegExp(\"(\" + k + \")\").test(format))\n            var str = o[k] + \'\'\n        format = format.replace(\n            RegExp.$1,\n            (RegExp.$1.length === 1) ? str : padLeftZero(str)\n        );\n    }\n    return format;\n}\n\nfunction padLeftZero(str) {\n    return (\"00\" + str).substr(str.length)\n}\n\n```\n\n\n\n###  封装axios\n\n```javascript\nimport axios from \'axios\';\nimport { Message } from \'element-ui\';\n\n//携带证书\naxios.defaults.withCredentials = true;\n//设置超时时间\naxios.defaults.timeout = 100000;\n\nexport default {\n\n  //get请求\n  requestGet(url, params = {}) {\n    return new Promise((resolve, reject) => {\n      axios.get(url, params).then(res => {\n        resolve(res.data)\n      }).catch(error => {\n        reject(error)\n      })\n    })\n  },\n\n\n  // post请求\n  requestPost(url, params = {}) {\n    return new Promise((resolve, reject) => {\n      axios.post(url, params).then(res => {\n        resolve(res.data)\n      }).catch(error => {\n        reject(error)\n      })\n    })\n  },\n  // post请求\n  requestPostFormData(url, params = {}) {\n    return new Promise((resolve, reject) => {\n      axios.post(url, params, {\n        headers: { \'Content-Type\': \'multipart/form-data\' }\n      }).then(res => {\n        resolve(res.data)\n      }).catch(error => {\n        reject(error)\n      })\n    })\n  },\n  // delete请求\n  requestDelete(url, params = {}) {\n    return new Promise((resolve, reject) => {\n      axios.delete(url, params).then(res => {\n        resolve(res.data)\n      }).catch(error => {\n        reject(error)\n      })\n    })\n  },\n  // put请求\n  requestPut(url, params = {}) {\n    return new Promise((resolve, reject) => {\n      axios.put(url, params).then(res => {\n        resolve(res.data)\n      }).catch(error => {\n        reject(error)\n      })\n    })\n  }\n}\n\n// 2.请求拦截器\naxios.interceptors.request.use(config => {\n  //发请求前做的一些处理，数据转化，配置请求头，设置token,设置loading等，根据需求去添加\n  return config\n}, error => {\n  Promise.reject(error)\n})\n\n// 3.响应拦截器\naxios.interceptors.response.use(response => {\n  //接收到响应数据并成功后的一些共有的处理，关闭loading等\n\n  return response\n}, error => {\n  /***** 接收到异常响应的处理开始 *****/\n  if (error && error.response) {\n    // 1.公共错误处理\n    // 2.根据响应码具体处理\n    switch (error.response.status) {\n      case 400:\n        error.message = \'错误请求\'\n        break;\n      case 401:\n        error.message = \'未授权，请重新登录\'\n        break;\n      case 403:\n        error.message = \'拒绝访问\'\n        break;\n      case 404:\n        error.message = \'请求错误,未找到该资源\'\n        // window.location.href = \"/NotFound\"\n        break;\n      case 405:\n        error.message = \'请求方法未允许\'\n        break;\n      case 408:\n        error.message = \'请求超时\'\n        break;\n      case 500:\n        error.message = \'服务器端出错\'\n        break;\n      case 501:\n        error.message = \'网络未实现\'\n        break;\n      case 502:\n        error.message = \'网络错误\'\n        break;\n      case 503:\n        error.message = \'服务不可用\'\n        break;\n      case 504:\n        error.message = \'网络超时\'\n        break;\n      case 505:\n        error.message = \'http版本不支持该请求\'\n        break;\n      default:\n        error.message = `连接错误${error.response.status}`\n    }\n  } else {\n    // 超时处理\n    if (JSON.stringify(error).includes(\'timeout\')) {\n      Message.error(\'服务器响应超时，请刷新当前页\')\n    }\n    error.message = \'连接服务器失败\'\n  }\n\n  Message.error(error.message)\n  /***** 处理结束 *****/\n  //如果不需要错误处理，以上的处理过程都可省略\n  return Promise.resolve(error.response)\n})\n\n```\n\n\n\n###  ', '1', '3', 'Vue的通用笔记', 'java-vue', '5', '2021-03-21 11:20:03', '2021-03-21 14:39:39', '0', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/1e6510c902c648739f620a4db4e6056aQQ图片20210320140517.jpg');
INSERT INTO `tb_article` VALUES ('f8dc4a8e-a054-40a9-b4ff-c3c5a343e9f8', '自定义模板-CSS部分', '0feed2d15abb871103ba9ee846aca914', null, null, '4ef92691411d967075fb5e057e988dc8', '##  CSS部分\n\n```css\n.email-length {\n  width: 50% !important;\n}\n.avatar-uploader .el-upload {\n  border: 1px dashed #d9d9d9;\n  border-radius: 6px;\n  cursor: pointer;\n  position: relative;\n  overflow: hidden;\n}\n.avatar-uploader .el-upload:hover {\n  border-color: #409eff;\n}\n.avatar-uploader-icon {\n  font-size: 28px;\n  color: #8c939d;\n  width: 178px;\n  height: 178px;\n  line-height: 178px;\n  text-align: center;\n}\n.avatar {\n  width: 178px;\n  height: 178px;\n  display: block;\n}\n\n.el-tag + .el-tag {\n    margin-left: 10px;\n  }\n  .button-new-tag {\n    margin-left: 10px;\n    height: 32px;\n    line-height: 30px;\n    padding-top: 0;\n    padding-bottom: 0;\n  }\n  .input-new-tag {\n    width: 90px;\n    margin-left: 10px;\n    vertical-align: bottom;\n  }\n```\n\n##  ', '1', '3', 'CSS', 'java-vue', '0', '2021-03-21 13:00:27', '2021-03-21 15:32:25', '0', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/1e6510c902c648739f620a4db4e6056aQQ图片20210320140517.jpg');

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `pinyin` varchar(128) NOT NULL COMMENT '拼音',
  `description` text NOT NULL COMMENT '描述',
  `order` int(11) NOT NULL COMMENT '顺序',
  `status` varchar(1) NOT NULL COMMENT '状态：0表示不使用，1表示正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES ('4ef92691411d967075fb5e057e988dc8', 'spring', 'spring', 'spring框架，非常重要。为什么呢？我最喜欢学Java！！！', '0', '1', '2021-02-17 14:29:46', '2021-03-05 15:59:07', '0');
INSERT INTO `tb_category` VALUES ('600ffcc8d3f0f1836525ed8c1b34aad8', 'springboot', 'springboot', 'springboot微服务', '0', '1', '2021-02-17 14:29:27', '2021-02-17 14:29:27', '0');
INSERT INTO `tb_category` VALUES ('b316fb0a5dd63ec177dc572f28f24b90', 'Java', 'java', 'java编程', '0', '1', '2021-02-17 14:28:21', '2021-02-17 14:28:21', '0');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `parent_content` text COMMENT '父内容',
  `article_id` varchar(255) NOT NULL COMMENT '文章ID',
  `content` text NOT NULL COMMENT '评论内容',
  `user_id` varchar(255) NOT NULL COMMENT '评论用户的ID',
  `user_avatar` varchar(1024) DEFAULT NULL COMMENT '评论用户的头像',
  `user_name` varchar(255) DEFAULT NULL COMMENT '评论用户的名称',
  `state` varchar(1) NOT NULL COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES ('62c74e7af728c8350b941f3b8d45f51b', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论1', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:15:58', '2021-02-18 16:15:58', '1');
INSERT INTO `tb_comment` VALUES ('672630ffa74d9b738df9e79686025940', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论12', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '3', '2021-02-18 16:16:03', '2021-02-18 16:20:47', '0');
INSERT INTO `tb_comment` VALUES ('732b2ddb1d4f6af322207356bf117d8d', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '看看能不能清除缓存1', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '3', '2021-02-27 21:38:06', '2021-03-21 15:04:47', '0');
INSERT INTO `tb_comment` VALUES ('938e096e024124b3060087c1d20ecc3b', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论123456712', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:20', '2021-02-18 16:16:20', '0');
INSERT INTO `tb_comment` VALUES ('97cfde7b6a29f781364fdcbef0d1fc17', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论12345', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:10', '2021-02-18 16:16:10', '0');
INSERT INTO `tb_comment` VALUES ('a0bd63df4150971af76b5d37a55cf538', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论1234567123', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:23', '2021-02-18 16:16:23', '0');
INSERT INTO `tb_comment` VALUES ('b1fabb93f362a2288c04430e670a566e', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论123456', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:12', '2021-02-18 16:16:12', '0');
INSERT INTO `tb_comment` VALUES ('b4e18333a1fd3040a50bcc5ede31de4a', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论123', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:05', '2021-02-18 16:16:05', '0');
INSERT INTO `tb_comment` VALUES ('c17f4630c6178e5bcf890772e23f7fac', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论12345671', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:18', '2021-02-18 16:16:18', '0');
INSERT INTO `tb_comment` VALUES ('dde11a5a838c209e7f1543a507fd6562', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论1234', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:08', '2021-02-18 16:16:08', '0');
INSERT INTO `tb_comment` VALUES ('e17c07a58c0b99119b2d61b68aa21fb6', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '看看能不能清除缓存12', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '3', '2021-02-27 21:38:49', '2021-03-21 15:32:29', '0');
INSERT INTO `tb_comment` VALUES ('ebebf530ad18628f8bfde92b5db4e15e', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '测试评论1234567', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-18 16:16:15', '2021-02-18 16:16:15', '0');
INSERT INTO `tb_comment` VALUES ('fd196352f16da1e070067264977d18ea', null, 'd317fc01-6b58-4b3b-bef2-62b66e914c01', '看看能不能清除缓存', '0feed2d15abb871103ba9ee846aca914', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'admin', '1', '2021-02-27 21:36:16', '2021-03-21 15:32:31', '0');

-- ----------------------------
-- Table structure for tb_daily_view_count
-- ----------------------------
DROP TABLE IF EXISTS `tb_daily_view_count`;
CREATE TABLE `tb_daily_view_count` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '每天浏览量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_daily_view_count
-- ----------------------------

-- ----------------------------
-- Table structure for tb_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_friend_link`;
CREATE TABLE `tb_friend_link` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '友情链接名称',
  `logo` varchar(1024) NOT NULL COMMENT '友情链接logo',
  `url` varchar(1024) NOT NULL COMMENT '友情链接',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `state` varchar(1) NOT NULL COMMENT '友情链接状态:0表示不可用，1表示正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_friend_link
-- ----------------------------
INSERT INTO `tb_friend_link` VALUES ('4cceb6ce99a047649021bd926ccf0c7f', '社会你白哥哥', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/76b6c06764bc4c16a15f96fe9b3ee6afQQ图片20210320140517.jpg', 'http://www.hao123.com', '0', '1', '2021-03-20 21:00:40', '2021-03-20 21:00:40', '0');

-- ----------------------------
-- Table structure for tb_images
-- ----------------------------
DROP TABLE IF EXISTS `tb_images`;
CREATE TABLE `tb_images` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `user_id` varchar(255) NOT NULL COMMENT '用户ID',
  `name` varchar(255) NOT NULL COMMENT '图片名称',
  `url` varchar(1024) NOT NULL COMMENT '路径',
  `content_type` varchar(255) NOT NULL COMMENT '图片类型',
  `state` varchar(1) NOT NULL COMMENT '状态（0表示删除，1表正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_images
-- ----------------------------
INSERT INTO `tb_images` VALUES ('202743706df40e1848687b97542a5192', '0feed2d15abb871103ba9ee846aca914', '头像.gif', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/07/12901ced0d7e4073b657f6d35ad7bbaa头像.gif', 'image/gif', '1', '2021-03-07 11:08:39', '2021-03-07 11:08:39', '0');
INSERT INTO `tb_images` VALUES ('6b14c1b0c1e1fb01942162d0e7b4513c', '0feed2d15abb871103ba9ee846aca914', '肯德基爷爷.jpg', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', 'image/jpeg', '1', '2021-03-20 14:05:30', '2021-03-20 14:05:30', '0');
INSERT INTO `tb_images` VALUES ('6e74b9416ea8c9cdb08c2c6464d5311e', '0feed2d15abb871103ba9ee846aca914', 'IMG20191002163846.jpg', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/07/0a34c96cd979473b8b5e5db047fb5874IMG20191002163846.jpg', 'image/jpeg', '1', '2021-03-07 11:33:22', '2021-03-07 11:33:22', '0');
INSERT INTO `tb_images` VALUES ('7561532252d0cc6a30be299198bddb54', '0feed2d15abb871103ba9ee846aca914', 'IMG_20180702_191147.jpg', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/07/a43c755bab4c4ca7a7a64054fc5293d5IMG_20180702_191147.jpg', 'image/jpeg', '1', '2021-03-07 11:14:25', '2021-03-07 11:14:25', '0');
INSERT INTO `tb_images` VALUES ('924ad7abf7f66e38e7c619e9f02c144c', '0feed2d15abb871103ba9ee846aca914', 'IMG_20180614_110112.jpg', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/07/897f1b9c5b7240b09f21b58e54fedbd8IMG_20180614_110112.jpg', 'image/jpeg', '1', '2021-03-07 11:14:54', '2021-03-07 11:14:54', '0');
INSERT INTO `tb_images` VALUES ('d330667e329eca997f5eba7a129a26a5', '0feed2d15abb871103ba9ee846aca914', 'Screenshot_20180718-123454.jpg', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/07/99b27c8324fd4ba898c3646396dfb574Screenshot_20180718-123454.jpg', 'image/jpeg', '1', '2021-03-07 11:19:13', '2021-03-07 11:19:13', '0');
INSERT INTO `tb_images` VALUES ('f31ce0e6c76f71f3e582dbbfefa87253', '0feed2d15abb871103ba9ee846aca914', '丑照.jpg', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c049d43a0c614970a7e07cdfa6bfec16丑照.jpg', 'image/jpeg', '1', '2021-03-20 21:03:45', '2021-03-20 21:03:45', '0');

-- ----------------------------
-- Table structure for tb_img_looper
-- ----------------------------
DROP TABLE IF EXISTS `tb_img_looper`;
CREATE TABLE `tb_img_looper` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `title` varchar(128) NOT NULL COMMENT '轮播图标题',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `state` varchar(10) NOT NULL COMMENT '状态：0表示不可用，1表示正常',
  `target_url` varchar(1024) DEFAULT NULL COMMENT '目标URL',
  `image_url` varchar(2014) NOT NULL COMMENT '图片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_img_looper
-- ----------------------------

-- ----------------------------
-- Table structure for tb_labels
-- ----------------------------
DROP TABLE IF EXISTS `tb_labels`;
CREATE TABLE `tb_labels` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `name` varchar(32) NOT NULL COMMENT '标签名称',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_labels
-- ----------------------------
INSERT INTO `tb_labels` VALUES ('2082a28addf0a57ef8648882af8bccbe', 'Java', '7', '2021-02-18 13:29:57', '2021-02-18 13:29:57', '0');
INSERT INTO `tb_labels` VALUES ('da80643c4babcc118b6776faf822e415', 'vue', '7', '2021-03-21 10:59:29', '2021-03-21 10:59:29', '0');

-- ----------------------------
-- Table structure for tb_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `tb_refresh_token`;
CREATE TABLE `tb_refresh_token` (
  `id` varchar(255) NOT NULL,
  `refresh_token` text NOT NULL COMMENT 'token',
  `user_id` varchar(255) NOT NULL COMMENT '用户Id',
  `mobile_token_key` varchar(255) DEFAULT NULL COMMENT '移动端的tokenkey',
  `token_key` varchar(255) DEFAULT NULL COMMENT 'token_key ，存放在redis中需要的用到的key',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_refresh_token
-- ----------------------------
INSERT INTO `tb_refresh_token` VALUES ('823197530534838272', 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwZmVlZDJkMTVhYmI4NzExMDNiYTllZTg0NmFjYTkxNCIsImlhdCI6MTYxNjMwNzE4NSwiZXhwIjoxNjE2MzA5Nzc3fQ.LdUzWaK6quZrwXzVdeeYwj-pxLTfyZ05qlpUNzIlDXg', '0feed2d15abb871103ba9ee846aca914', null, 'c1057bbe499a4c95dd5d471f820ba973', '2021-03-21 14:13:06', '2021-03-21 14:13:06');

-- ----------------------------
-- Table structure for tb_settings
-- ----------------------------
DROP TABLE IF EXISTS `tb_settings`;
CREATE TABLE `tb_settings` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `key` varchar(32) NOT NULL COMMENT '键',
  `value` varchar(512) NOT NULL COMMENT '值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_settings
-- ----------------------------
INSERT INTO `tb_settings` VALUES ('081aeae9162b76605b2328ac2cc4639b', 'web_size_keywords', '夕阳醉了', '2021-02-17 20:34:17', '2021-03-20 18:19:53');
INSERT INTO `tb_settings` VALUES ('2ed19451d96ae9bc692cde60fe491d2f', 'web_size_view_count', '1', '2021-02-17 19:45:20', '2021-02-18 14:38:25');
INSERT INTO `tb_settings` VALUES ('56ef6bd6b5486d26b58e738d20e29008', 'web_size_title', '夕阳醉了', '2021-02-17 19:35:15', '2021-03-20 18:19:54');
INSERT INTO `tb_settings` VALUES ('5b921a2517500d49074b2515948567fe', 'web_size_description', '一包烟一杯茶，一个BUG改一天', '2021-02-17 20:34:17', '2021-03-20 18:19:53');
INSERT INTO `tb_settings` VALUES ('b148c544bbdbe211f1a2361be6195bbb', 'has_manager_init_state', '1', '2021-02-14 15:07:01', '2021-02-14 15:07:01');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `roles` varchar(100) NOT NULL COMMENT '角色',
  `avatar` varchar(1024) NOT NULL COMMENT '头像地址',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `sign` varchar(100) DEFAULT NULL COMMENT '签名',
  `state` varchar(1) NOT NULL DEFAULT '1' COMMENT '状态：0表示删除，1表示正常',
  `reg_ip` varchar(32) NOT NULL COMMENT '注册ip',
  `login_ip` varchar(32) NOT NULL COMMENT '登录Ip',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '(0表示不删除，1表示删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('0feed2d15abb871103ba9ee846aca914', 'admin', '$2a$10$7a0NzCo2oXrqanPDqrYA4uMYf0OMyvTiAGSk3r6Pw0mCJUQsXpjsK', 'role_admin', 'https://oldbai-flie.oss-cn-shenzhen.aliyuncs.com/2021/03/20/c4b745715cd44d4eb67d6d3ed9d417d9QQ图片20210320140517.jpg', '1005777562@qq.com', '我就是我，不一样的烟火.我是好人', '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2021-02-14 15:07:01', '2021-03-20 14:05:33', '0');

-- ----------------------------
-- View structure for tb_article_view
-- ----------------------------
DROP VIEW IF EXISTS `tb_article_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `tb_article_view` AS select `tb_article`.`id` AS `id`,`tb_article`.`title` AS `title`,`tb_article`.`user_id` AS `user_id`,`tb_article`.`user_avatar` AS `user_avatar`,`tb_article`.`user_name` AS `user_name`,`tb_article`.`category_id` AS `category_id`,`tb_article`.`type` AS `type`,`tb_article`.`state` AS `state`,`tb_article`.`summary` AS `summary`,`tb_article`.`labels` AS `labels`,`tb_article`.`view_count` AS `view_count`,`tb_article`.`create_time` AS `create_time`,`tb_article`.`update_time` AS `update_time`,`tb_article`.`is_delete` AS `is_delete`,`tb_article`.`cover` AS `cover` from `tb_article` ;

-- ----------------------------
-- View structure for tb_user_view
-- ----------------------------
DROP VIEW IF EXISTS `tb_user_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `tb_user_view` AS select `tb_user`.`id` AS `id`,`tb_user`.`user_name` AS `user_name`,`tb_user`.`roles` AS `roles`,`tb_user`.`avatar` AS `avatar`,`tb_user`.`email` AS `email`,`tb_user`.`sign` AS `sign`,`tb_user`.`state` AS `state`,`tb_user`.`reg_ip` AS `reg_ip`,`tb_user`.`login_ip` AS `login_ip`,`tb_user`.`create_time` AS `create_time`,`tb_user`.`update_time` AS `update_time`,`tb_user`.`is_delete` AS `is_delete` from `tb_user` ;
