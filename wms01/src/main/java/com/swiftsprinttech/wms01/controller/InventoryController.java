package com.swiftsprinttech.wms01.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swiftsprinttech.wms01.domain.entity.InboundInfo;
import com.swiftsprinttech.wms01.domain.entity.ProductInfo;
import com.swiftsprinttech.wms01.domain.vo.ProductIdAndNameVO;
import com.swiftsprinttech.wms01.service.IInboundInfoService;
import com.swiftsprinttech.wms01.service.IProductInfoService;
import com.swiftsprinttech.wms01.utils.Result;
import com.swiftsprinttech.wms01.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 入库操作控制器
 * @author jiawe
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private IProductInfoService productInfoService;

    @Autowired
    private IInboundInfoService iInboundInfoService;

    /**
     * 查询所有入库记录（分页 + 模糊查询）
     */
    @GetMapping("/list")
    public ResponseEntity<Result<Page<InboundInfo>>> getAllInboundRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,  // 页码，默认为1
            @RequestParam(value = "size", defaultValue = "10") Integer size,  // 每页数量，默认为10
            @RequestParam(value = "productId", required = false) String productId,  // 商品ID
            @RequestParam(value = "productName", required = false) String productName  // 商品名称
    ) {
        // 创建分页对象
        Page<InboundInfo> pageRequest = new Page<>(page, size);

        // 创建查询条件
        QueryWrapper<InboundInfo> queryWrapper = new QueryWrapper<>();

        // 模糊查询条件
        if (productId != null && !productId.isEmpty()) {
            queryWrapper.like("product_id", productId);
        }
        if (productName != null && !productName.isEmpty()) {
            queryWrapper.like("product_name", productName);
        }

        // 执行查询并返回分页结果
        Page<InboundInfo> result = iInboundInfoService.page(pageRequest, queryWrapper);

        // 返回成功的响应
        return ResultUtil.success("查询成功", result);
    }


    /**
     * 新增入库记录
     * 入库时需要更新商品库存
     */
    @PostMapping("/add")
    public ResponseEntity<Result<InboundInfo>> addInboundRecord(@RequestBody InboundInfo inboundInfo) {
        // 检查商品是否存在
        ProductInfo product = productInfoService.getById(inboundInfo.getProductId());
        if (product == null) {
            return ResultUtil.error("商品不存在");
        }

        // 更新商品库存
        product.setStock(product.getStock() + inboundInfo.getQuantity());
        boolean updated = productInfoService.updateById(product);
        if (!updated) {
            return ResultUtil.error("商品库存更新失败");
        }
        // 填充入库时间
        inboundInfo.setInboundTime(LocalDateTime.now());
        // 保存入库记录
        boolean saved = iInboundInfoService.save(inboundInfo);
        if (saved) {
            return ResultUtil.success("入库记录添加成功", inboundInfo);
        } else {
            return ResultUtil.error("入库记录添加失败");
        }
    }

    /**
     * 修改入库记录
     */
    @PutMapping("/update")
    public ResponseEntity<Result<InboundInfo>> updateInboundRecord(@RequestBody InboundInfo inboundInfo) {
        // 检查入库记录是否存在
        InboundInfo existingRecord = iInboundInfoService.getById(inboundInfo.getId());
        if (existingRecord == null) {
            return ResultUtil.error("入库记录不存在");
        }

        // 更新商品库存
        ProductInfo product = productInfoService.getById(inboundInfo.getProductId());
        if (product == null) {
            return ResultUtil.error("商品不存在");
        }
        // 更新库存
        product.setStock(product.getStock() + inboundInfo.getQuantity() - existingRecord.getQuantity());
        boolean updatedProduct = productInfoService.updateById(product);
        if (!updatedProduct) {
            return ResultUtil.error("商品库存更新失败");
        }
        // 填充入库时间
        inboundInfo.setInboundTime(LocalDateTime.now());
        // 更新入库记录
        boolean updatedInbound = iInboundInfoService.updateById(inboundInfo);
        if (updatedInbound) {
            return ResultUtil.success("入库记录更新成功", inboundInfo);
        } else {
            return ResultUtil.error("入库记录更新失败");
        }
    }

    /**
     * 删除入库记录
     * 删除时需要恢复商品库存
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result<Void>> deleteInboundRecord(@PathVariable String id) {
        // 检查入库记录是否存在
        InboundInfo existingRecord = iInboundInfoService.getById(id);
        if (existingRecord == null) {
            return ResultUtil.error("入库记录不存在");
        }

        // 恢复商品库存
        ProductInfo product = productInfoService.getById(existingRecord.getProductId());
        if (product != null) {
            product.setStock(product.getStock() - existingRecord.getQuantity());
            productInfoService.updateById(product);
        }

        // 删除入库记录
        boolean deleted = iInboundInfoService.removeById(id);
        if (deleted) {
            return ResultUtil.success("入库记录删除成功");
        } else {
            return ResultUtil.error("入库记录删除失败");
        }
    }

    /**
     * 获取单个入库记录详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<Result<InboundInfo>> getInboundRecordDetail(@PathVariable String id) {
        // 查询入库记录
        InboundInfo inboundInfo = iInboundInfoService.getById(id);
        if (inboundInfo != null) {
            return ResultUtil.success("查询成功", inboundInfo);
        } else {
            return ResultUtil.error("入库记录不存在");
        }
    }
}
