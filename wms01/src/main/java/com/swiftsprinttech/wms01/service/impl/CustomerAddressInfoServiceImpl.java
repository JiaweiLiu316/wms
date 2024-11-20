package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.CustomerAddressInfo;
import com.swiftsprinttech.wms01.domain.entity.OrderItem;
import com.swiftsprinttech.wms01.mappers.CustomerAddressInfoMapper;
import com.swiftsprinttech.wms01.service.ICustomerAddressInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiawe
 */
@Service
public class CustomerAddressInfoServiceImpl extends ServiceImpl<CustomerAddressInfoMapper, CustomerAddressInfo> implements ICustomerAddressInfoService {
    @Override
    public List<String> getAddressByCustomerId(String customerId) {
        return this.list(new QueryWrapper<CustomerAddressInfo>()
                        .eq("customer_id", customerId))
                .stream()
                .map(CustomerAddressInfo::getAddress)
                .collect(Collectors.toList());
    }
}
