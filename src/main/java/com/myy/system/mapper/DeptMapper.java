package com.myy.system.mapper;

import com.myy.system.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author mark
 */
public interface DeptMapper extends BaseMapper<Dept> {
    /**
     * 递归删除部门
     *
     * @param deptId deptId
     */
    void deleteDepts(String deptId);
}
