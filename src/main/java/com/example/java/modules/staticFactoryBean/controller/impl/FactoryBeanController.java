package com.example.java.modules.staticFactoryBean.controller.impl;

import com.example.java.modules.staticFactoryBean.utils.StaticFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
@RestController
public class FactoryBeanController {

    public BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

    @RequestMapping(value = "/testSingleInstance",method = RequestMethod.GET)
    public Map<String, Object> testSingleInstance() throws InterruptedException {
        boolean getSingleFlag = true ;
        return getInstance( getSingleFlag );
    }

    @RequestMapping(value = "/singleInstanceMultithread",method = RequestMethod.GET)
    public Map<String, Object> singleInstanceMultithread()throws InterruptedException {
        boolean getSingleFlag = true ;
        return getInstanceMultithread(getSingleFlag);
    }

    @RequestMapping(value = "/testNewInstance",method = RequestMethod.GET)
    public Map<String, Object> testNewInstance() throws InterruptedException {
        boolean getSingleFlag = false ;
        return getInstance(getSingleFlag);
    }

    @RequestMapping(value = "/newInstanceMultithread",method = RequestMethod.GET)
    public Map<String, Object> newInstanceMultithread() throws InterruptedException {
        boolean getSingleFlag = false ;
        return getInstanceMultithread(getSingleFlag);
    }

    public Map<String,Object> getInstance(boolean getSingleFlag) throws InterruptedException {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();
        //获取单个 defultKey 赋值为 singleId 的实例
        StaticFactoryBean staticFactoryBean;
        if( getSingleFlag ){
            staticFactoryBean= StaticFactoryBean.getSingleInstance("singleId");
        }else {
            staticFactoryBean= StaticFactoryBean.getNewInstance("singleId");
        }
        System.out.println("testSingleInstance  defultKey : "+ staticFactoryBean.defultKey);
        result.put("testInstance defultKey",staticFactoryBean.defultKey);
        Map<String,String> uniqueKey = new HashMap<>();
        uniqueKey.put("key1","1");
        uniqueKey.put("key2","2");
        //模拟修改变量值  给staticFactoryBean中的uniqueKey赋值,
        staticFactoryBean.uniqueKey = uniqueKey;
        result.put("testInstance uniqueKey" , uniqueKey) ;

        //模拟获取对象线程导致暂时不能打印   初始quene中没有值,模拟消费导致线程挂起
        queue.take();
        result.put("defultKey befor printStr" ,  staticFactoryBean.defultKey) ;
        String defultKey = staticFactoryBean.printStr("get singleInstance  defultKey");
        result.put("printStr " ,  "get singleInstance  defultKey") ;
        result.put("defultKey after printStr" ,  defultKey) ;
        return result;
    }

    public Map<String,Object> getInstanceMultithread(boolean getSingleFlag) throws InterruptedException {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();
        //期望获得默认的 defultKey 为 id 的实例
        StaticFactoryBean staticFactoryBeanBefor ;
        if( getSingleFlag ){
            staticFactoryBeanBefor= StaticFactoryBean.getSingleInstance(null);
        }else {
            staticFactoryBeanBefor= StaticFactoryBean.getNewInstance(null);
        }
        System.out.println("get defultKey befor Multithread: "+ staticFactoryBeanBefor.defultKey);
        result.put("get defultKey Befor Multithread",staticFactoryBeanBefor.defultKey);
        result.put("get uniqueKey Befor Multithread" , staticFactoryBeanBefor.uniqueKey) ;

        //再次强制重新获取 defultKey 为 id 的实例
        StaticFactoryBean staticFactoryBeanAgain;
        if( getSingleFlag ){
            staticFactoryBeanAgain= StaticFactoryBean.getSingleInstance("multithreadId");
        }else {
            staticFactoryBeanAgain= StaticFactoryBean.getNewInstance("multithreadId");
        }
        System.out.println("get instanceMultithread  defultKey  again: "+ staticFactoryBeanAgain.defultKey);
        result.put("get defultKey again",staticFactoryBeanAgain.defultKey);
        result.put("get duniqueKey again" , staticFactoryBeanAgain.uniqueKey) ;
        result.put("get defultKey befor printStr" ,  staticFactoryBeanAgain.defultKey) ;
        String defultKey = staticFactoryBeanAgain.printStr("get instance Multithread  defultKey  again");
        result.put("printStr " ,  "get instance Multithread  defultKey  again") ;
        result.put("get defultKey after printStr" ,  defultKey) ;
        //模拟打印完之后不释放对象   初始quene中没有值,模拟消费导致线程挂起
        queue.take();
        return result;
    }

    @RequestMapping(value = "/producer",method = RequestMethod.GET)
    public Map<String, BlockingQueue> producer() throws InterruptedException {
        Map<String,BlockingQueue> result = new HashMap<>();
        queue.put("生产者产蛋一枚!");
        result.put("queue",queue);
        return result;
    }
}
