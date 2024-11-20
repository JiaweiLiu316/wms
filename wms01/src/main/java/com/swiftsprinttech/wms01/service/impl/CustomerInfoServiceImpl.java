package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.CustomerInfo;
import com.swiftsprinttech.wms01.domain.vo.CustomerInfoVO;
import com.swiftsprinttech.wms01.mappers.CustomerInfoMapper;
import com.swiftsprinttech.wms01.service.ICustomerInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jiawe
 */
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Override
    public CustomerInfoVO getCustomerInfoById(String customerId) {
        // MyBatis-Plus 的方法
        CustomerInfo customerInfo = this.getById(customerId);
        if (customerInfo == null) {
            // 或者抛出自定义异常
            return null;
        }

        // 将 CustomerInfo 转换为 CustomerInfoVO
        CustomerInfoVO customerInfoVO = new CustomerInfoVO();
        customerInfoVO.setCustomerId(customerInfo.getId());
        customerInfoVO.setCustomerName(customerInfo.getName());
        customerInfoVO.setPhoneNumber(customerInfo.getPhoneNumber());
        return customerInfoVO;
    }
}
