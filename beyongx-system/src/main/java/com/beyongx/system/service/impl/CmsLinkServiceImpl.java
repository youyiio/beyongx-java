package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.BeanPropertyUtils;
import com.beyongx.system.entity.CmsLink;
import com.beyongx.system.entity.meta.LinkMeta;
import com.beyongx.system.mapper.CmsLinkMapper;
import com.beyongx.system.service.ICmsLinkService;
import com.beyongx.system.vo.LinkVo;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 链接 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class CmsLinkServiceImpl extends ServiceImpl<CmsLinkMapper, CmsLink> implements ICmsLinkService {

    @Override
    public CmsLink createLink(LinkVo linkVo) {
        CmsLink link = new CmsLink();
        try {
            BeanUtils.copyProperties(linkVo, link);;
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        if (link.getStatus() == null) {
            link.setStatus(LinkMeta.Status.ONLINE.getCode());
        }
        if (link.getSort() == null) {
            link.setSort(1);
        }

        Date currentTime = new Date();
        link.setCreateTime(currentTime);

        baseMapper.insert(link);

        return link;
    }

    @Override
    public CmsLink editLink(LinkVo linkVo) {
        CmsLink link = baseMapper.selectById((Integer)linkVo.getId());
        if (link == null) {
            throw new ServiceException(Result.Code.E_CMS_LINK_NOT_FOUND, Result.Msg.E_CMS_LINK_NOT_FOUND);
        }

        try {
            String[] ignoreProperties = BeanPropertyUtils.getNullPropertyNames(linkVo);
            BeanUtils.copyProperties(linkVo, link, ignoreProperties);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        baseMapper.updateById(link);

        return link;
    }

}
