package com.jools.joolsclientsdk.jdkspi.impl;

import com.jools.joolsclientsdk.jdkspi.CoffeeStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/28 16:46
 * @description: TODO
 */
public class StarBuck implements CoffeeStore {

    public static final String NAME = "StarBuck";
    public static final String TYPES = "Coffee,Ice-Cream";

    @Override
    public String showBrandName() {
        return NAME;
    }

    @Override
    public List<String> getMenuItemsType() {
        return new ArrayList<>(Arrays.asList(TYPES.split(",")));
    }
}
