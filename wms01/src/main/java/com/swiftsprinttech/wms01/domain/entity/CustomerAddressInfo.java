package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 顾客常用地址详情
 * @author jiawe
 */
@Data
@TableName("customer_address")
public class CustomerAddressInfo {

    // 地址ID（自增）
    @TableId
    private Integer id;

    // 用户ID（关联到 CustomerInfo）
    private String customerId;

    // 地址内容
    private String address;
}
