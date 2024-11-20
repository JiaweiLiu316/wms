package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jiawe
 */
@Data
public class ProductIdAndNameVO {
    private String productId;

    private String ProductName;

    private String productCategory;

    private BigDecimal unitPrice;
}
