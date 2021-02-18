package com.oldbai.halfmoon.mapper;

import com.oldbai.halfmoon.entity.Labels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Repository
public interface LabelsMapper extends BaseMapper<Labels> {

    @Update({"update tb_labels set count = count+1 where name = #{label}"})
    int updateCountByName(String label);

    @Update({"update tb_labels set count = count-1 where name = #{label}"})
    int deleteCountByName(String label);
}
