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
public class Manner implements CoffeeStore {

    public static final String NAME = "Manner";
    public static final String TYPES = "Coffee,Bread,Bake";

    @Override
    public String showBrandName() {
        return NAME;
    }

    @Override
    public List<String> getMenuItemsType() {
        String[] typesArr = TYPES.split(",");
        return new ArrayList<>(Arrays.asList(typesArr));
    }
}
