package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.PaymentInfo;
import com.swiftsprinttech.wms01.domain.vo.PaymentInfoVO;
import com.swiftsprinttech.wms01.mappers.PayMentInfoMapper;
import com.swiftsprinttech.wms01.service.IPaymentInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jiawe
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PayMentInfoMapper, PaymentInfo> implements IPaymentInfoService {
    @Override
    public PaymentInfoVO getPaymentInfoById(String paymentId) {
        PaymentInfo paymentInfo = this.getById(paymentId);
        if (paymentInfo == null) {
            return null;
        }

        // 将 PaymentInfo 转换为 PaymentInfoVO
        PaymentInfoVO paymentInfoVO = new PaymentInfoVO();
        paymentInfoVO.setPaymentId(paymentInfo.getId());
        paymentInfoVO.setOrderId(paymentInfo.getOrderId());
        paymentInfoVO.setPaymentMethod(paymentInfo.getPaymentMethod());
        paymentInfoVO.setPaymentDate(paymentInfo.getPaymentDate());
        paymentInfoVO.setAmountDue(paymentInfo.getAmountDue());
        paymentInfoVO.setAmountPaid(paymentInfo.getAmountPaid());
        paymentInfoVO.setIsCompleted(paymentInfo.getIsCompleted());

        return paymentInfoVO;
    }
}
