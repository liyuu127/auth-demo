package cn.liyu.security.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.liyu.security.constant.SecurityConstant.USER_ENABLE_TRUE;

/**
 * 授权用户信息
 */
@Data
@AllArgsConstructor
public class JwtUser implements UserDetails {

    private final UserLogin user;

    private final Set<Integer> dataScopes;

    private final List<Authority> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(Authority::getAuthority).collect(Collectors.toSet());
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getUsername();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return ObjectUtils.equals(USER_ENABLE_TRUE, user.getEnabled());
    }
}
