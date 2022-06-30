package com.itlaoqi.zookeeperlock.shangguigu;

import org.apache.zookeeper.*;

import java.io.IOException;

import org.apache.zookeeper.ZooDefs.Ids;


/**
 * 模拟将服务注册到zookeeper
 */

public class DistributeServer {
    private static String connectionString="43.138.84.4:2181,139.155.45.26:2181";
    //以集群的形式连接zookeeper，需要将超时时间设置的长一点
    private static int sessionTimeout = 100000;
    private static ZooKeeper zk = null;
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //1.连接zookeeper客户端
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.createConnection();

        //2.创建节点以完成服务注册
        distributeServer.registerServer(args[0]);

        //3.业务逻辑
        System.out.println(args[0]+"is working");
        Thread.sleep(Long.MAX_VALUE);
    }

    //服务注册
    private void registerServer(String hostName) throws KeeperException, InterruptedException {
        String node = zk.create("/servers/" +hostName, hostName.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostName+"is online"+node);
    }

    //连接zookeeper客户端
    void  createConnection() throws IOException {
            zk = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });

    }
}
