package cn.liyu.auth.web;

import cn.liyu.auth.model.form.UserForm;
import cn.liyu.auth.service.SysUserService;
import cn.liyu.base.validated.Add;
import cn.liyu.security.annotation.AnonymousAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    SysUserService userService;


    @AnonymousAccess
    @PostMapping("/add")
//    @PreAuthorize("@el.check('user:add')")
    public void createUser(@Validated(Add.class) @RequestBody UserForm userForm) {
        userService.createUser(userForm);
    }
}
