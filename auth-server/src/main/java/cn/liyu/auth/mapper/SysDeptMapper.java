package cn.liyu.auth.mapper;

import cn.liyu.auth.entity.SysDept;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysDeptMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(SysDept record);

    int insertOrUpdate(SysDept record);

    int insertOrUpdateSelective(SysDept record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysDept record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    SysDept selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysDept record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysDept record);

    int updateBatch(List<SysDept> list);

    int updateBatchSelective(List<SysDept> list);

    int batchInsert(@Param("list") List<SysDept> list);

    List<SysDept> selectChildrenList(@Param("sptId") Integer sptId);

    SysDept getSelfByUserId(@Param("userId")Integer userId);

    List<SysDept> selectByIdList(@Param("list") List<Integer> pathNode);
}