package com.itlaoqi.zookeeperlock.service;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {
    public static int shoe = 10;

    public int outOfWarehouseWithLock() throws Exception {
        //设置ZK客户端重试策略, 每隔5秒重试一次，最多重试10次
        RetryPolicy policy = new ExponentialBackoffRetry(5000, 10);

        //创建ZK客户端，连接到Zookeeper服务器
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("43.138.84.4:2181").retryPolicy(policy).build();
        //创建与Zookeeper服务器的连接
        client.start();


        //声明锁对象，本质就是ZK顺序临时节点
        final InterProcessMutex mutex = new InterProcessMutex(client, "/locks/wh-shoe");
        try {
            //请求锁，创建锁
            mutex.acquire();
            //处理业务逻辑
            if (WarehouseService.shoe > 0) {
                Thread.sleep(1000);
                //扣减库存
                return --WarehouseService.shoe;
            } else {
                //库存不足，
                throw new RuntimeException("运动鞋库存不足");
            }
        } finally {
            //释放锁
            mutex.release();
        }
    }

    public int outOfWarehouse() throws Exception {
        //处理业务逻辑
        if (WarehouseService.shoe > 0) {
            Thread.sleep(1000);
            //扣减库存
            return --WarehouseService.shoe;
        } else {
            throw new RuntimeException("运动鞋库存不足");
        }
    }
}