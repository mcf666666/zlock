package com.itlaoqi.zookeeperlock.controller;

import com.itlaoqi.zookeeperlock.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class OrderController {
    @Resource
    private WarehouseService warehouseService;
    @GetMapping("/create_order")
    public String createOrder(String name){
        try {
            //创建订单
            int i = warehouseService.outOfWarehouseWithLock();
            System.out.println("[" + Thread.currentThread().getName() + "]商品出库成功，剩余库存：" + i);
            return "{\"code\":\"0\"}";
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("[" + Thread.currentThread().getName() + "]商品出库失败，异常信息：" + e.getMessage());
            return "{\"code\":\"500\"}";
        }
    }

    @GetMapping("/reset")
    public String reset(){
        WarehouseService.shoe = 10;
        return "success";
    }
}
