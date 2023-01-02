package cn.liyu.auth.mapper;

import cn.liyu.auth.entity.SysUserRole;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserRoleMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(SysUserRole record);

    int insertOrUpdate(SysUserRole record);

    int insertOrUpdateSelective(SysUserRole record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysUserRole record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    SysUserRole selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysUserRole record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysUserRole record);

    int updateBatch(List<SysUserRole> list);

    int updateBatchSelective(List<SysUserRole> list);

    int batchInsert(@Param("list") List<SysUserRole> list);

    void deleteByUserId(@Param("userId") Integer userId);
}