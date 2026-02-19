## 技术栈
```text
构建环境 jdk21 kotlin2 + gradle9 
spring boot 4.0
spring web socket
spring batch
spring security
spring data redis
postgresql
jooq 数据库依赖，相较于mybits plus和jpa， 个人比较喜欢jooq，写sql是一种享受
apache poi
aliyun oss
```
## 已实现后台功能
```text
1. 登录
2. 权限（菜单权限、操作权限）
3. 管理员
4. 操作日志
5. 文件管理
6. 异步消息（websocket）
7. 消息中心
```
## 自定义配置
```text 
# redis token
auth.token.expire=123 缓存秒数
auth.token.prefix=token 缓存前缀

# aliyun oss
aliyun.oss.endpoint=
aliyun.oss.access-key=
aliyun.oss.access-secret=
aliyun.oss.bucket-name=
aliyun.oss.upload-prefix=
```
## 模块介绍：分为4层 api 顶层用户入口 >> core 业务逻辑 >> data 数据库 >> base 基础设施
```text
api-admin 后台模块依赖core  提供http、websocket服务
|——— batch 批量处理模块 目前实现异步启动任务，导出表格功能 apache poi
|——— config 配置 security 跨域 websocket
|——— constants 常量
|——— controller 控制器
|——— dto 
|——— event  事件
|——— listener 监听器
|——— middleware 中间件 过滤器 与 拦截器
|——— security auth 过滤器 与 异常处理
└──— AdminApplication 启动类

api-xxx 其它api模块可根据需求自行创建

base 基础模块底层支持 第三方与全局配置，jackson、oss、redis、线程池等等
|——— config 配置
|——— dto
|——— exception 异常类
|——— provider 服务提供者
|——— serializer 序列化
└──— utils 工具类

core 业务模块依赖data 领域服务、应用服务、数据转换
|——— admin 后台服务
|     —— domain 领域模型
|     —— dto
|         —— req 请求
|         —— res 响应
|         —— bo 业务对象 
|     —— query 查询构造
|     —— security 
|     —— service
└──— config 配置

data 数据模块依赖base 枚举、字段类型转换等等。用的jooq 自动生成实体，不用自定义，
|——— admin 后台
|     —— enums 枚举
|     —— converter 字段类型转换
└──— converter 通用字段转换
```