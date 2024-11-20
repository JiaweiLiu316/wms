package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交付详情
 * @author jiawe
 */
@Data
@TableName("delivery_info")
public class DeliveryInfo {
    // 交收记录ID
    @TableId
    private String id;

    // 订单ID
    private String orderId;

    // 交收方式
    private String deliveryMethod;

    // 交收日期
    private LocalDateTime deliveryDate;

    // 交收地址
    private String deliveryAddress;

    // 是否完成交收
    private Boolean isCompleted;
}
