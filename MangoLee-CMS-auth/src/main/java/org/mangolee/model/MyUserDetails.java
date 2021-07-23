package org.mangolee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

//继承UserDetails，用于过度从数据库获得的数据，方便Spring security进行登录校验
// 包含账号密码
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDetails implements UserDetails {

    Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    //获得账户密码，与逻辑删除
    public MyUserDetails(String username, String password, Boolean enable) {
        this.username=username;
        this.password=password;
        this.enabled=enable;
    }


    //权限列表，暂未实现
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //返回账户是否被逻辑删除
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}