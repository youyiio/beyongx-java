# mvn使用手册

## MVM项目相关

### 发布到mvncenter流程

环境准备

* sonatype账号注册
* sonatype新建发布
* gpg4win生成密钥
* 发布公钥到GPS key-servers，
  > gpg --keyserver hkp://keyserver.ubuntu.com:11371 --send-keys

### 编译

> mvn compile

### 测试

> mvn 

### 打包

> mvn package

> mvn package -Dmaven.test.skip=true

> mvn clean package -Dmaven.test.skip=true

### 安装

> mvn install

> mvn install -Dmaven.test.skip=true

### 部署jar包

> mvn clean deploy

> mvn clean deploy -Dmaven.test.skip=true

> mvn clean deploy –P release -Dgpg.passphrase=xxx

> mvn clean deploy -Dmaven.test.skip=true –P release -Dgpg.passphrase=xxx


## SpringBoot相关

Spring Boot Maven plugin的5个Goals

spring-boot:repackage，默认goal。在mvn package之后，再次打包可执行的jar/war，同时保留mvn package生成的jar/war为.origin
spring-boot:run，运行Spring Boot应用
spring-boot:start，在mvn integration-test阶段，进行Spring Boot应用生命周期的管理
spring-boot:stop，在mvn integration-test阶段，进行Spring Boot应用生命周期的管理
spring-boot:build-info，生成Actuator使用的构建信息文件build-info.properties


### SpringBoot项目运行

> mvn spring-boot:run

