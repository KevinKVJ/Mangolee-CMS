package org.mangolee.service.impls;

import org.mangolee.service.AuthService;

public class AuthServiceImpl implements AuthService {

    /**
     * 业务层：登录合法性检查
     *
     * @param username 用户名
     * @param password 密码
     * @return 制作Token所需的UserInfo
     */
    @Override
    public Object checkLogin(String username, String password) {
        return null;
    }
}
