
package cn.liyu.auth.utils.mapstruct;

import cn.liyu.auth.entity.SysRole;
import cn.liyu.auth.entity.SysUser;
import cn.liyu.auth.model.form.RoleForm;
import cn.liyu.auth.model.form.UserForm;
import cn.liyu.security.model.UserLogin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleConvert {
    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    SysRole formToEntity(RoleForm role);

}
