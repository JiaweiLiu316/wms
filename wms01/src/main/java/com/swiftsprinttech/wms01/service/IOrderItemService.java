package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.OrderItem;
import com.swiftsprinttech.wms01.domain.vo.OrderItemVO;

import java.util.Arrays;
import java.util.List;

public interface IOrderItemService extends IService<OrderItem> {
    List<OrderItemVO> getOrderItemsByOrderId(String id);
}
