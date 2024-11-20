package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.ProductInfo;
import com.swiftsprinttech.wms01.domain.vo.ProductBasicInfoVO;
import com.swiftsprinttech.wms01.mappers.ProductInfoMapper;
import com.swiftsprinttech.wms01.service.IProductInfoService;
import org.springframework.stereotype.Service;

/**
 * @author jiawe
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

    @Override
    public ProductBasicInfoVO getProductBasicInfoById(String productId) {
        // 使用 MyBatis-Plus 的 getById 方法获取商品信息
        ProductInfo productInfo = this.getById(productId);
        if (productInfo == null) {
            return null;
        }

        // 将 ProductInfo 转换为 ProductBasicInfoVO
        ProductBasicInfoVO productBasicInfoVO = new ProductBasicInfoVO();
        productBasicInfoVO.setProductId(productInfo.getId());
        productBasicInfoVO.setProductName(productInfo.getName());
        productBasicInfoVO.setRegion(productInfo.getRegion());
        productBasicInfoVO.setProductCategory(productInfo.getCategory());
        productBasicInfoVO.setProductUnitPrice(productInfo.getPrice());
        productBasicInfoVO.setDescription(productInfo.getDescription());
        productBasicInfoVO.setProductStock(productInfo.getStock());
        // 根据需要填充其他字段

        return productBasicInfoVO;
    }
}
