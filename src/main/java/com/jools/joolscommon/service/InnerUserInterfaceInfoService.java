package com.jools.joolscommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jools.joolscommon.model.entity.User;
import com.jools.joolscommon.model.entity.UserInterfaceInfo;
import com.jools.joolscommon.model.vo.UserInterfaceInfoVO;

/**
 * @author 10355
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2024-08-30 20:23:33
 */
public interface InnerUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 从数据库中查找是否已经分配给用户密钥 accessKey / secretKey
     * @param accessKey
     * @param secretKey
     * @return
     */
    User getInvokeUser(String accessKey, String secretKey);

    /**
     * 从数据库中查询模拟接口是否存在
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeInterfaceCount(Long interfaceInfoId, Long userId);

    UserInterfaceInfoVO convert2VO(UserInterfaceInfo userInterfaceInfo);
}
