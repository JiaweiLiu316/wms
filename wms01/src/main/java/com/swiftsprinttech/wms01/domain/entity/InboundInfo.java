package com.swiftsprinttech.wms01.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("inbound_info")  // 对应数据库表名
public class InboundInfo {

    @TableId
    private String id;

    // 商品ID，关联商品表
    private String productId;

    // 商品名称
    private String productName;

    // 商品种类
    private String productCategory;

    // 商品单价
    private BigDecimal unitPrice;

    // 入库数量
    private Integer quantity;

    // 入库时间
    private LocalDateTime inboundTime;
}
