package cn.liyu.auth.service;

import cn.liyu.auth.constant.AuthStubInfo;
import cn.liyu.auth.entity.SysUser;
import cn.liyu.auth.entity.SysUserRole;
import cn.liyu.auth.mapper.SysUserMapper;
import cn.liyu.auth.mapper.SysUserRoleMapper;
import cn.liyu.auth.model.form.UserForm;
import cn.liyu.auth.utils.mapstruct.UserConvert;
import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.model.UserLogin;
import cn.liyu.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserService {
    @Autowired
    SysUserMapper userMapper;
    @Autowired
    SysUserRoleMapper userRoleMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 获取登录用户信息
     *
     * @param username
     * @return
     */
    public UserLogin getLoginUser(String username) {
        SysUser sysUser = userMapper.getOneByUsername(username);
        if (sysUser == null) {
            return null;
        }
        return UserConvert.INSTANCE.entityToUserLogin(sysUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserForm userForm) {

        checkUserUniqueField(userForm);

        SysUser sysUser = UserConvert.INSTANCE.userFormToEntity(userForm);
        sysUser.setPassword(passwordEncoder.encode(userForm.getPassword()));
//        sysUser.setCreateBy(SecurityUtils.getCurrentUsername());
        sysUser.setCreateTime(LocalDateTime.now());
        userMapper.insertSelective(sysUser);

        //role
        List<Integer> roleIdList = userForm.getRoleIdList();
        if (roleIdList != null && !roleIdList.isEmpty()) {
            Integer userId = sysUser.getId();
            bathInsertUserRole(roleIdList, userId);
        }

    }

    private void bathInsertUserRole(List<Integer> roleIdList, Integer userId) {
        List<SysUserRole> userRoles = roleIdList.stream()
                .map(roleId -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                }).collect(Collectors.toList());

        userRoleMapper.batchInsert(userRoles);
    }


    private void checkUserUniqueField(UserForm userForm) {
        if (StringUtils.isNotBlank(userForm.getPhone())) {
            userMapper.selectByLowerUsernameOrPhone(null, userForm.getPhone())
                    .ifPresent(userT -> {
                        if (userForm.getId() != null && !userT.getId().equals(userForm.getId())) {
                            throw new ApplicationException(AuthStubInfo.TEL_EXIST);
                        }
                    });
        }

        if (StringUtils.isNotBlank(userForm.getUsername())) {
            userMapper.selectByLowerUsernameOrPhone(userForm.getUsername(), null)
                    .ifPresent(userT -> {
                        if (userForm.getId() != null && !userT.getId().equals(userForm.getId())) {
                            throw new ApplicationException(AuthStubInfo.USERNAME_EXIST);
                        }
                    });
        }
    }

    @Transactional
    public void updateUser(UserForm userForm) {

        SysUser sysUser = UserConvert.INSTANCE.userFormToEntity(userForm);
        sysUser.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysUser.setUpdateTime(LocalDateTime.now());
        userMapper.updateByPrimaryKeySelective(sysUser);

        if (userForm.getRoleIdList() != null && userForm.getRoleIdList().isEmpty()) {
            userRoleMapper.deleteByUserId(userForm.getId());
            bathInsertUserRole(userForm.getRoleIdList(), userForm.getId());
        }

    }
}
