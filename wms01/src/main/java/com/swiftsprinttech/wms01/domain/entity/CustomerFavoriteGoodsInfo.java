package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 顾客常购物品详情
 * @author jiawe
 */
@Data
@TableName("customer_favorite_goods")
public class CustomerFavoriteGoodsInfo {
    // 自增ID
    @TableId
    private Integer id;

    // 用户ID（关联到 CustomerInfo）
    private String customerId;

    // 商品ID（关联到 ProductInfo）
    private String productId;
}
