package cn.liyu.biz.web;

import cn.liyu.log.annotation.Log;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Log("测试用户权限")
    @GetMapping("/user")
    public JwtUser getUser() {
        return SecurityUtils.getCurrentUser();
    }
}
