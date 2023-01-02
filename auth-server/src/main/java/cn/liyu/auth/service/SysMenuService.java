package cn.liyu.auth.service;

import cn.liyu.auth.entity.SysMenu;
import cn.liyu.auth.mapper.SysMenuMapper;
import cn.liyu.auth.model.form.MenuForm;
import cn.liyu.auth.model.vo.MenuTreeVo;
import cn.liyu.auth.utils.mapstruct.MenuConvert;
import cn.liyu.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return menuMapper.getUserMenuList(userId);
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

    public void createMenu(MenuForm menuForm) {
        SysMenu sysMenu = MenuConvert.INSTANCE.formToEntity(menuForm);
        menuMapper.insertSelective(sysMenu);
    }

    public SysMenu queryMenuInfo(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }


    public List<SysMenu> queryMenuList(String title) {
        return menuMapper.selectList(title);
    }

    public void updateMenu(MenuForm menuForm) {

    }

    public void deleteMenu(MenuForm menuForm) {

    }

    public List<MenuTreeVo> queryTree() {
        List<SysMenu> userMenuList = getUserMenuList(SecurityUtils.getCurrentUserId());

        return buildTree(userMenuList);
    }

    public List<MenuTreeVo> buildTree(List<SysMenu> userMenuList) {
        List<MenuTreeVo> menuTreeVos = new ArrayList<>();
        Set<Integer> idSet = userMenuList.stream().map(SysMenu::getId).collect(Collectors.toSet());
        for (SysMenu menu : userMenuList) {
            if (!idSet.contains(menu.getPid())) {
                MenuTreeVo c = MenuConvert.INSTANCE.entityToTreeNode(menu);
                addToParentNode(userMenuList, c);
                menuTreeVos.add(c);
            }
        }
        return menuTreeVos;
    }

    private void addToParentNode(List<SysMenu> menuList, MenuTreeVo p) {
        for (SysMenu menu : menuList) {
            if (menu.getPid().equals(p.getId())) {
                if (p.getChildrenList() == null) {
                    p.setChildrenList(new ArrayList<>());
                }
                List<MenuTreeVo> childrenList = p.getChildrenList();
                MenuTreeVo c = MenuConvert.INSTANCE.entityToTreeNode(menu);
                addToParentNode(menuList, c);
                childrenList.add(c);
            }
        }
    }
}
