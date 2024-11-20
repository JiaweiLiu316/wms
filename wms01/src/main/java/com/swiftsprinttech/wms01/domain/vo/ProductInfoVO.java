package com.swiftsprinttech.wms01.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author jiawe
 */
@Data
public class ProductInfoVO {
    private String productId;

    // 商品名称
    private String productName;

    // 商品分类
    private String productCategory;

    // 商品所在地区
    private String productRegion;

    // 商品单价
    private BigDecimal productUnitPrice;

    // 商品库存数量
    private Integer productStock;

    // 商品描述（可选字段）
    private String description;

    // 创建时间

    private LocalDateTime createdTime;

    // 更新时间

    private LocalDateTime updatedTime;
}
