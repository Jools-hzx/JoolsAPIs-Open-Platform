package com.jools.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jools.joolscommon.model.entity.InterfacesInfo;
import com.jools.joolscommon.service.InnerInterfacesInfoService;
import com.jools.project.service.InterfacesInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/10 23:06
 * @description: 服务提供者实现 InnerInterfacesInfoService 方法
 */
@DubboService
public class InnerInterfacesInfoServiceImpl implements InnerInterfacesInfoService {

    @Resource
    private InterfacesInfoService interfacesInfoService;

    @Override
    public InterfacesInfo getInterfaceInfo(String url, String methodType) {
        QueryWrapper<InterfacesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", methodType);
        return interfacesInfoService.getOne(queryWrapper);
    }
}
