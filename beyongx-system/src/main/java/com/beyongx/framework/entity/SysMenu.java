package com.beyongx.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private String name;

    private String title;

    private String path;

    private String icon;

    private Boolean type;

    /**
     * 0.否;1.是
     */
    private Boolean isMenu;

    private Integer sort;

    /**
     * -1.删除;1.激活;2.暂停;
     */
    private Boolean status;

    private String belongsTo;


}
