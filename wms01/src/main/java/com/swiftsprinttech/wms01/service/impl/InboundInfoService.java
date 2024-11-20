package com.swiftsprinttech.wms01.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swiftsprinttech.wms01.domain.entity.InboundInfo;
import com.swiftsprinttech.wms01.mappers.InboundInfoMapper;
import com.swiftsprinttech.wms01.service.IInboundInfoService;
import org.springframework.stereotype.Service;

@Service
public class InboundInfoService extends ServiceImpl<InboundInfoMapper, InboundInfo> implements IInboundInfoService {
}
