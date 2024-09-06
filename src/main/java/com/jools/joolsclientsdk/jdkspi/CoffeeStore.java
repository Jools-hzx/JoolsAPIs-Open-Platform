package com.jools.joolsclientsdk.jdkspi;

import java.util.List;

public interface CoffeeStore {

    //获取店名
    String showBrandName();

    //获取店内售卖的商品类型
    List<String> getMenuItemsType();
}
