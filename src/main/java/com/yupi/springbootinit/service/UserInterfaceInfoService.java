package com.yupi.springbootinit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.entity.UserInterfaceInfo;
import com.yupi.springbootinit.model.vo.UserInterfaceInfoVO;

/**
* @author 10355
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-08-30 20:23:33
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {


    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, Boolean flag);

    UserInterfaceInfoVO convert2VO(UserInterfaceInfo userInterfaceInfo);
}
