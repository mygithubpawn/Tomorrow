# Tomorrow后台
## 项目介绍
- 多人博客+社区
## 项目技术点
- 开发语言：Java
-  MVC框架：SpringBoot
- jar包管理：Maven
-  持久层框架：Mybatis-Plus
-  数据库：Mysql，连接池：Druid
-  缓存：Redis
-  安全框架：SpringSecurity+jwt
-  对象存储：阿里OSS 
### 项目数据库 Sql->employee.sql
- 默认管理员账户：name:admin，password:123
### 您需要修改的地方
- pawn-blog-web->src->main->resources->application-dev.properties
- 数据库配置
![image.png](http://47.99.193.119:8090/upload/2021/03/image-952efc8bb3c44bc69f3162c7f23042f5.png)
- 您的邮箱属性
![image.png](http://47.99.193.119:8090/upload/2021/03/image-2f1d5340c74b403885569fedcab4bbcb.png)
- redis配置
![image.png](http://47.99.193.119:8090/upload/2021/03/image-a6323a88454e445bb5a205f60540ae51.png)
- pawn-blog-common->src->java->com->pawn->blog->utils->OssUtil
- 修改为自己的OSS属性
![image.png](http://47.99.193.119:8090/upload/2021/03/image-4d6b258261574fe4a947d8438efb127c.png)
### Tomorrow前台项目地址：[Tomorrow前台项目](https://github.com/mygithubpawn/TomorrowWeb)
#### Tomorrow交流群：722633922
