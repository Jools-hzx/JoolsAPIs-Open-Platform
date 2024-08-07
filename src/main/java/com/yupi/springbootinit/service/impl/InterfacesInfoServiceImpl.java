package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.InterfacesInfoMapper;
import com.yupi.springbootinit.model.entity.InterfacesInfo;
import com.yupi.springbootinit.service.InterfacesInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 10355
 * @description 针对表【interfaces_info(接口信息表)】的数据库操作Service实现
 * @createDate 2024-08-07 22:46:43
 */
@Service
public class InterfacesInfoServiceImpl
        extends ServiceImpl<InterfacesInfoMapper, InterfacesInfo>
        implements InterfacesInfoService {

    @Override
    public void validInterfacesInfo(InterfacesInfo interfacesInfo, boolean add) {
        if (interfacesInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //获取接口信息的对象的名称
        String name = interfacesInfo.getName();

        // 创建时，参数不能为空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        //限制接口名称长度;如果接口名称不为空且长度大于50,抛出参数错误的异常,错误信息为"名称过长"
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }
}




