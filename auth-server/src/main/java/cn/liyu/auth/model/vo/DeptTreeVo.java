package cn.liyu.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeptTreeVo  {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：0顶级，1公司，2部门，3职位
     */
    private Integer type;

    /**
     * 父级id
     */
    private Integer pid;

    /**
     * 排序优先级
     */
    private Integer sort;

    /**
     * 层级结构路径1|2|5|12|18
     */
    private String path;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptTreeVo> childrenList;
}