package cn.liyu.auth.web;

import cn.liyu.auth.entity.SysRole;
import cn.liyu.auth.model.form.RoleForm;
import cn.liyu.auth.model.form.UserForm;
import cn.liyu.auth.service.SysRoleService;
import cn.liyu.auth.service.SysUserService;
import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import cn.liyu.security.annotation.AnonymousAccess;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class SysRoleController {
    @Autowired
    SysRoleService roleService;


    @AnonymousAccess
    @PostMapping("/add")
//    @PreAuthorize("@el.check('role:add')")
    public void createRole(@Validated(Add.class) @RequestBody RoleForm roleForm) {
        roleService.createRole(roleForm);
    }


    @GetMapping("/info")
    @PreAuthorize("@el.check('role:info')")
    public SysRole queryRoleInfo(@RequestParam(required = true) Integer id) {
        return roleService.queryRoleInfo(id);
    }

    @GetMapping("/list")
    @PreAuthorize("@el.check('role:list')")
    public PageInfo<SysRole> queryRoleList(@RequestParam(required = false) String nameL,
                                           @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return roleService.queryRoleList(nameL, pageNo, pageSize);
    }

    @PostMapping("/update")
    @PreAuthorize("@el.check('role:update')")
    public void updateRole(@Validated(Update.class) @RequestBody RoleForm roleForm) {
        roleService.updateRole(roleForm);
    }

    @PostMapping("/delete")
    @PreAuthorize("@el.check('role:delete')")
    public void deleteRole(@Validated(Delete.class) @RequestBody RoleForm roleForm) {
        roleService.deleteRole(roleForm);
    }
}
