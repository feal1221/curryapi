package com.curry.project.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ResultController {

//    @Autowired
//    private ResultService resultService;

    @GetMapping("/share/{id}")
    public String sharePage(@PathVariable String id, Model model) {
//        ResultVo result = resultService.getResultById(id);
//
//        if (result == null) {
//            return "error_page"; // 找不到結果則顯示錯誤頁
//        }

        // 注入資料到 Thymeleaf 模板
        model.addAttribute("title", "測試花朵");
        model.addAttribute("imageUrl", "https://img.ooopenlab.cc/5dY1Wb8bPu-HjU8fdQ986wA8BiTDJz2fl4RJbwbXBzA/rs:fill:1920/q:90/aHR0cHM6Ly9wdWItZGNjNzVkYTE5OGY5NGQ0M2I4OGUxYTVjODcwYzQ0ZTYucjIuZGV2L2EzVEJ5NGd5TEFUMTR2aTlpTEF3QUxkUnNYNDMvcXVpenplcy9aVlEzaXNVSUY2ejFkcVlNbGxYaC9yZXN1bHRzL0VmNnJDSC9jb3Zlci9zdGF0aWMvc3JjP3Q9MTc1MzcwNDQxNQ");
        model.addAttribute("imageUrl2", "https://img.ooopenlab.cc/H0qLLKlSa9wwo1RP9sdFOF1DfHKwW16F4w0713RvPtk/rs:fill:1920/q:90/aHR0cHM6Ly9wdWItZGNjNzVkYTE5OGY5NGQ0M2I4OGUxYTVjODcwYzQ0ZTYucjIuZGV2L2EzVEJ5NGd5TEFUMTR2aTlpTEF3QUxkUnNYNDMvcXVpenplcy9aVlEzaXNVSUY2ejFkcVlNbGxYaC9yZXN1bHRzL2hUSjRKWC9jb3Zlci9zdGF0aWMvc3JjP3Q9MTc1MzcwNDM3Mg");

        // 這是告訴社群平台「這則分享的原網址」
        model.addAttribute("currentUrl", "https://tw.yahoo.com/");

        return "share_template";
    }
}
