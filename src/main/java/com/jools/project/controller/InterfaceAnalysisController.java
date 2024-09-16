package com.jools.project.controller;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jools.joolscommon.model.entity.InterfacesInfo;
import com.jools.joolscommon.model.entity.UserInterfaceInfo;
import com.jools.project.annotation.AuthCheck;
import com.jools.project.common.BaseResponse;
import com.jools.project.common.ErrorCode;
import com.jools.project.common.ResultUtils;
import com.jools.project.constant.UserConstant;
import com.jools.project.exception.BusinessException;
import com.jools.project.model.dto.userInterfaceAnalysisVo.InterfaceAnalysisQueryRequest;
import com.jools.project.model.vo.InterfaceAnalysisVO;
import com.jools.project.service.InterfacesInfoService;
import com.jools.project.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/16 15:42
 * @description: TODO
 */
@RestController
@RequestMapping("/interfacesInfo/analysis")
@Slf4j
public class InterfaceAnalysisController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfacesInfoService interfacesInfoService;


    /**
     * 获取接口调用分析结果列表（仅管理员）
     *
     * @param
     * @return
     */
    @PostMapping("/invoke/total/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceAnalysisVO>> listInvokeAnalysis(@RequestBody InterfaceAnalysisQueryRequest request) {

        Integer limit = request.getLimit();
        if (null == limit || 0 >= limit) throw new BusinessException(ErrorCode.PARAMS_ERROR);

        //1. 查询前 limit名次的接口调用次数的 interfaceId 和 userId
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoService.listInterfacesAnalysis(limit);

        //2. 基于接口 id 查询得到的接口的详细信息
        // k -> 接口 id
        // v -> 所有 id 为 k 的 UserInterfaceInfo 信息
        Map<Long, List<UserInterfaceInfo>> userInterfacesInfoMap =
                userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        //3. 基于接口id查询其详细信息
        QueryWrapper<InterfacesInfo> queryWrapper = new QueryWrapper<>();
        //查询所有接口id在 keySet 范围内的接口详细信息
        queryWrapper.in("id", userInterfacesInfoMap.keySet());

        //4. 基于所有获取到的 UserInterfacesInfo 内的 interfaceId 查询获取到接口的详细信息
        List<InterfacesInfo> interfacesInfoList = interfacesInfoService.list(queryWrapper);

        //5. 转化成 UserInterfaceAnalysisVO 类型集合，填充 total 字段的值
        if (interfacesInfoList.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        List<InterfaceAnalysisVO> analysisVOList = interfacesInfoList.stream().map(
                interfacesInfo -> {
                    long id = interfacesInfo.getId();   //接口id
                    InterfaceAnalysisVO analysisVO = new InterfaceAnalysisVO();
                    BeanUtils.copyProperties(interfacesInfo, analysisVO);

                    //填充统计到的数值 -> 查询统计接口，基于接口id查询
                    analysisVO.setTotalNum(userInterfacesInfoMap.get(id).get(0).getTotalNum());
                    return analysisVO;
                }
        ).collect(Collectors.toList());

        return ResultUtils.success(analysisVOList);
    }
}
