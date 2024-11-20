package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jiawe
 */
@Data
public class PaymentInfoVO {

    private String PaymentId;

    // 订单ID
    private String orderId;

    /* 付款方式 */
    private String paymentMethod;

    // 付款日期
    private LocalDateTime paymentDate;

    // 应付款额
    private BigDecimal amountDue;

    // 已付款额
    private BigDecimal amountPaid;

    // 是否完成付款
    private Boolean isCompleted;
}
