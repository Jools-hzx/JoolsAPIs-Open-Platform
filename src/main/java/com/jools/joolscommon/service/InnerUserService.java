package com.jools.joolscommon.service;


import com.jools.joolscommon.model.entity.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface InnerUserService {

    /**
     * 数据库中查询是否已经给用户分配密钥 accessKey
     *
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
