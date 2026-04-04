package com.curry.project.result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name="Curry",description = "測驗結果相關API")
@RestController
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://curryui.vercel.app",
        "https://housefindyourcurry.tw",
        "https://test.housefindyourcurry.tw",
        "https://api.housefindyourcurry.tw",
})
public class ResultController {

    @Autowired
    private ResultService resultService;

    @Operation(summary = "儲存結果", description = "儲存結果")
    @PostMapping("/results/create")
    public String createUser(@Valid @RequestBody ResultVo resultVo)  {
        ResultVo newResult = resultService.saveResult(resultVo);
        return "Success"; // 儲存後重定向到分享頁面
    }

    @Operation(summary = "匯出Excel", description = "匯出Excel")
    @GetMapping("/results/exportExcel")
    public void exportExcel() {
        try {
            resultService.exportExcel();
        } catch (Exception e) {
            // 這裡可以導向錯誤處理或回傳 JSON 錯誤訊息
            e.printStackTrace();
        }
    }


//    @GetMapping("/share")
//    public String sharePage(@PathVariable  Model model) {
////        ResultVo result = resultService.getResultById(id);
////
////        if (result == null) {
////            return "error_page"; // 找不到結果則顯示錯誤頁
////        }
//
//        // 注入資料到 Thymeleaf 模板
//        model.addAttribute("title", "咖哩靈魂拌測驗：測出你的咖哩人格！");
//        model.addAttribute("imageUrl", "https://img.ooopenlab.cc/5dY1Wb8bPu-HjU8fdQ986wA8BiTDJz2fl4RJbwbXBzA/rs:fill:1920/q:90/aHR0cHM6Ly9wdWItZGNjNzVkYTE5OGY5NGQ0M2I4OGUxYTVjODcwYzQ0ZTYucjIuZGV2L2EzVEJ5NGd5TEFUMTR2aTlpTEF3QUxkUnNYNDMvcXVpenplcy9aVlEzaXNVSUY2ejFkcVlNbGxYaC9yZXN1bHRzL0VmNnJDSC9jb3Zlci9zdGF0aWMvc3JjP3Q9MTc1MzcwNDQxNQ");
//
//        // 這是告訴社群平台「這則分享的原網址」
//        model.addAttribute("currentUrl", "https://curryui.vercel.app/");
//
//        return "share_template";
//    }

}
