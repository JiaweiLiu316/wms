package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.CustomerFavoriteGoodsInfo;
import com.swiftsprinttech.wms01.domain.vo.ProductBasicInfoVO;
import com.swiftsprinttech.wms01.domain.vo.ProductIdAndNameVO;
import com.swiftsprinttech.wms01.mappers.CustomerFavoriteGoodsInfoMapper;
import com.swiftsprinttech.wms01.service.ICustomerFavoriteGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiawe
 */
@Service
public class CustomerFavoriteGoodsInfoServiceImpl extends ServiceImpl<CustomerFavoriteGoodsInfoMapper, CustomerFavoriteGoodsInfo> implements ICustomerFavoriteGoodsInfoService {
    @Autowired
    private ProductInfoServiceImpl productInfoService;
    @Override
    public List<ProductIdAndNameVO> getGoodsByCustomerId(String customerId) {
        return this.list(new QueryWrapper<CustomerFavoriteGoodsInfo>()
                        .eq("customer_id", customerId))
                .stream()
                .map(this::convertToProductIdAndNameVO)
                .collect(Collectors.toList());
    }

    /**
     * 将 CustomerFavoriteGoodsInfo 转换为 ProductIdAndNameVO
     */
    private ProductIdAndNameVO convertToProductIdAndNameVO(CustomerFavoriteGoodsInfo customerFavoriteGoodsInfo) {
        String productId = customerFavoriteGoodsInfo.getProductId();
        ProductBasicInfoVO productInfo = productInfoService.getProductBasicInfoById(productId);

        if (productInfo == null) {
            return null;
        }

        ProductIdAndNameVO productIdAndNameVO = new ProductIdAndNameVO();
        productIdAndNameVO.setProductId(productId);
        productIdAndNameVO.setProductName(productInfo.getProductName());
        productIdAndNameVO.setProductCategory(productInfo.getProductCategory());
        productIdAndNameVO.setUnitPrice(productInfo.getProductUnitPrice());

        return productIdAndNameVO;
    }
}
