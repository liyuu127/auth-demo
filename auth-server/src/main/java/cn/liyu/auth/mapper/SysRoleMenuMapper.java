package cn.liyu.auth.mapper;

import cn.liyu.auth.entity.SysRoleMenu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRoleMenuMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(SysRoleMenu record);

    int insertOrUpdate(SysRoleMenu record);

    int insertOrUpdateSelective(SysRoleMenu record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysRoleMenu record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    SysRoleMenu selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysRoleMenu record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysRoleMenu record);

    int updateBatch(List<SysRoleMenu> list);

    int updateBatchSelective(List<SysRoleMenu> list);

    int batchInsert(@Param("list") List<SysRoleMenu> list);

    void deleteByRoleId(@Param("roleId") Integer roleId);
}