package com.beyongx.framework.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色菜单关联表
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer menuId;


}
