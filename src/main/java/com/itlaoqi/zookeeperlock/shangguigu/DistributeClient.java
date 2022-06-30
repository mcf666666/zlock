package com.itlaoqi.zookeeperlock.shangguigu;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟客户端监听服务端
 */

public class DistributeClient {
    private static String connectionString="43.138.84.4:2181,139.155.45.26:2181";
    //以集群的形式连接zookeeper，需要将超时时间设置的长一点
    private static int sessionTimeout = 100000;
    private static ZooKeeper zk = null;


    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        //1.连接zookeeper
        DistributeClient client = new DistributeClient();
        client.createConnection();

        //2.监听服务器
        client.getServerList();



        //3.业务代码
        System.out.println("client is working");
        Thread.sleep(Long.MAX_VALUE);
    }

    //获取服务器列表信息，
    private void getServerList() throws KeeperException, InterruptedException {
        //1.获取服务器子节点信息，并且对父节点进行监听
        List<String> children = zk.getChildren("/servers", true);

        //2.获取节点里存储的服务器信息
        ArrayList<String> server = new ArrayList<>();
        for (String child:children){
            byte[] data = zk.getData("/servers/" + child, false, null);
            server.add(new String(data));
        }

        System.out.println(server);
    }


    //连接zookeeper客户端
    private void  createConnection() throws IOException {
        zk = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //再次启动监听
                try {
                    getServerList();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }


}
