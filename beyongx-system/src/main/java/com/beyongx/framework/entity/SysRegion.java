package com.beyongx.framework.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 地区表,
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer pid;

    private String shortname;

    private String name;

    private String mergerName;

    private Integer level;

    private String pinyin;

    private String code;

    private String zipCode;

    private String first;

    private String lng;

    private String lat;


}
