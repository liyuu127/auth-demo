package cn.liyu.log.mapper;

import cn.liyu.log.entity.SysLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysLogMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(SysLog record);

    int insertOrUpdate(SysLog record);

    int insertOrUpdateSelective(SysLog record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysLog record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    SysLog selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysLog record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysLog record);

    int updateBatch(List<SysLog> list);

    int updateBatchSelective(List<SysLog> list);

    int batchInsert(@Param("list") List<SysLog> list);
}