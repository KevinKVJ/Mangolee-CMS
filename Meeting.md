## Gateway对auth的JWT需求
* Login正确时颁发JWT 并收录一份入Redis进行周期管理
   + interface: `Token login(String User, String Pwd);`
* 验证接收的JWT是否有效(Redis是否收录)，是否过期,进行自动续期
   + interface: `Result tokenVerify(Token JWT);`
   + `Result`需要反映Token是否有效，若有效是否过期(谷歌token自动续期)
* 根据JWT获取User信息(若向后传递User)
   + interface: `User getUserFromJWT(Token JWT);`
* 获取单个用户的权限角色
  + interface: `String getUserCharacter(Token JWT);`
* Logout时将Redis过期
   + interface `Result logout(Token JWT)`
   + `Result`同上verify
  
## 添加
* 在gateway上鉴权
* 鉴权服务
* 加权限列表redis

frontend ->service1 ->`service2`

## 服务feign包装和nacos发现
* 添加open feign依赖
* entity 和 service interface 放provider
*  consumer 依赖 Provider
* Service interface 添加feign支持
* Provide 和 consumer 添加配置暴露自身给nacos

## eleven
+ 整合gateway，放行（注册），拦截（登录)
+ 服务器加用户securitykey
+ 服务器部署nacos
+ 鉴权服务

## Miao
+ Redis部署，微服务化（openfein provider）
+ 异常处理模块补充
+ 分布式事务
+ 部署到nacos

## Shen
+ gateway接口
+ Security封装jwt
+ 部署到nacos
