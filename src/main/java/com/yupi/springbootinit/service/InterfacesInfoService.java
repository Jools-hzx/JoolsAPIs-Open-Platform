package com.yupi.springbootinit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.entity.InterfacesInfo;
import com.yupi.springbootinit.model.vo.InterfacesInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 10355
* @description 针对表【interfaces_info(接口信息表)】的数据库操作Service
* @createDate 2024-08-07 22:46:43
*/
public interface InterfacesInfoService extends IService<InterfacesInfo> {

    /**
     * 校验
     *
     * @param interfacesInfo
     * @param add
     */
    void validInterfacesInfo(InterfacesInfo interfacesInfo, boolean add);

}
