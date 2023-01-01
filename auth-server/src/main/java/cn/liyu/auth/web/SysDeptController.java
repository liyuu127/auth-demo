package cn.liyu.auth.web;

import cn.liyu.auth.entity.SysDept;
import cn.liyu.auth.model.form.DeptForm;
import cn.liyu.auth.model.form.DeptForm;
import cn.liyu.auth.model.vo.DeptTreeVo;
import cn.liyu.auth.service.SysDeptService;
import cn.liyu.auth.service.SysDeptService;
import cn.liyu.auth.service.SysUserService;
import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import cn.liyu.security.annotation.AnonymousAccess;
import cn.liyu.security.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class SysDeptController {
    @Autowired
    SysDeptService deptService;

    @Autowired
    SysUserService userService;

    @AnonymousAccess
    @PostMapping("/add")
//    @PreAuthorize("@el.check('dept:add')")
    public void createDept(@Validated(Add.class) @RequestBody DeptForm deptForm) {
        deptService.createDept(deptForm);
    }

    /**
     * 单个部门信息
     *
     * @param id
     * @return
     */
    @GetMapping("/info")
    @PreAuthorize("@el.check('dept:info')")
    public SysDept queryDeptInfo(@RequestParam(required = true) Integer id) {
        return deptService.queryDeptInfo(id);
    }

    /**
     * 查询用户上级及当前信息
     *
     * @param id
     * @return
     */
    @GetMapping("/superior")
    @PreAuthorize("@el.check('dept:superior')")
    public DeptTreeVo queryDeptSuperior(@RequestParam(required = false) Integer id) {
        if (id == null) {
            id = SecurityUtils.getCurrentUser().getUser().getDeptId();
        }
        return deptService.queryDeptSuperior(id);
    }

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    @GetMapping("/subordinate")
    @PreAuthorize("@el.check('dept:subordinate')")
    public DeptTreeVo queryDeptSubordinate(@RequestParam(required = false) Integer id) {
        if (id == null) {
            id = SecurityUtils.getCurrentUser().getUser().getDeptId();
        }
        return deptService.queryDeptSubordinate(id);
    }

    @GetMapping("/list")
    @PreAuthorize("@el.check('dept:list')")
    public List<SysDept> queryDeptList(@RequestParam(required = false) String nameL) {
        return deptService.queryDeptList(nameL);
    }

    @PostMapping("/update")
    @PreAuthorize("@el.check('dept:update')")
    public void updateDept(@Validated(Update.class) @RequestBody DeptForm deptForm) {
        deptService.updateDept(deptForm);
    }

    @PostMapping("/delete")
    @PreAuthorize("@el.check('dept:delete')")
    public void deleteDept(@Validated(Delete.class) @RequestBody DeptForm deptForm) {
        deptService.deleteDept(deptForm);
    }
}
