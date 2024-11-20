package com.swiftsprinttech.wms01.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swiftsprinttech.wms01.domain.entity.*;
import com.swiftsprinttech.wms01.domain.vo.*;
import com.swiftsprinttech.wms01.service.*;
import com.swiftsprinttech.wms01.utils.IdGenerator;
import com.swiftsprinttech.wms01.utils.Result;
import com.swiftsprinttech.wms01.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单表
 * @author jiawe
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private ICustomerInfoService customerInfoService;
    @Autowired
    private ICustomerFavoriteGoodsInfoService customerFavoriteGoodsInfoService;
    @Autowired
    private ICustomerAddressInfoService customerAddressInfoService;
    @Autowired
    private IDeliveryInfoService deliveryInfoService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IPaymentInfoService paymentInfoService;


    /**
     * @description: 订单列表
     * @param: [page, size]
     * @return: org.springframework.http.ResponseEntity<com.swiftsprinttech.wms01.utils.Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.swiftsprinttech.wms01.domain.entity.OrderInfo>>>
     * @date: 2024/11/17 下午 9:06
     */
    @GetMapping("/getAllOrder")
    public ResponseEntity<Result<Page<OrderInfoVO>>> getAllOrder(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        // 构建分页查询条件
        Page<OrderInfo> orderPage = new Page<>(page, size);
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        // 优先排序：状态为 false 的优先
        queryWrapper.orderByAsc("status")
                // 次要排序：创建时间降序
                .orderByDesc("created_time");

        // 分页查询订单实体
        Page<OrderInfo> orderInfoPage = orderInfoService.page(orderPage, queryWrapper);

        // 转换为 VO 对象
        List<OrderInfoVO> orderInfoVOList = orderInfoPage.getRecords().stream().map(orderInfo -> {
            OrderInfoVO orderInfoVO = new OrderInfoVO();
            orderInfoVO.setOrderId(orderInfo.getId());
            orderInfoVO.setOrderStatus(orderInfo.getStatus());
            orderInfoVO.setOrderCreatedTime(orderInfo.getCreatedTime());

            // 查询并设置用户信息
            CustomerInfoVO customerInfoVO = customerInfoService.getCustomerInfoById(orderInfo.getCustomerId());
            customerInfoVO.setShippingAddress(orderInfo.getShippingAddress());
            orderInfoVO.setCustomerInfoVO(customerInfoVO);

            // 查询并设置交付信息
            DeliveryInfoVO deliveryInfoVO = deliveryInfoService.getDeliveryInfoById(orderInfo.getDeliveryId());
            orderInfoVO.setDeliveryInfoVO(deliveryInfoVO);

            // 查询并设置付款信息
            PaymentInfoVO paymentInfoVO = paymentInfoService.getPaymentInfoById(orderInfo.getPaymentId());
            orderInfoVO.setPaymentInfoVO(paymentInfoVO);

            // 查询并设置订单项
            List<OrderItemVO> orderItemVOList = orderItemService.getOrderItemsByOrderId(orderInfo.getId()).stream().map(orderItem -> {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setOrderItemId(orderItem.getOrderItemId());
                orderItemVO.setOrderId(orderItem.getOrderId());
                orderItemVO.setOrderItemQuantity(orderItem.getOrderItemQuantity());
                orderItemVO.setOrderItemTotalPrice(orderItem.getOrderItemTotalPrice());

                // 查询并设置商品基本信息
                ProductBasicInfoVO productBasicInfoVO = productInfoService.getProductBasicInfoById(orderItem.getProductBasicInfoVO().getProductId());
                orderItemVO.setProductBasicInfoVO(productBasicInfoVO);

                return orderItemVO;
            }).collect(Collectors.toList());
            orderInfoVO.setOrderItemVOList(orderItemVOList);

            return orderInfoVO;
        }).collect(Collectors.toList());

        // 构建分页 VO
        Page<OrderInfoVO> orderInfoVOPage = new Page<>();
        orderInfoVOPage.setRecords(orderInfoVOList);
        orderInfoVOPage.setCurrent(orderInfoPage.getCurrent());
        orderInfoVOPage.setSize(orderInfoPage.getSize());
        orderInfoVOPage.setTotal(orderInfoPage.getTotal());

        // 返回分页结果
        return ResultUtil.success("查询成功", orderInfoVOPage);
    }


    @PostMapping("/add")
    @Transactional
    public ResponseEntity<Result<Object>> addOrderInfo(@RequestBody OrderInfoVO orderInfoVO){
        if (orderInfoVO == null ){
            return ResultUtil.error("入库失败！");
        }
        // 生成唯一的订单 ID
        String orderId = IdGenerator.generateId();;
        String customerId;
        String deliverId = IdGenerator.generateId();;
        String paymentId = IdGenerator.generateId();;
        String orderItemId = IdGenerator.generateId();;
        List<String> productIdList = new ArrayList<>();
        LocalDateTime orderCreatedTime = LocalDateTime.now();

        //        用户信息入库
        CustomerInfo customerInfo = new CustomerInfo();
        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",orderInfoVO.getCustomerInfoVO().getPhoneNumber());
        // 检查是否存在相同手机号的用户
        CustomerInfo existingCustomer = customerInfoService.getOne(queryWrapper);
        if (existingCustomer != null) {
            // 如果用户已存在，则使用现有用户的 ID
            customerId = existingCustomer.getId();
            BigDecimal totalAmount = existingCustomer.getRepurchaseAmount().add(orderInfoVO.getPaymentInfoVO().getAmountDue());
            existingCustomer.setRepurchaseAmount(totalAmount);
//            存在则更新复购金额
            customerInfoService.updateById(existingCustomer);
        } else {
            customerId = UUID.randomUUID().toString();
            // 如果用户不存在，则新建用户信息
            customerInfo.setName(orderInfoVO.getCustomerInfoVO().getCustomerName());
            customerInfo.setPhoneNumber(orderInfoVO.getCustomerInfoVO().getPhoneNumber());
            customerInfo.setRepurchaseAmount(orderInfoVO.getPaymentInfoVO().getAmountDue());
            customerInfoService.save(customerInfo);
        }

        // 检查并保存地址信息
        CustomerAddressInfo customerAddressInfo = new CustomerAddressInfo();
        customerAddressInfo.setCustomerId(customerId);
        customerAddressInfo.setAddress(orderInfoVO.getCustomerInfoVO().getShippingAddress());

        QueryWrapper<CustomerAddressInfo> addressWrapper = new QueryWrapper<>();
        addressWrapper.eq("customer_id", customerId)
                .eq("address", orderInfoVO.getCustomerInfoVO().getShippingAddress());

        CustomerAddressInfo existingAddress = customerAddressInfoService.getOne(addressWrapper);
        if (existingAddress == null) {
            // 地址不存在，插入新地址
            customerAddressInfoService.save(customerAddressInfo);
        }

        // 检查并保存喜欢的商品信息
        for (OrderItemVO orderItemVO : orderInfoVO.getOrderItemVOList()) {
            productIdList.add(orderItemVO.getProductBasicInfoVO().getProductId());
            CustomerFavoriteGoodsInfo favoriteGoodsInfo = new CustomerFavoriteGoodsInfo();
            favoriteGoodsInfo.setCustomerId(customerId);
            favoriteGoodsInfo.setProductId(orderItemVO.getProductBasicInfoVO().getProductId());

            QueryWrapper<CustomerFavoriteGoodsInfo> favoriteWrapper = new QueryWrapper<>();
            favoriteWrapper.eq("customer_id", customerId)
                    .eq("product_id", orderItemVO.getProductBasicInfoVO().getProductId());

            CustomerFavoriteGoodsInfo existingFavoriteGoods = customerFavoriteGoodsInfoService.getOne(favoriteWrapper);
            if (existingFavoriteGoods == null) {
                // 如果商品不在收藏中，插入新记录
                favoriteGoodsInfo.setProductId(orderItemVO.getProductBasicInfoVO().getProductId());
                customerFavoriteGoodsInfoService.save(favoriteGoodsInfo);
            }

            // 保存商品项 (OrderItem)
            OrderItem orderItem = new OrderItem();
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderId);
            orderItem.setProductId(orderItemVO.getProductBasicInfoVO().getProductId());
            orderItem.setQuantity(orderItemVO.getOrderItemQuantity());
            orderItem.setUnitPrice(orderItemVO.getProductBasicInfoVO().getProductUnitPrice());
            orderItem.setTotalPrice(orderItemVO.getOrderItemTotalPrice());
            orderItemService.save(orderItem);
        }

        // 保存支付信息
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setId(paymentId);
        paymentInfo.setOrderId(orderId);
        paymentInfo.setPaymentMethod(orderInfoVO.getPaymentInfoVO().getPaymentMethod());
        paymentInfo.setPaymentDate(orderInfoVO.getPaymentInfoVO().getPaymentDate());
        paymentInfo.setAmountDue(orderInfoVO.getPaymentInfoVO().getAmountDue());
        paymentInfo.setAmountPaid(orderInfoVO.getPaymentInfoVO().getAmountPaid());
        paymentInfo.setIsCompleted(orderInfoVO.getPaymentInfoVO().getIsCompleted());
        paymentInfoService.save(paymentInfo);

        //        交付信息入库
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setOrderId(orderId);
        deliveryInfo.setId(deliverId);
        deliveryInfo.setDeliveryMethod(orderInfoVO.getDeliveryInfoVO().getDeliveryMethod());
        deliveryInfo.setDeliveryAddress(orderInfoVO.getDeliveryInfoVO().getDeliveryAddress());
        deliveryInfo.setDeliveryDate(orderInfoVO.getDeliveryInfoVO().getDeliveryDate());
        deliveryInfo.setIsCompleted(orderInfoVO.getDeliveryInfoVO().getIsCompleted());
        deliveryInfoService.save(deliveryInfo);

        // 保存订单信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setCustomerId(customerId);
        orderInfo.setDeliveryId(deliverId);
        orderInfo.setPaymentId(paymentId);
        orderInfo.setShippingAddress(orderInfoVO.getDeliveryInfoVO().getDeliveryAddress());
        orderInfo.setProductIds(String.valueOf(productIdList));
        orderInfo.setStatus(orderInfoVO.getOrderStatus());
        orderInfo.setCreatedTime(String.valueOf(orderCreatedTime));
        orderInfoService.save(orderInfo);

        return ResultUtil.success("入库成功！");
    }

    /**
     * @description: 根据订单ID查询订单详情
     * @param: [orderId]
     * @return: org.springframework.http.ResponseEntity<com.swiftsprinttech.wms01.utils.Result<com.swiftsprinttech.wms01.domain.entity.OrderInfoVO>>
     * @date: 2024/11/18 下午 4:15
     */
    @GetMapping("/getOrderById")
    public ResponseEntity<Result<OrderInfoVO>> getOrderById(@RequestParam("orderId") String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            return ResultUtil.error("订单ID不能为空！");
        }

        // 查询订单实体
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if (orderInfo == null) {
            return ResultUtil.error("订单不存在！");
        }

        // 构建订单 VO
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        orderInfoVO.setOrderId(orderInfo.getId());
        orderInfoVO.setOrderStatus(orderInfo.getStatus());
        orderInfoVO.setOrderCreatedTime(orderInfo.getCreatedTime());

        // 查询并设置用户信息
        CustomerInfoVO customerInfoVO = customerInfoService.getCustomerInfoById(orderInfo.getCustomerId());
        customerInfoVO.setShippingAddress(orderInfo.getShippingAddress());
        orderInfoVO.setCustomerInfoVO(customerInfoVO);

        // 查询并设置交付信息
        DeliveryInfoVO deliveryInfoVO = deliveryInfoService.getDeliveryInfoById(orderInfo.getDeliveryId());
        orderInfoVO.setDeliveryInfoVO(deliveryInfoVO);

        // 查询并设置付款信息
        PaymentInfoVO paymentInfoVO = paymentInfoService.getPaymentInfoById(orderInfo.getPaymentId());
        orderInfoVO.setPaymentInfoVO(paymentInfoVO);

        // 查询并设置订单项
        List<OrderItemVO> orderItemVOList = orderItemService.getOrderItemsByOrderId(orderInfo.getId()).stream().map(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            orderItemVO.setOrderItemId(orderItem.getOrderItemId());
            orderItemVO.setOrderId(orderItem.getOrderId());
            orderItemVO.setOrderItemQuantity(orderItem.getOrderItemQuantity());
            orderItemVO.setOrderItemTotalPrice(orderItem.getOrderItemTotalPrice());

            // 查询并设置商品基本信息
            ProductBasicInfoVO productBasicInfoVO = productInfoService.getProductBasicInfoById(orderItem.getProductBasicInfoVO().getProductId());
            orderItemVO.setProductBasicInfoVO(productBasicInfoVO);

            return orderItemVO;
        }).collect(Collectors.toList());
        orderInfoVO.setOrderItemVOList(orderItemVOList);

        // 返回查询结果
        return ResultUtil.success("查询成功", orderInfoVO);
    }


    /**
     * @description: 更新订单信息
     * @param: orderInfoVO 包含更新内容的订单信息
     * @return: ResponseEntity<Result<?>>
     */
    @PutMapping("/updateOrder")
    @Transactional
    public ResponseEntity<Result<Object>> updateOrder(@RequestBody OrderInfoVO orderInfoVO) {
        // 校验输入
        if (orderInfoVO == null || orderInfoVO.getOrderId() == null) {
            return ResultUtil.error("订单ID不能为空！");
        }

        // 查询订单是否存在
        OrderInfo existingOrder = orderInfoService.getById(orderInfoVO.getOrderId());
        if (existingOrder == null) {
            return ResultUtil.error("订单不存在！");
        }
        existingOrder.setShippingAddress(orderInfoVO.getDeliveryInfoVO().getDeliveryAddress());
        existingOrder.setStatus(orderInfoVO.getOrderStatus());
        // 获取用户ID
        String customerId = orderInfoVO.getCustomerInfoVO().getCustomerId();
        // 更新用户信息
        if (orderInfoVO.getCustomerInfoVO() != null) {
            // 检查用户是否存在
            CustomerInfo existingCustomer = customerInfoService.getById(customerId);
            if (existingCustomer == null) {
                return ResultUtil.error("用户不存在！");
            }
            existingCustomer.setName(orderInfoVO.getCustomerInfoVO().getCustomerName());
            existingCustomer.setPhoneNumber(orderInfoVO.getCustomerInfoVO().getPhoneNumber());
            customerInfoService.updateById(existingCustomer);
        }

        // 更新支付信息
        if (orderInfoVO.getPaymentInfoVO() != null) {
            String paymentId = orderInfoVO.getPaymentInfoVO().getPaymentId();
            // 检查支付信息是否存在
            PaymentInfo existingPayment = paymentInfoService.getById(paymentId);
            if (existingPayment == null) {
                return ResultUtil.error("支付信息不存在！");
            }
            // 更新支付信息
            existingPayment.setAmountPaid(orderInfoVO.getPaymentInfoVO().getAmountPaid());
            existingPayment.setAmountDue(orderInfoVO.getPaymentInfoVO().getAmountDue());
            existingPayment.setPaymentDate(orderInfoVO.getPaymentInfoVO().getPaymentDate());
            existingPayment.setPaymentMethod(orderInfoVO.getPaymentInfoVO().getPaymentMethod());
            existingPayment.setIsCompleted(orderInfoVO.getPaymentInfoVO().getIsCompleted());
            paymentInfoService.updateById(existingPayment);

        }

        // 更新配送信息
        if (orderInfoVO.getDeliveryInfoVO() != null) {
            String deliveryId = orderInfoVO.getDeliveryInfoVO().getDeliveryId();
            // 检查配送信息是否存在
            DeliveryInfo existingDelivery = deliveryInfoService.getById(deliveryId);
            if (existingDelivery == null) {
                return ResultUtil.error("配送信息不存在！");
            }

            // 获取配送地址
            String deliveryAddress = orderInfoVO.getDeliveryInfoVO().getDeliveryAddress();


            // 查找该用户是否已有该配送地址
            QueryWrapper<CustomerAddressInfo> addressQuery = new QueryWrapper<>();
            addressQuery.eq("customer_id", customerId).eq("address", deliveryAddress);
            CustomerAddressInfo existingAddress = customerAddressInfoService.getOne(addressQuery);

            // 如果用户地址表中没有该地址，插入该地址
            if (existingAddress == null) {
                CustomerAddressInfo newAddress = new CustomerAddressInfo();
                newAddress.setCustomerId(customerId);
                newAddress.setAddress(deliveryAddress);
                // 保存配送地址到用户地址表
                customerAddressInfoService.save(newAddress);
            }

            // 更新配送信息
            existingDelivery.setDeliveryAddress(deliveryAddress);
            existingDelivery.setDeliveryDate(orderInfoVO.getDeliveryInfoVO().getDeliveryDate());
            existingDelivery.setDeliveryMethod(orderInfoVO.getDeliveryInfoVO().getDeliveryMethod());
            existingDelivery.setIsCompleted(orderInfoVO.getDeliveryInfoVO().getIsCompleted());
            // 保存更新的配送信息
            deliveryInfoService.updateById(existingDelivery);
        }
        List<String> productIdList = new ArrayList<>();
        // 更新订单项（如果有修改）
        if (orderInfoVO.getOrderItemVOList() != null && !orderInfoVO.getOrderItemVOList().isEmpty()) {
            // 删除现有的订单项
            orderItemService.remove(new QueryWrapper<OrderItem>().eq("order_id", orderInfoVO.getOrderId()));
            // 插入新的订单项

            for (OrderItemVO orderItemVO : orderInfoVO.getOrderItemVOList()) {
                productIdList.add(orderItemVO.getProductBasicInfoVO().getProductId());
                OrderItem orderItem = new OrderItem();
                orderItem.setId(orderItemVO.getOrderItemId());
                orderItem.setOrderId(orderInfoVO.getOrderId());
                orderItem.setProductId(orderItemVO.getProductBasicInfoVO().getProductId());
                orderItem.setQuantity(orderItemVO.getOrderItemQuantity());
                orderItem.setUnitPrice(orderItemVO.getProductBasicInfoVO().getProductUnitPrice());
                orderItem.setTotalPrice(orderItemVO.getOrderItemTotalPrice());
                // 查找该用户是否已有该配送地址
                QueryWrapper<CustomerFavoriteGoodsInfo> goodsQuery = new QueryWrapper<>();
                goodsQuery.eq("customer_id", customerId).eq("product_id", orderItemVO.getProductBasicInfoVO().getProductId());
                CustomerFavoriteGoodsInfo goodsOne = customerFavoriteGoodsInfoService.getOne(goodsQuery);

                // 如果用户地址表中没有该地址，插入该地址
                if (goodsOne == null) {
                    CustomerFavoriteGoodsInfo newGoods = new CustomerFavoriteGoodsInfo();
                    newGoods.setCustomerId(customerId);
                    newGoods.setProductId(orderItemVO.getProductBasicInfoVO().getProductId());
                    // 保存配送地址到用户地址表
                    customerFavoriteGoodsInfoService.save(newGoods);
                }
                // 保存订单项
                orderItemService.save(orderItem);
            }
        }
        existingOrder.setProductIds(String.valueOf(productIdList));
        // 执行更新操作
        // 更新订单信息
        boolean success = orderInfoService.updateById(existingOrder);
        if (!success) {
            return ResultUtil.error("订单更新失败，请重试！");
        }

        return ResultUtil.success("订单更新成功！");
    }

}
