package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiawe
 */
@Data
public class CustomerAllInfoVO {
    private String customerId;

    // 姓名
    private String customerName;

    // 联系方式
    private String phoneNumber;

    // 复购金额
    private BigDecimal repurchaseAmount;

    List<String> address;

    List<ProductIdAndNameVO> productIdAndNameVOS;
}
