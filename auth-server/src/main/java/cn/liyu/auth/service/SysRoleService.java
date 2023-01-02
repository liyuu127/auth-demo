package cn.liyu.auth.service;

import cn.liyu.auth.constant.AuthStubInfo;
import cn.liyu.auth.entity.SysRole;
import cn.liyu.auth.entity.SysRoleMenu;
import cn.liyu.auth.entity.SysUser;
import cn.liyu.auth.mapper.SysRoleMapper;
import cn.liyu.auth.mapper.SysRoleMenuMapper;
import cn.liyu.auth.model.form.RoleForm;
import cn.liyu.auth.utils.mapstruct.RoleConvert;
import cn.liyu.base.constant.CommonConstant;
import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysRoleService {
    @Autowired
    SysRoleMapper roleMapper;
    @Autowired
    SysRoleMenuMapper roleMenuMapper;

    /**
     * 获取当前用户对应的角色列表
     *
     * @param userId
     * @return
     */
    public List<SysRole> getUserRoles(Integer userId) {
        return roleMapper.getUserRoleList(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleForm roleForm) {
        checkRoleUniqueField(roleForm);

        SysRole role = RoleConvert.INSTANCE.formToEntity(roleForm);
        roleMapper.insertSelective(role);

        if (roleForm.getMenuIdList() != null && !roleForm.getMenuIdList().isEmpty()) {
            bathSaveRoleMenu(roleForm, role);
        }
    }

    private void checkRoleUniqueField(RoleForm roleForm) {
        if (StringUtils.isNotBlank(roleForm.getName())) {
            roleMapper.selectByName(roleForm.getName())
                    .ifPresent(role -> {
                        if (roleForm.getId() != null && !role.getId().equals(roleForm.getId())) {
                            throw new ApplicationException(AuthStubInfo.ROLE_NAME_EXIST);
                        }
                    });
        }
    }


    public PageInfo<SysRole> queryRoleList(String nameL, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<SysRole> sysRoleList = roleMapper.selectList(nameL);
        return new PageInfo<>(sysRoleList);
    }

    public SysRole queryRoleInfo(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public void updateRole(RoleForm roleForm) {
        checkRoleUniqueField(roleForm);
        SysRole role = RoleConvert.INSTANCE.formToEntity(roleForm);
        role.setUpdateBy(SecurityUtils.getCurrentUsername());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);

        if (roleForm.getMenuIdList() != null && !roleForm.getMenuIdList().isEmpty()) {
            roleMenuMapper.deleteByRoleId(role.getId());
            bathSaveRoleMenu(roleForm, role);
        }
    }

    private void bathSaveRoleMenu(RoleForm roleForm, SysRole role) {
        Integer rId = role.getId();
        List<SysRoleMenu> roleMenus = roleForm.getMenuIdList()
                .stream()
                .map(id -> {
                    SysRoleMenu sm = new SysRoleMenu();
                    sm.setRoleId(rId);
                    sm.setMenuId(id);
                    return sm;
                }).collect(Collectors.toList());
        roleMenuMapper.batchInsert(roleMenus);
    }

    @Transactional
    public void deleteRole(RoleForm roleForm) {

        List<SysUser> sysUsers = roleMapper.selectUserInRole(roleForm.getId());
        if (!sysUsers.isEmpty()) {
            new ApplicationException(AuthStubInfo.ROLE_DELETE_ERROR);
        }

        SysRole role = RoleConvert.INSTANCE.formToEntity(roleForm);
        role.setDeleted(CommonConstant.DELETED_YES);
        role.setUpdateBy(SecurityUtils.getCurrentUsername());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
        roleMenuMapper.deleteByRoleId(role.getId());
    }
}
