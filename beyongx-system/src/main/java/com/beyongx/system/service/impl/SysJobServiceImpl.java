package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.BeanPropertyUtils;
import com.beyongx.system.entity.SysJob;
import com.beyongx.system.entity.meta.JobMeta;
import com.beyongx.system.mapper.SysJobMapper;
import com.beyongx.system.service.ISysJobService;
import com.beyongx.system.vo.JobVo;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    @Override
    public SysJob createJob(JobVo jobVo) {
        SysJob job = new SysJob();
        try {
            BeanUtils.copyProperties(jobVo, job);;
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        if (job.getStatus() == null) {
            job.setStatus(JobMeta.Status.ONLINE.getCode());
        }
        if (job.getSort() == null) {
            job.setSort(0);
        }

        Date currentTime = new Date();
        job.setUpdateTime(currentTime);
        job.setCreateTime(currentTime);

        baseMapper.insert(job);

        return job;
    }

    @Override
    public SysJob editJob(JobVo jobVo) {
        SysJob job = baseMapper.selectById((Integer)jobVo.getId());
        if (job == null) {
            throw new ServiceException(Result.Code.E_JOB_NOT_FOUND, Result.Msg.E_JOB_NOT_FOUND);
        }

        try {
            String[] ignoreProperties = BeanPropertyUtils.getNullPropertyNames(jobVo);
            BeanUtils.copyProperties(jobVo, job, ignoreProperties);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        Date currentTime = new Date();
        job.setUpdateTime(currentTime);

        baseMapper.updateById(job);

        return job;
    }

}
