package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 顾客详情
 * @author jiawe
 */
@Data
@TableName("customer_info")
public class CustomerInfo {

    // 用户ID（UUID）
    @TableId
    private String id;

    // 姓名
    private String name;

    // 联系方式
    private String phoneNumber;

    private String shippingAddress;

    // 复购金额
    private BigDecimal repurchaseAmount;

}
