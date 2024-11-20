package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.CustomerAddressInfo;

import java.util.List;

/**
 * @author jiawe
 */
public interface ICustomerAddressInfoService extends IService<CustomerAddressInfo> {

    /**
     * @param customerId
     * @return
     */
    List<String> getAddressByCustomerId(String customerId);
}
