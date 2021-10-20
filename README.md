# tgtools-security
## 描述  
提供一个 基于 spring security 的 安全库
对于常见的 安全漏洞进行验证防护
如：重放、xss、referer、origin、Host


### csrf 启用功能
```
//使用注解方式 加载功能
@TgToolsEnableSecurityCsrf
```

### csrf常用配置  
使用配置方式设置功能  

```
# Referer 验证 忽略的路径 如：/a,/b,/**
app.security.csrf.ignore-path:
# Referer 验证 白名单 如：http://192.168.1.1:8080,http://192.168.1.1:8081
app.security.csrf.write-list:
# Origin 验证  忽略的路径 如：/a,/b,/**
app.security.csrf.origin.ignore-path:
# Origin 验证  白名单 如：http://192.168.1.1:8080,http://192.168.1.1:8081
app.security.csrf.origin.write-list:
# Host 验证  白名单 如：192.168.1.1:8080,192.168.1.1:8081
app.security.csrf.host.white-list:
```

### xss 启用功能
```
//使用注解方式 加载功能
@TgToolsToolsEnableSecurityXss
```

### xss 常用配置
使用配置方式设置功能,目前仅能验证Json 类型字符串属性的值  

```
# Xss 验证 忽略的路径 如：/a,/b,/**
app.security.xss.ignore-path:
```




### 重放攻击 验证 启用功能
```
//开启 EhCache 如果项目已有EhCache 可忽略
@TgToolsEnableEhCacheManager

//使用 EhCache 进行防护  二选一
@TgToolsEnableEhCacheReplayAttack

//ReplayAttack 方式进行防护 二选一
@TgToolsEnableRedisCacheReplayAttack
```
### 重放攻击 常用配置
使用配置方式设置功能,目前仅能验证Json 类型字符串属性的值

```
#加密方式
app.security.replay-attack.encrypt-id:
#密钥
app.security.replay-attack.encrypt-key:
#cache name 缓存的名称 避免冲突
app.security.replay-attack.cache-name:
#过期时间
app.security.replay-attack.cache-ttl:
#是否启用时间戳验证
app.security.replay-attack.use-valid-time:
#允许的时间戳 间隔
app.security.replay-attack.valid-time-second:
#忽略验证的path
app.security.replay-attack.ignore-path:

```
