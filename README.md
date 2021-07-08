# MyLittleBlogSystem-Java
An LittleBlogSystem realized by java(SpringBoot)
该项目是一个基于SpringBoot的小型博客系统。运行该项目需要搭建底层的数据库，运行Redis服务器，和Zookeeper服务器。
首先启动provider-mysql，再启动provider-redis，最后启动客户端client即可正常运行。
访问localhost:8091即可进入博客。

数据库的创建代码如下：
```sql
CREATE DATABASE zhz_blog;
CREATE TABLE user_account(
	user_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	user_account VARCHAR(20) NOT NULL UNIQUE,
	user_password VARCHAR(20) NOT NULL
);
CREATE TABLE article_comments(
	comment_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	comment_article_id INT NOT NULL,
	comment_owner VARCHAR(20) NOT NULL,
	comment_contents TEXT
);
CREATE TABLE user_articles(
	article_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	article_name VARCHAR(50)NOT NULL,
	article_content TEXT,
	article_owner VARCHAR(20)
);
```
