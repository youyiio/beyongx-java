package com.beyongx.system.vo;

import java.io.Serializable;
import java.util.Date;

import com.beyongx.common.vo.Node;

import lombok.Data;

@Data
public class MenuVo extends Node implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
     * 类型 0.页面菜单1.动作菜单
     */
    private Boolean type;

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
    private Boolean status;

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
