
```angular2html
# redis token
auth.token.expire=token 缓存时间
auth.token.prefix= token 缓存前缀

# aliyun oss
aliyun.oss.endpoint=
aliyun.oss.access-key=
aliyun.oss.access-secret=
aliyun.oss.bucket-name=
aliyun.oss.upload-prefix=
目录结构 
spring boot kotlin 
api-admin 模块 提供 http 接口服务 依赖core
base  模块 通用的 
core  业务模块 领域服务：负责核心业务规则（状态校验、密码验证）、跨实体逻辑。
core.service 应用服务：负责流程编排、事务控制、DTO 转换。 
core.domain.model：解决“我这个对象自己正不正常”的问题。
core.domain.service：解决“这几个东西合在一起合不合法”的问题。
data 数据模块 枚举  依赖base
data.enums
data.result  




模块
api-admin
core (Application) 应用服务
core (Domain)	领域服务
data	
```


在处理阿里短信、腾讯短信等多平台接入时，最成熟的方案是使用 策略模式 (Strategy Pattern)。
这能保证你的核心业务逻辑（core）不被具体的第三方 SDK 绑定，未来增加新平台（如华为云、七牛云）时无需修改业务代码。