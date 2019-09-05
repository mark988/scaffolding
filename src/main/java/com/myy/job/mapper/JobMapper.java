package com.myy.job.mapper;


import com.myy.job.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();
}