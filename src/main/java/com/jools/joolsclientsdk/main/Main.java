package com.jools.joolsclientsdk.main;

import com.jools.joolsclientsdk.jdkspi.CoffeeStore;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/28 16:57
 * @description: TODO
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!!!");
        /*
        按照约定在 META-INF/services 目录下面，以接口的全限定为名称创建一个文件夹
        文件夹下面在放具体实现类的全限定名称，系统就可以根据这些文件，加载实现不同的类
         */
        ServiceLoader<CoffeeStore> coffeeStores = ServiceLoader.load(CoffeeStore.class);
        Iterator<CoffeeStore> iterator = coffeeStores.iterator();
        while (iterator.hasNext()) {
            CoffeeStore next = iterator.next();
            System.out.println(next.showBrandName());
            System.out.println(next.getMenuItemsType());
        }

    }
}
