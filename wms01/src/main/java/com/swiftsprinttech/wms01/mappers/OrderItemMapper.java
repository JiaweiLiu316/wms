package com.swiftsprinttech.wms01.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swiftsprinttech.wms01.domain.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiawe
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
