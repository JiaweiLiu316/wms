package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.CustomerInfo;
import com.swiftsprinttech.wms01.domain.vo.CustomerInfoVO;

public interface ICustomerInfoService extends IService<CustomerInfo> {
    /**
     * @param customerId
     * @return
     */
    CustomerInfoVO getCustomerInfoById(String customerId);
}
