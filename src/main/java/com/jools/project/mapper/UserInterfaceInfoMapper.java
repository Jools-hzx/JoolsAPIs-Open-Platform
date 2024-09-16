package com.jools.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jools.joolscommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 10355
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-08-30 20:23:33
* @Entity com.yupi.springbootinit.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    //查询前 limit 个调用次数最多的接口
    List<UserInterfaceInfo> listAllInterfaceAnalysisRecords(Integer limit);
}