package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.ProductInfo;
import com.swiftsprinttech.wms01.domain.vo.ProductBasicInfoVO;

public interface IProductInfoService extends IService<ProductInfo> {
    /**
     * @param productId
     * @return
     */
    ProductBasicInfoVO getProductBasicInfoById(String productId);
}
