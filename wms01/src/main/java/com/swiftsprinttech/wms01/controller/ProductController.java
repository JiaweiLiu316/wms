package com.swiftsprinttech.wms01.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swiftsprinttech.wms01.domain.entity.ProductInfo;
import com.swiftsprinttech.wms01.domain.vo.ProductIdAndNameVO;
import com.swiftsprinttech.wms01.service.IProductInfoService;
import com.swiftsprinttech.wms01.utils.Result;
import com.swiftsprinttech.wms01.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品操作控制器
 * @author jiawei
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IProductInfoService productInfoService;

    @GetMapping("/list")
    public ResponseEntity<Result<Page<ProductInfo>>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,  // 页码，默认为1
            @RequestParam(value = "size", defaultValue = "10") Integer size,  // 每页数量，默认为10
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "name", required = false) String name,  // 商品名称（模糊查询）
            @RequestParam(value = "category", required = false) String category,  // 商品分类（模糊查询）
            @RequestParam(value = "region", required = false) String region  // 商品地区（模糊查询）
    ) {
        try{
            // 创建分页对象
            Page<ProductInfo> pageRequest = new Page<>(page, size);

            // 创建 Lambda 查询构造器
            LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

            // 模糊查询条件
            if (id != null && !id.isEmpty()) {
                queryWrapper.like(ProductInfo::getId, id);
            }
            if (name != null && !name.isEmpty()) {
                queryWrapper.like(ProductInfo::getName, name);
            }
            if (category != null && !category.isEmpty()) {
                queryWrapper.like(ProductInfo::getCategory, category);
            }
            if (region != null && !region.isEmpty()) {
                queryWrapper.like(ProductInfo::getRegion, region);
            }

            // 执行查询并返回分页结果
            Page<ProductInfo> result = productInfoService.page(pageRequest, queryWrapper);

            // 返回成功的响应
            return ResultUtil.success("查询成功", result);
        }catch (Exception e){
            System.out.println(e.getMessage());
            // 返回成功的响应
            return ResultUtil.error("出错");
        }

    }

    // 根据ID查询商品
    @GetMapping("/{id}")
    public ResponseEntity<Result<ProductInfo>> getProductById(@PathVariable("id") String id) {
        ProductInfo product = productInfoService.getById(id);
        if (product != null) {
            return ResultUtil.success("查询成功", product);
        } else {
            return ResultUtil.error("商品未找到");
        }
    }

    // 新增商品
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addProduct(@RequestBody ProductInfo productInfo) {
        boolean isSaved = productInfoService.save(productInfo);
        if (isSaved) {
            return ResultUtil.success("商品新增成功");
        } else {
            return ResultUtil.error("商品新增失败");
        }
    }

    // 修改商品
    @PutMapping("/update")
    public ResponseEntity<Result<Boolean>> updateProduct(@RequestBody ProductInfo productInfo) {
        boolean isUpdated = productInfoService.updateById(productInfo);
        if (isUpdated) {
            return ResultUtil.success("商品修改成功");
        } else {
            return ResultUtil.error("商品修改失败");
        }
    }

    // 删除商品
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result<Boolean>> deleteProduct(@PathVariable("id") String id) {
        boolean isDeleted = productInfoService.removeById(id);
        if (isDeleted) {
            return ResultUtil.success("商品删除成功");
        } else {
            return ResultUtil.error("商品删除失败");
        }
    }

    /**
     * @description: 获取产品标识和名称列表
     * @param: []
     * @return: org.springframework.http.ResponseEntity<com.swiftsprinttech.wms01.utils.Result<com.swiftsprinttech.wms01.domain.vo.ProductIdAndNameVO>>
     * @date: 2024/11/17 上午 1:27
     */
    @GetMapping("/productIdAndNameList")
    public ResponseEntity<Result<List<ProductIdAndNameVO>>> getProductIdAndNameList(){
        // 获取所有商品信息
        List<ProductInfo> productList = productInfoService.list();
        // 转换为 ProductIdAndNameVO 列表
        List<ProductIdAndNameVO> productIdAndNameList = productList.stream()
                .map(product -> {
                    ProductIdAndNameVO vo = new ProductIdAndNameVO();
                    vo.setProductId(product.getId());
                    vo.setProductName(product.getName());
                    vo.setProductCategory(product.getCategory());
                    vo.setUnitPrice(product.getPrice());
                    return vo;
                })
                .collect(Collectors.toList());

        return ResultUtil.success("查询成功",productIdAndNameList);

    }
}
