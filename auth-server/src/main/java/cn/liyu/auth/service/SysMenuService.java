package cn.liyu.auth.service;

import cn.liyu.auth.entity.SysMenu;
import cn.liyu.auth.mapper.SysMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;


    /**
     * 获取用户角色菜单
     *
     * @param userId
     * @return
     */
    public List<SysMenu> getUserMenuList(Integer userId) {
        List<SysMenu> menuList = menuMapper.getUserMenuList(userId);
        return menuList;
    }

    /**
     * 获取用户权限集合
     *
     * @param userId
     * @return
     */
    public Set<String> getUserPermission(Integer userId) {
        return this.getUserMenuList(userId)
                .stream()
                .map(SysMenu::getPermission)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

}
