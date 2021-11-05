package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户岗位关联表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 岗位id
     */
    private Integer jobId;


}
