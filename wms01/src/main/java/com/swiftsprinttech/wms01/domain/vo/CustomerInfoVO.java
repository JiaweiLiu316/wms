package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

/**
 * @author jiawe
 */
@Data
public class CustomerInfoVO {
    private String customerId;

    // 姓名
    private String customerName;

    // 联系方式
    private String phoneNumber;

    // 收货地址
    private String shippingAddress;
}
