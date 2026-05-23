package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.entity.Collect;
import com.example.entity.CollectDto.QuaryCollectDto;
import com.example.service.CollectService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/Collect")
public class CollectController {

    @Resource
    private CollectService CollectService;


//    @GetMapping("/praise/{id}")
//    public Result praise(@PathVariable Integer id) {
//        CollectService.praise(id);
//        return Result.success("添加成功");
//    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(Integer pictureId) {
        CollectService.addCollect(pictureId);
        return Result.success("添加成功");
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        CollectService.deleteById(id);
        return Result.success("删除成功");
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Collect Collect) {
        CollectService.updateById(Collect);
        return Result.success("更新成功");
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        CollectService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Collect Collect = CollectService.selectById(id);
        return Result.success(Collect);
    }

    /**
     * 查询所有
     */
    @PostMapping("/selectAll")
    public Result selectAll(@RequestBody QuaryCollectDto quaryCollectDto) {
        List<QuaryCollectDto> list = CollectService.selectAll(quaryCollectDto);
        return Result.success(list);
    }
//
    /**
     * 分页查询
     */
    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody QuaryCollectDto quaryCollectDto,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<QuaryCollectDto> userPage = CollectService.selectPage(quaryCollectDto, pageNum, pageSize);
        return Result.success(userPage);
    }

}
