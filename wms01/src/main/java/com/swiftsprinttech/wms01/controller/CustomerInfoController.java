package com.swiftsprinttech.wms01.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swiftsprinttech.wms01.domain.entity.CustomerInfo;
import com.swiftsprinttech.wms01.domain.entity.OrderInfo;
import com.swiftsprinttech.wms01.domain.entity.ProductInfo;
import com.swiftsprinttech.wms01.domain.vo.CustomerAllInfoVO;
import com.swiftsprinttech.wms01.domain.vo.CustomerInfoVO;
import com.swiftsprinttech.wms01.domain.vo.ProductIdAndNameVO;
import com.swiftsprinttech.wms01.service.ICustomerAddressInfoService;
import com.swiftsprinttech.wms01.service.ICustomerFavoriteGoodsInfoService;
import com.swiftsprinttech.wms01.service.ICustomerInfoService;
import com.swiftsprinttech.wms01.utils.Result;
import com.swiftsprinttech.wms01.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiawe
 */
@RestController
@RequestMapping("/api/customerInfo")
public class CustomerInfoController {
    @Autowired
    private ICustomerInfoService customerInfoService;
    @Autowired
    private ICustomerAddressInfoService customerAddressInfoService;
    @Autowired
    private ICustomerFavoriteGoodsInfoService customerFavoriteGoodsInfoService;
    // 新增用戶
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addProduct(@RequestBody CustomerInfoVO customerInfoVO) {
        CustomerInfo customerInfo = new CustomerInfo();
        BeanUtils.copyProperties(customerInfoVO, customerInfo);
        CustomerInfo one = customerInfoService.getOne(new LambdaQueryWrapper<CustomerInfo>().eq(CustomerInfo::getPhoneNumber, customerInfoVO.getPhoneNumber()));
        if (one != null){
            return ResultUtil.error("用户已存在（电话号码相同），无需添加");
        }
        boolean isSaved = customerInfoService.save(customerInfo);
        if (isSaved) {
            return ResultUtil.success("用戶新增成功");
        } else {
            return ResultUtil.error("用戶新增失敗");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Result<Page<CustomerAllInfoVO>>> getAllCustomer(
            @RequestParam(value = "page", defaultValue = "1") Integer page,  // 页码，默认为1
            @RequestParam(value = "size", defaultValue = "10") Integer size  // 每页数量，默认为10

    ) {
        try{
            // 创建分页对象
            Page<CustomerInfo> pageRequest = new Page<>(page, size);
            Page<CustomerInfo> customerInfoPage = customerInfoService.page(pageRequest, null);
            // 执行查询并返回分页结果
            List<CustomerAllInfoVO> result = customerInfoPage
                    .getRecords().stream().map(customerInfo -> {
                        CustomerAllInfoVO customerAllInfoVO = new CustomerAllInfoVO();
                        customerAllInfoVO.setCustomerId(customerInfo.getId());
                        customerAllInfoVO.setCustomerName(customerInfo.getName());
                        customerAllInfoVO.setPhoneNumber(customerInfo.getPhoneNumber());
                        customerAllInfoVO.setRepurchaseAmount(customerInfo.getRepurchaseAmount());

                        List<String> addressByCustomerId = customerAddressInfoService.getAddressByCustomerId(customerInfo.getId());
                        customerAllInfoVO.setAddress(addressByCustomerId);
                        List<ProductIdAndNameVO> goodsByCustomerId = customerFavoriteGoodsInfoService.getGoodsByCustomerId(customerInfo.getId());
                        customerAllInfoVO.setProductIdAndNameVOS(goodsByCustomerId);
                        return customerAllInfoVO;
                    }).collect(Collectors.toList());
            Page<CustomerAllInfoVO> resultPage = new Page<>();
            resultPage.setCurrent(customerInfoPage.getCurrent());
            resultPage.setSize(customerInfoPage.getSize());
            resultPage.setTotal(customerInfoPage.getTotal());
            resultPage.setRecords(result);
            // 返回成功的响应
            return ResultUtil.success("查询成功", resultPage);
        }catch (Exception e){
            System.out.println(e.getMessage());
            // 返回成功的响应
            return ResultUtil.error("出错");
        }

    }
}
