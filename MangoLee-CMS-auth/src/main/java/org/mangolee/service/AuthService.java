package org.mangolee.service;

public interface AuthService {
    // TODO 调用数据库服务相关API，来验证用户名密码是否正确且完备（包含多个节点的判断，如用户被删除或停用等）
    //  ，完备则返回包含制作Token所需要的UserInfo

    /**
     * 业务层：登录合法性检查
     * @param username 用户名
     * @param password 密码
     * @return 制作Token所需的UserInfo
     */
    Object checkLogin(String username, String password);
}
