package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品
 * @author jiawe
 */
@Data
@TableName("order_item")
public class OrderItem {
    // 订单项ID
    @TableId
    private String id;

    // 订单ID
    private String orderId;

    // 商品ID
    private String productId;

    // 商品数量
    private Integer quantity;

    // 商品单价
    private BigDecimal unitPrice;

    // 商品总价
    private BigDecimal totalPrice;
}
