package com.beyongx.system.service;

import com.beyongx.system.entity.SysJob;
import com.beyongx.system.vo.JobVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 岗位表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
public interface ISysJobService extends IService<SysJob> {

    SysJob createJob(JobVo jobVo);

    SysJob editJob(JobVo jobVo);
}
