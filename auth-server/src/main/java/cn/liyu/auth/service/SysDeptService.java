package cn.liyu.auth.service;

import cn.liyu.auth.constant.AuthStubInfo;
import cn.liyu.auth.constant.DataScopeEnum;
import cn.liyu.auth.entity.SysDept;
import cn.liyu.auth.entity.SysRole;
import cn.liyu.auth.mapper.SysDeptMapper;
import cn.liyu.auth.model.form.DeptForm;
import cn.liyu.auth.model.vo.DeptTreeVo;
import cn.liyu.auth.utils.NodeUtils;
import cn.liyu.auth.utils.mapstruct.DeptConvert;
import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.liyu.auth.constant.AuthConstant.NODE_PATH_SEP;
import static cn.liyu.auth.constant.AuthStubInfo.DEPT_NOT_FOUND;
import static cn.liyu.auth.constant.AuthStubInfo.PARENT_NODE_NOT_FOUND;

@Service
@Slf4j
public class SysDeptService {
    @Autowired
    SysDeptMapper deptMapper;
    @Autowired
    SysRoleService roleService;


    public Set<Integer> getUserDeptId(Integer userId) {
        return this.getUserDeptList(userId)
                .stream()
                .map(SysDept::getId)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的部门集合
     *
     * @param userId
     * @return
     */
    public List<SysDept> getUserDeptList(Integer userId) {
        List<SysRole> userRoles = roleService.getUserRoles(userId);
        Set<SysDept> depts = new HashSet<>();
        SysDept self = this.getSelfByUserId(userId);
        for (SysRole role : userRoles) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case ALL:
                    depts.addAll(this.getSelfAndChildrenList(self.getId()));
                    break;
                case THIS_LEVEL:
                    depts.add(self);
                    break;
                default:
            }
        }
        return new ArrayList<>(depts);
    }


    /**
     * 获取当前及下级部门信息
     *
     * @param sptId 当前部门id
     * @return
     */
    public List<SysDept> getSelfAndChildrenList(Integer sptId) {
        SysDept dept = getSysDept(sptId);
        List<SysDept> deptList = new ArrayList<>();
        List<SysDept> childrenList = deptMapper.selectChildrenList(sptId);
        deptList.add(dept);
        deptList.addAll(childrenList);
        return deptList;
    }

    public List<SysDept> getSelfAndSuperiorList(Integer sptId) {
        SysDept dept = getSysDept(sptId);
        List<SysDept> deptList = new ArrayList<>();
        String path = dept.getPath();
        if (StringUtils.isNotBlank(path)) {
            List<Integer> pathNode = NodeUtils.getPathNode(path);
            List<SysDept> pList = deptMapper.selectByIdList(pathNode);
            deptList.addAll(pList);
        }
        deptList.add(dept);
        return deptList;
    }

    private SysDept getSysDept(Integer sptId) {
        SysDept dept = deptMapper.selectByPrimaryKey(sptId);
        if (dept == null) {
            throw new ApplicationException(DEPT_NOT_FOUND);
        }
        return dept;
    }

    /**
     * 获取用户当前部门信息
     * path 记录父节点路径
     *
     * @param userId 用户
     * @return
     */
    public SysDept getSelfByUserId(Integer userId) {
        return deptMapper.getSelfByUserId(userId);
    }

    public void createDept(DeptForm deptForm) {
        SysDept sysDept = DeptConvert.INSTANCE.formToEntity(deptForm);

        Integer pid = sysDept.getPid();
        String path = null;
        if (pid != null) {
            SysDept deptParent = getSysDept(pid);
            String parentPath = deptParent.getPath();
            if (StringUtils.isNotBlank(parentPath)) {
                path = parentPath + pid + NODE_PATH_SEP;
            } else {
                path = NODE_PATH_SEP + pid + NODE_PATH_SEP;
            }
        }
        sysDept.setPath(path);
        sysDept.setCreateTime(LocalDateTime.now());
        sysDept.setCreateBy(SecurityUtils.getCurrentUsername());
        deptMapper.insertSelective(sysDept);
    }

    public SysDept queryDeptInfo(Integer id) {
        return deptMapper.selectByPrimaryKey(id);
    }


    public DeptTreeVo queryDeptSuperior(Integer id) {
        checkDeptPermissionScope(id);
        List<SysDept> selfAndSuperiorList = this.getSelfAndSuperiorList(id);
        return buildSingleTree(selfAndSuperiorList);
    }

    private DeptTreeVo buildSingleTree(List<SysDept> sysDeptList) {
        DeptTreeVo p = null;
        Set<Integer> idSet = sysDeptList.stream().map(SysDept::getId).collect(Collectors.toSet());
        for (SysDept dept : sysDeptList) {
            if (!idSet.contains(dept.getPid())) {
                p = DeptConvert.INSTANCE.entityToTreeNode(dept);
                break;
            }
        }
        if (p == null) {
            throw new ApplicationException(PARENT_NODE_NOT_FOUND);
        }

        addToParentNode(sysDeptList, p);
        return p;
    }

    private void addToParentNode(List<SysDept> sysDeptList, DeptTreeVo p) {
        for (SysDept dept : sysDeptList) {
            if (dept.getPid().equals(p.getId())) {
                if (p.getChildrenList() == null) {
                    p.setChildrenList(new ArrayList<>());
                }
                List<DeptTreeVo> childrenList = p.getChildrenList();
                DeptTreeVo c = DeptConvert.INSTANCE.entityToTreeNode(dept);
                addToParentNode(sysDeptList, c);
                childrenList.add(c);
            }
        }
    }

    private void checkDeptPermissionScope(Integer id) {
        Set<Integer> currentUserDataScope = SecurityUtils.getCurrentUserDataScope();
        if (!currentUserDataScope.contains(id)) {
            throw new ApplicationException(AuthStubInfo.INSUFFICIENT_PERMISSIONS);
        }
    }

    public DeptTreeVo queryDeptSubordinate(Integer id) {
        checkDeptPermissionScope(id);

        List<SysDept> selfAndSuperiorList = this.getSelfAndChildrenList(id);
        return buildSingleTree(selfAndSuperiorList);
    }


    public List<SysDept> queryDeptList(String nameL) {
        return null;
    }

    public void updateDept(DeptForm deptForm) {

    }

    public void deleteDept(DeptForm deptForm) {

    }

    public Integer getFistNode(Integer id) {
        SysDept sysDept = getSysDept(id);
        String path = sysDept.getPath();
        if (StringUtils.isBlank(path)) {
            return id;
        } else {
            return Integer.valueOf(NODE_PATH_SEP, 1);
        }
    }
}
