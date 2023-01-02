
package cn.liyu.auth.utils.mapstruct;

import cn.liyu.auth.entity.SysMenu;
import cn.liyu.auth.model.form.MenuForm;
import cn.liyu.auth.model.vo.MenuTreeVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuConvert {
    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    SysMenu formToEntity(MenuForm menuForm);

    MenuTreeVo entityToTreeNode(SysMenu menu);

}
