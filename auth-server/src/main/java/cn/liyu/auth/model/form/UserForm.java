package cn.liyu.auth.model.form;

import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserForm {
    /**
     * 用户表主键
     */
    @NotNull(message = "id can not bu null", groups = {Update.class, Delete.class})
    private Integer id;

    @NotNull(message = "deptId can not bu null", groups = {Add.class})
    private Integer deptId;

    /**
     * 用户名
     */
    @NotNull(message = "username can not bu null", groups = {Add.class})
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别:0女性,1男性
     */
    private Byte gender;

    /**
     * 手机号码
     */
    @NotBlank(message = "phone can not bu null", groups = {Add.class})
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像网址
     */
    private String avatarUrl;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用:0禁用,1启用
     */
    private Byte enabled;

    /**
     * 角色列表
     */
    List<Integer> roleIdList;

}
