package cn.liyu.auth.mapper;

import cn.liyu.auth.entity.SysMenu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysMenuMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(SysMenu record);

    int insertOrUpdate(SysMenu record);

    int insertOrUpdateSelective(SysMenu record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysMenu record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    SysMenu selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysMenu record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysMenu record);

    int updateBatch(List<SysMenu> list);

    int updateBatchSelective(List<SysMenu> list);

    int batchInsert(@Param("list") List<SysMenu> list);

    List<SysMenu> getUserMenuList(Integer userId);
}