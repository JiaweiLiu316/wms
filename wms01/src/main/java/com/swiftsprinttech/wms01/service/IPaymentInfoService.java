package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.PaymentInfo;
import com.swiftsprinttech.wms01.domain.vo.PaymentInfoVO;

public interface IPaymentInfoService extends IService<PaymentInfo> {
    PaymentInfoVO getPaymentInfoById(String paymentId);
}
