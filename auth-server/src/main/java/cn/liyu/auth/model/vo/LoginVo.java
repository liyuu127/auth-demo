package cn.liyu.auth.model.vo;

import cn.liyu.security.model.JwtUser;
import lombok.Data;

@Data
public class LoginVo {
    private String token;
    private JwtUser jwtUser;
}
