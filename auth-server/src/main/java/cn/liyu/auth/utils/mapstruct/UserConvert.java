
package cn.liyu.auth.utils.mapstruct;

import cn.liyu.auth.entity.SysUser;
import cn.liyu.auth.model.form.UserForm;
import cn.liyu.security.model.UserLogin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserLogin entityToUserLogin(SysUser user);


    SysUser userFormToEntity(UserForm user);

}
