
package cn.liyu.auth.utils.mapstruct;

import cn.liyu.auth.entity.SysDept;
import cn.liyu.auth.entity.SysRole;
import cn.liyu.auth.model.form.DeptForm;
import cn.liyu.auth.model.form.RoleForm;
import cn.liyu.auth.model.vo.DeptTreeVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeptConvert {
    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    SysDept formToEntity(DeptForm deptForm);
    DeptTreeVo entityToTreeNode(SysDept dept);

}
