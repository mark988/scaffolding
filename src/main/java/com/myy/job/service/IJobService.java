package com.myy.job.service;

import com.myy.common.entity.QueryRequest;
import com.myy.job.entity.Job;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author MrBird
 */
public interface IJobService extends IService<Job> {

    Job findJob(Long jobId);

    IPage<Job> findJobs(QueryRequest request, Job job);

    void createJob(Job job);

    void updateJob(Job job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

}
