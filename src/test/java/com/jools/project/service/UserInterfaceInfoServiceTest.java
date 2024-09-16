package com.jools.project.service;


import com.jools.joolscommon.model.entity.UserInterfaceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    @Test
    public void invokeInterfaceCount() {
        boolean succeed = userInterfaceInfoService.invokeInterfaceCount(1L, 1L);
        assert succeed;
    }

    @Test
    public void testAnalysisResult() {
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoService.listInterfacesAnalysis(5);
        for (UserInterfaceInfo userInterfaceInfo : userInterfaceInfos) {
            System.out.println(userInterfaceInfo);
        }
    }
}