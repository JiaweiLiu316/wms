package com.swiftsprinttech.wms01.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swiftsprinttech.wms01.domain.entity.CustomerFavoriteGoodsInfo;
import com.swiftsprinttech.wms01.domain.vo.ProductIdAndNameVO;

import java.util.List;

public interface ICustomerFavoriteGoodsInfoService extends IService<CustomerFavoriteGoodsInfo> {
    List<ProductIdAndNameVO> getGoodsByCustomerId(String customerId);
}
