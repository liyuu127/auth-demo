package cn.liyu.auth.mapper;

import cn.liyu.auth.entity.SysRole;

import java.util.List;
import java.util.Optional;

import cn.liyu.auth.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRoleMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(SysRole record);

    int insertOrUpdate(SysRole record);

    int insertOrUpdateSelective(SysRole record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysRole record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    SysRole selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysRole record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysRole record);

    int updateBatch(List<SysRole> list);

    int updateBatchSelective(List<SysRole> list);

    int batchInsert(@Param("list") List<SysRole> list);

    List<SysRole> getUserRoleList(@Param("userId") Integer userId);

    Optional<SysRole> selectByName(@Param("name") String name);

    List<SysRole> selectList(@Param("nameL") String nameL);

    /**
     * 查询 角色下的用户列表
     *
     * @param roleId 角色id
     * @return SysUser list
     */
    List<SysUser> selectUserInRole(@Param("roleId") Integer roleId);
}