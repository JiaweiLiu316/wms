package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.OrderItem;
import com.swiftsprinttech.wms01.domain.vo.OrderItemVO;
import com.swiftsprinttech.wms01.domain.vo.ProductBasicInfoVO;
import com.swiftsprinttech.wms01.mappers.OrderItemMapper;
import com.swiftsprinttech.wms01.service.IOrderItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiawe
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {
    @Override
    public List<OrderItemVO> getOrderItemsByOrderId(String orderId) {
        // 根据 orderId 查询订单项列表
        List<OrderItem> orderItems = this.list(new QueryWrapper<OrderItem>().eq("order_id", orderId));
        if (orderItems == null || orderItems.isEmpty()) {
            return new ArrayList<>();
        }

        // 将订单项实体转换为 VO
        return orderItems.stream().map(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            orderItemVO.setOrderItemId(orderItem.getId());
            orderItemVO.setOrderId(orderItem.getOrderId());
            orderItemVO.setOrderItemQuantity(orderItem.getQuantity());
            orderItemVO.setOrderItemTotalPrice(orderItem.getTotalPrice());

            // 可根据需要填充 productBasicInfoVO
            ProductBasicInfoVO productBasicInfoVO = new ProductBasicInfoVO();
            productBasicInfoVO.setProductId(orderItem.getProductId());
            // 假设还有其他信息需要填充，可从 ProductBasicInfoService 获取
            orderItemVO.setProductBasicInfoVO(productBasicInfoVO);

            return orderItemVO;
        }).collect(Collectors.toList());
    }
}
