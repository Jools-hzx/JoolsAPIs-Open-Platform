package com.jools.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.jools.joolsclientsdk.client.JoolsHttpClient;
import com.jools.project.common.BaseResponse;
import com.jools.project.common.DeleteRequest;
import com.jools.project.common.ErrorCode;
import com.jools.project.common.ResultUtils;
import com.jools.project.model.dto.interfacesInfo.InterfacesInfoInvokeRequest;
import com.jools.project.model.dto.interfacesInfo.InterfacesInfoUpdateRequest;
import com.jools.project.model.entity.InterfacesInfo;
import com.jools.project.model.entity.User;
import com.jools.project.model.enums.InterfaceStatusEnum;
import com.jools.project.model.vo.InterfacesInfoVO;
import com.jools.project.annotation.AuthCheck;
import com.jools.project.constant.CommonConstant;
import com.jools.project.constant.UserConstant;
import com.jools.project.exception.BusinessException;
import com.jools.project.exception.ThrowUtils;
import com.jools.project.model.dto.interfacesInfo.InterfacesInfoAddRequest;
import com.jools.project.model.dto.interfacesInfo.InterfacesInfoQueryRequest;
import com.jools.project.service.InterfacesInfoService;
import com.jools.project.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/interfacesInfo")
@Slf4j
public class InterfacesController {

    @Resource
    private InterfacesInfoService interfacesInfoService;

    @Resource
    private UserService userService;

    @Resource
    private JoolsHttpClient joolsHttpClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfacesInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfacesInfo(@RequestBody InterfacesInfoAddRequest interfacesInfoAddRequest, HttpServletRequest request) {
        if (interfacesInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfacesInfo interfacesInfo = new InterfacesInfo();
        BeanUtils.copyProperties(interfacesInfoAddRequest, interfacesInfo);

        //校验
        interfacesInfoService.validInterfacesInfo(interfacesInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfacesInfo.setUserId(loginUser.getId());

        boolean result = interfacesInfoService.save(interfacesInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfacesInfoId = interfacesInfo.getId();
        return ResultUtils.success(newInterfacesInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfacesInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfacesInfo oldInterfacesInfo = interfacesInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfacesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfacesInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfacesInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfacesInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfacesInfo(@RequestBody InterfacesInfoUpdateRequest interfacesInfoUpdateRequest) {
        if (interfacesInfoUpdateRequest == null || interfacesInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfacesInfo interfacesInfo = new InterfacesInfo();
        BeanUtils.copyProperties(interfacesInfoUpdateRequest, interfacesInfo);

        // 参数校验
        interfacesInfoService.validInterfacesInfo(interfacesInfo, false);
        long id = interfacesInfoUpdateRequest.getId();
        // 判断是否存在
        InterfacesInfo oldInterfacesInfo = interfacesInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfacesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfacesInfoService.updateById(interfacesInfo);
        return ResultUtils.success(result);
    }


    /**
     * 更新上线接口（仅管理员）
     *
     * @param interfacesInfoUpdateRequest
     * @return
     */
    @PostMapping("/update/status/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  //基于 AOP 机制校验用户身份
    public BaseResponse<Boolean> onlineInterfacesInfo(@RequestBody InterfacesInfoUpdateRequest interfacesInfoUpdateRequest) {
        //请求参数校验
        if (interfacesInfoUpdateRequest == null || interfacesInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1. 校验该接口是否存在
        Long interfaceId = interfacesInfoUpdateRequest.getId();
        InterfacesInfo interfacesInfo = interfacesInfoService.getById(interfaceId);
        if (null == interfaceId) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //2. 判断该接口是否可以调用
        try {
            joolsHttpClient.testPostRequest("Jools Wakoo");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        //3. 更新接口状态，配置枚举类
        interfacesInfo.setId(interfaceId);
        interfacesInfo.setStatus(InterfaceStatusEnum.ONLINE.getValue());

        boolean result = interfacesInfoService.updateById(interfacesInfo);
        return ResultUtils.success(result);
    }

    /**
     * 更新下线接口（仅管理员）
     *
     * @param interfacesInfoUpdateRequest
     * @return
     */
    @PostMapping("/update/status/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  //基于 AOP 机制校验用户身份
    public BaseResponse<Boolean> offlineInterfacesInfo(@RequestBody InterfacesInfoUpdateRequest interfacesInfoUpdateRequest) {
        //请求参数校验
        if (interfacesInfoUpdateRequest == null || interfacesInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1. 校验该接口是否存在
        Long interfaceId = interfacesInfoUpdateRequest.getId();
        InterfacesInfo interfacesInfo = interfacesInfoService.getById(interfaceId);
        if (null == interfaceId) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //2. 更新接口状态，修改为下线
        interfacesInfo.setId(interfaceId);
        interfacesInfo.setStatus(InterfaceStatusEnum.OFFLINE.getValue());

        boolean result = interfacesInfoService.updateById(interfacesInfo);
        return ResultUtils.success(result);
    }

    /**
     * 请求调用接口
     *
     * @param interfacesInfoUpdateRequest
     * @return
     */
    @PostMapping("/update/status/invoke")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  //基于 AOP 机制校验用户身份
    public BaseResponse<String> invokeInterfaces(@RequestBody InterfacesInfoInvokeRequest interfacesInfoInvokeRequest,
                                                 HttpServletRequest request) {
        //请求参数校验
        if (interfacesInfoInvokeRequest == null || interfacesInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //解析 InterfacesInvokeRequest
        Long interfaceId = interfacesInfoInvokeRequest.getId();
        String requestParams = interfacesInfoInvokeRequest.getUserRequestParams();

        //校验用户是否登录
        User loginUser = userService.getLoginUser(request);

        //1. 校验该接口是否存在
        InterfacesInfo interfaceEntity = interfacesInfoService.getById(interfaceId);
        if (null == interfaceEntity) {
            log.error("请求接口 id:{} 不存在", interfaceId);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //2. 校验接口状态，如果已经下线则不能请求
        if (interfaceEntity.getStatus().equals(InterfaceStatusEnum.OFFLINE)) {
            log.error("请求接口 id:{} 已经下线了", interfaceId);
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        //校验 accessKey 和 secretKey
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey)) {
            log.error("用户 id:{} 非法请求接口调用", loginUser.getId());
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //TODO: 到数据库内调用校验

        //重新构建请求 client; 防止始终用管理员账号、密码来测试
        JoolsHttpClient client = new JoolsHttpClient(accessKey, secretKey);

        //校验请求参数
        //将请求参数的格式转换成为 JSON 格式
        Gson gson = new Gson();
        com.jools.joolsclientsdk.model.User jsonReq = gson.fromJson(requestParams,
                com.jools.joolsclientsdk.model.User.class);
        System.out.println("接收到 JSON 格式用户数据:" + jsonReq);

        //请求调用接口 - SDK 会携带 accessKey 和 secretKey 到接口平台校验
        String result = client.testModelPost(jsonReq);

        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfacesInfoVO> getInterfacesInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfacesInfo interfacesInfo = interfacesInfoService.getById(id);
        if (interfacesInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将 InterfacesInfo 转换为 InterfacesInfoVo
        InterfacesInfoVO vo = interfacesInfoService.convert2Vo(interfacesInfo);
        return ResultUtils.success(vo);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfacesInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfacesInfo>> listInterfacesInfoByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest) {
        long current = interfacesInfoQueryRequest.getCurrent();
        long size = interfacesInfoQueryRequest.getPageSize();

        String sortField = interfacesInfoQueryRequest.getSortField();
        String sortOrder = interfacesInfoQueryRequest.getSortOrder();

        InterfacesInfo interfacesInfoQuery = new InterfacesInfo();
        String description = interfacesInfoQuery.getDescription();

        // description 需要支持模糊搜索
        interfacesInfoQuery.setDescription(null);

        //限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfacesInfo> queryWrapper = new QueryWrapper<>(interfacesInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(
                StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size));
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size),
//                interfacesInfoService.getQueryWrapper(interfacesInfoQueryRequest));
        return ResultUtils.success(interfacesInfoPage);
    }

//    /**
//     * 分页获取列表（封装类）
//     *
//     * @param interfacesInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<InterfacesInfoVO>> listInterfacesInfoVOByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest,
//                                                                           HttpServletRequest request) {
//        long current = interfacesInfoQueryRequest.getCurrent();
//        long size = interfacesInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size),
//                interfacesInfoService.getQueryWrapper(interfacesInfoQueryRequest));
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVOPage(interfacesInfoPage, request));
//    }
//
//    /**
//     * 分页获取当前用户创建的资源列表
//     *
//     * @param interfacesInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<InterfacesInfoVO>> listMyInterfacesInfoVOByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest,
//                                                                             HttpServletRequest request) {
//        if (interfacesInfoQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        interfacesInfoQueryRequest.setUserId(loginUser.getId());
//        long current = interfacesInfoQueryRequest.getCurrent();
//        long size = interfacesInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size),
//                interfacesInfoService.getQueryWrapper(interfacesInfoQueryRequest));
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVOPage(interfacesInfoPage, request));
//    }
//
//    // endregion
//
//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param interfacesInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<InterfacesInfoVO>> searchInterfacesInfoVOByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest,
//                                                                             HttpServletRequest request) {
//        long size = interfacesInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.searchFromEs(interfacesInfoQueryRequest);
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVOPage(interfacesInfoPage, request));
//    }

//    /**
//     * 编辑（用户）
//     *
//     * @param interfacesInfoEditRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/edit")
//    public BaseResponse<Boolean> editInterfacesInfo(@RequestBody InterfacesInfoEditRequest interfacesInfoEditRequest, HttpServletRequest request) {
//        if (interfacesInfoEditRequest == null || interfacesInfoEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        InterfacesInfo interfacesInfo = new InterfacesInfo();
//        BeanUtils.copyProperties(interfacesInfoEditRequest, interfacesInfo);
//        List<String> tags = interfacesInfoEditRequest.getTags();
//        if (tags != null) {
//            interfacesInfo.setTags(JSONUtil.toJsonStr(tags));
//        }
//        // 参数校验
//        interfacesInfoService.validInterfacesInfo(interfacesInfo, false);
//        User loginUser = userService.getLoginUser(request);
//        long id = interfacesInfoEditRequest.getId();
//        // 判断是否存在
//        InterfacesInfo oldInterfacesInfo = interfacesInfoService.getById(id);
//        ThrowUtils.throwIf(oldInterfacesInfo == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldInterfacesInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = interfacesInfoService.updateById(interfacesInfo);
//        return ResultUtils.success(result);
//    }

}
