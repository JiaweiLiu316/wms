package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品详情
 * @author jiawe
 */
@Data
@TableName("product_info")
public class ProductInfo {
        // 商品ID
        @TableId
        private String id;

        // 商品名称
        private String name;

        // 商品分类
        private String category;

        // 商品所在地区
        private String region;

        // 商品单价
        private BigDecimal price;

        // 商品库存数量
        private Integer stock;

        // 商品描述（可选字段）
        private String description;

        // 创建时间

        private LocalDateTime createdTime;

        // 更新时间

        private LocalDateTime updatedTime;

}
