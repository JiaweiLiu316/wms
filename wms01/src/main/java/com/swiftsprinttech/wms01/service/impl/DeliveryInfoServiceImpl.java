package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.DeliveryInfo;
import com.swiftsprinttech.wms01.domain.vo.DeliveryInfoVO;
import com.swiftsprinttech.wms01.mappers.DeliveryInfoMapper;
import com.swiftsprinttech.wms01.service.IDeliveryInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jiawe
 */
@Service
public class DeliveryInfoServiceImpl extends ServiceImpl<DeliveryInfoMapper, DeliveryInfo> implements IDeliveryInfoService {
    @Override
    public DeliveryInfoVO getDeliveryInfoById(String deliveryId) {
        DeliveryInfo deliveryInfo = this.getById(deliveryId);
        if (deliveryInfo == null) {
            return null;
        }

        // 将 DeliveryInfo 转换为 DeliveryInfoVO
        DeliveryInfoVO deliveryInfoVO = new DeliveryInfoVO();
        deliveryInfoVO.setDeliveryId(deliveryInfo.getId());
        deliveryInfoVO.setOrderId(deliveryInfo.getOrderId());
        deliveryInfoVO.setDeliveryMethod(deliveryInfo.getDeliveryMethod());
        deliveryInfoVO.setDeliveryDate(deliveryInfo.getDeliveryDate());
        deliveryInfoVO.setDeliveryAddress(deliveryInfo.getDeliveryAddress());
        deliveryInfoVO.setIsCompleted(deliveryInfo.getIsCompleted());

        return deliveryInfoVO;
    }
}
