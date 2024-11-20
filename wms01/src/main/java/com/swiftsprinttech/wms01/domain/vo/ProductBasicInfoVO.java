package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author jiawe
 */
@Data
public class ProductBasicInfoVO {
    private String productId;

    // 商品名称
    private String productName;

    // 商品分类
    private String productCategory;

    // 商品所在地区
    private String region;

    // 商品单价
    private BigDecimal productUnitPrice;

    // 商品库存数量
    private Integer productStock;

    // 商品描述（可选字段）
    private String description;
}
