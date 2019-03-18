/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: SpecificationService
 * Author:   gyr
 * Date:     2019/3/6 20:31
 * Description:
 */
package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        // 查询条件
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        // 查询
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(list)){  //判断list是否为空
            throw new LyException(ExceptionEnums.SPEC_NOT_FOND);
        }
        // 返回结果
        return list;
    }

    public List<SpecParam> querySpecParamList(Long gid,Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)){  //判断list是否为空
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOND);
        }
        // 返回结果
        return list;
    }

}