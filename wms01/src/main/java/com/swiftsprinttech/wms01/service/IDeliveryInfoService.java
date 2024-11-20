package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.DeliveryInfo;
import com.swiftsprinttech.wms01.domain.vo.DeliveryInfoVO;

public interface IDeliveryInfoService extends IService<DeliveryInfo> {
    DeliveryInfoVO getDeliveryInfoById(String deliveryId);
}
