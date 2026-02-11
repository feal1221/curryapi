package com.curry.project.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

//@Service
public class ResultService {

//    @Autowired
    private ResultRepository repository;

    // 根據 ID 取得結果
    public ResultVo getResultById(String id) {
        return repository.findById(id).orElse(null);
    }

    // 模擬存入測驗結果的方法（實際測驗結束後會呼叫這個）
//    public UUID saveResult(String flowerName, String imgUrl) {
//        ResultVo result = new ResultVo();
//        result.setId(java.util.UUID.randomUUID());
//        result.setResultName(flowerName);
//        result.setImageUrl(imgUrl);
//        result.setDescription("我是" + flowerName + "，一起來測你是哪種花朵?");
//
//        repository.save(result);
//        return result.getId();
//    }
}