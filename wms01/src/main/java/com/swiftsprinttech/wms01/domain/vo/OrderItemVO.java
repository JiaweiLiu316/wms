package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author jiawe
 */

@Data
public class OrderItemVO {
    private String orderItemId;

    // 订单ID
    private String orderId;

    // 商品ID
    private ProductBasicInfoVO productBasicInfoVO;

    // 商品数量
    private Integer orderItemQuantity;

    // 商品总价
    private BigDecimal orderItemTotalPrice;
}
