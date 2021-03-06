package com.beyongx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父节点
     */
    private Integer pid;

    /**
     * 标题
     */
    private String title;

    /**
     * 名称
     */
    private String name;

    /**
     * 组件
     */
    private String component;

    /**
     * 路径
     */
    private String path;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单类型 0.网页页面1.菜单组件2.动作组件（捆绑请求）
     */
    private Integer type;

    /**
     * 是否菜单
     */
    private Boolean isMenu;

    /**
     * 权限
     */
    private String permission;

    /**
     * 状态 -1.删除;1.激活;2.暂停;
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 归属于
     */
    private String belongsTo;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
