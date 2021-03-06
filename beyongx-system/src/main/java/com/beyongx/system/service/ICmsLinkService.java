package com.beyongx.system.service;

import com.beyongx.system.entity.CmsLink;
import com.beyongx.system.vo.LinkVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 链接 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ICmsLinkService extends IService<CmsLink> {

    CmsLink createLink(LinkVo linkVo);

    CmsLink editLink(LinkVo linkVo);
}
