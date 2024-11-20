package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款详情
 * @author jiawe
 */

@Data
@TableName("payment_info")
public class PaymentInfo {
    // 付款记录ID
    @TableId
    private String id;

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
