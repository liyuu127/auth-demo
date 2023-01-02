package cn.liyu.auth.model.form;

import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class DeptForm {
    @NotNull(message = "id can not be null", groups = {Update.class, Delete.class})
    private Integer id;

    /**
     * 名称
     */
    @NotBlank(message = "name can not be null", groups = {Add.class})
    private String name;

    /**
     * 类型：0顶级，1公司，2部门，3职位
     */
    @NotNull(message = "name can not be null", groups = {Add.class})
    private Integer type;

    /**
     * 父级id,顶级结点为0
     */
    private Integer pid;

    /**
     * 排序优先级
     */
    private Integer sort;

}