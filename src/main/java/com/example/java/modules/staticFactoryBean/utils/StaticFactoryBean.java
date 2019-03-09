package com.example.java.modules.staticFactoryBean.utils;

import java.util.HashMap;
import java.util.Map;

public class StaticFactoryBean {
    private static final StaticFactoryBean singleinstance = new StaticFactoryBean();
    private static StaticFactoryBean newinstance ;

    //模拟修改单例对象唯一值
    public String defultKey = "id";
    private String copyDfultKey = "id";

    //模拟放置单例对象公共返回参数
    public Map<String,String> uniqueKey = new HashMap<>();

    //获取单例对象方法
    public static StaticFactoryBean getSingleInstance( String defultKey ) {
        //defultKey为null 取singleinstance得defultKey
        singleinstance.defultKey = null != defultKey ? defultKey : singleinstance.defultKey;
        singleinstance.copyDfultKey = singleinstance.defultKey;
        return singleinstance;
    }
    //获取多例对象方法
    public static StaticFactoryBean getNewInstance( String defultKey ) {
        newinstance = new StaticFactoryBean();
        newinstance.defultKey = null != defultKey ? defultKey : newinstance.defultKey;
        newinstance.copyDfultKey = newinstance.defultKey;
        return newinstance;
    }

    private StaticFactoryBean(){}

    public String printStr( String str ){
        System.out.println("defultKey : "+defultKey+" printStr : "+ str);
        return defultKey;
    }
}
