package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.OrderInfo;
import com.swiftsprinttech.wms01.mappers.OrderInfoMapper;
import com.swiftsprinttech.wms01.service.IOrderInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jiawe
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper,OrderInfo> implements IOrderInfoService {
}
