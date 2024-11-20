package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiawe
 */
@Data
public class OrderInfoVO {
    // 订单 UUID
    private String OrderId;

    // 用户 ID
    private CustomerInfoVO customerInfoVO;

    // 交付 ID
    private DeliveryInfoVO deliveryInfoVO;

    // 付款 ID
    private PaymentInfoVO paymentInfoVO;

    // 存储产品 ID 列表的 JSON 字符串 orderItems
    private List<OrderItemVO> orderItemVOList;

    // 订单状态
    private Integer orderStatus;

    // 下单时间
    private String orderCreatedTime;
}
