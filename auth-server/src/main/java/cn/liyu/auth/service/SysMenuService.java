package cn.liyu.auth.service;

import cn.liyu.auth.entity.SysMenu;
import cn.liyu.auth.mapper.SysMenuMapper;
import cn.liyu.auth.model.form.MenuForm;
import cn.liyu.auth.model.vo.MenuTreeVo;
import cn.liyu.auth.utils.NodeUtils;
import cn.liyu.auth.utils.mapstruct.MenuConvert;
import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.liyu.auth.constant.AuthConstant.NODE_PATH_ROOT_ID;
import static cn.liyu.auth.constant.AuthStubInfo.MENU_NOT_FOUND;

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
        if (NODE_PATH_ROOT_ID != sysMenu.getPid()) {
            SysMenu menu = getMenu(sysMenu.getPid());
            sysMenu.setPath(NodeUtils.getPathFromParent(menu.getPath(), menu.getId()));
        }
        menuMapper.insertSelective(sysMenu);
    }

    public SysMenu queryMenuInfo(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }


    public List<SysMenu> queryMenuList(String title) {
        return menuMapper.selectList(title);
    }

    public void updateMenu(MenuForm menuForm) {
        Integer id = menuForm.getId();
        //更新当前节点及子节点信息
        SysMenu sysMenu = getMenu(id);
        //查询新的父节点信息 比较收是否有做节点变动
        if (menuForm.getPid() == null || Objects.equals(sysMenu.getPid(), menuForm.getPid())) {
            SysMenu menu = MenuConvert.INSTANCE.formToEntity(menuForm);
            menuMapper.updateByPrimaryKeySelective(menu);
            return;
        }
        //更新后前缀
        String path = "";
        if (NODE_PATH_ROOT_ID != menuForm.getPid()) {
            //获取新节点的前缀
            SysMenu menuParent = getMenu(menuForm.getPid());
            path = NodeUtils.getPathFromParent(menuParent.getPath(), menuParent.getId());
        }
        //之前父级前缀
        String prePath = "";
        if (StringUtils.isNotBlank(sysMenu.getPath())) {
            prePath = sysMenu.getPath();
        }

        SysMenu menu = MenuConvert.INSTANCE.formToEntity(menuForm);
        menu.setPath(StringUtils.isNotBlank(path) ? path : null);

        //获取所有子节点
        List<SysMenu> childrenList = getChildrenList(id);
        if (!childrenList.isEmpty()) {
            for (SysMenu c : childrenList) {
                //除去旧的前缀
                String children = NodeUtils.getChildrenPath(path, prePath, c.getPath());
                c.setPath(children);
            }
        }
        if (!childrenList.isEmpty()) {
            childrenList.add(menu);
            menuMapper.updateBatchSelective(childrenList);
        } else {
            menuMapper.updateByPrimaryKeySelective(menu);
        }
    }

    /**
     * 获取子菜单
     *
     * @param id
     * @return
     */
    private List<SysMenu> getChildrenList(Integer id) {
        return menuMapper.selectChildrenList(id);
    }


    private SysMenu getMenu(Integer id) {
        SysMenu sysMenu = menuMapper.selectByPrimaryKey(id);
        if (sysMenu == null) {
            throw new ApplicationException(MENU_NOT_FOUND);
        }
        return sysMenu;
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
