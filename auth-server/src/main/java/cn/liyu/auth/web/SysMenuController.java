package cn.liyu.auth.web;

import cn.liyu.auth.entity.SysMenu;
import cn.liyu.auth.model.form.MenuForm;
import cn.liyu.auth.model.vo.MenuTreeVo;
import cn.liyu.auth.service.SysMenuService;
import cn.liyu.auth.service.SysUserService;
import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import cn.liyu.security.annotation.AnonymousAccess;
import cn.liyu.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    SysMenuService menuService;

    @AnonymousAccess
    @PostMapping("/add")
//    @PreAuthorize("@el.check('menu:add')")
    public void createMenu(@Validated(Add.class) @RequestBody MenuForm menuForm) {
        menuService.createMenu(menuForm);
    }

    /**
     * 单个信息
     *
     * @param id
     * @return
     */
    @GetMapping("/info")
    @PreAuthorize("@el.check('menu:info')")
    public SysMenu queryMenuInfo(@RequestParam(required = true) Integer id) {
        return menuService.queryMenuInfo(id);
    }

    /**
     * 查询用户上级及当前信息
     *
     */
    @GetMapping("/tree")
    @PreAuthorize("@el.check('menu:tree')")
    public List<MenuTreeVo> queryTree() {
        return menuService.queryTree();
    }



    @GetMapping("/list")
    @PreAuthorize("@el.check('menu:list')")
    public List<SysMenu> queryMenuList(@RequestParam(required = false) String title) {
        return menuService.queryMenuList(title);
    }

    @PostMapping("/update")
    @PreAuthorize("@el.check('menu:update')")
    public void updateMenu(@Validated(Update.class) @RequestBody MenuForm menuForm) {
        menuService.updateMenu(menuForm);
    }

    @PostMapping("/delete")
    @PreAuthorize("@el.check('menu:delete')")
    public void deleteMenu(@Validated(Delete.class) @RequestBody MenuForm menuForm) {
        menuService.deleteMenu(menuForm);
    }
}
