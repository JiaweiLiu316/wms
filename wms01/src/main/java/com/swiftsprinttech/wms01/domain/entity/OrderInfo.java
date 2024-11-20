package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

/**
 * 订单详情
 * @author jiawe
 */
@Data
@TableName("order_info")
public class OrderInfo {

        // 订单 UUID
        private String id;

        // 用户 ID
        private String customerId;

        // 交付 ID
        private String deliveryId;

        // 付款 ID
        private String paymentId;

        // 收货地址
        private String shippingAddress;

        // 订单状态
        private Integer status;

        // 下单时间
        private String createdTime;

        // 存储产品 ID 列表的 JSON 字符串 orderItems
        private String productIds;

        // 将 productIds 转换为 List<String>
        public List<String> getProductIdsList() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(productIds, new TypeReference<List<String>>() {});
        }

        // 将 List<String> 转换为 JSON 字符串
        public void setProductIdsList(List<String> productIdsList) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            this.productIds = objectMapper.writeValueAsString(productIdsList);
        }
    }

