package com.curry.project.result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Tag(name="GCP",description = "限定GCP才能用")
@RestController
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://curryui.vercel.app",
        "https://housefindyourcurry.tw",
        "https://test.housefindyourcurry.tw",
        "https://api.housefindyourcurry.tw",
})
public class GcpController {

    @Autowired
    private ResultService resultService;


    // 建議定義在 application.properties 中，不要寫死在程式碼
    @Value("${app.internal.secret}")
    private String internalSecret;

    @PostMapping("/weekly-export")
    public String triggerWeeklyExport(
            @RequestHeader(value = "X-Internal-Secret", required = false) String incomingSecret
    ) {
        if (incomingSecret == null || !incomingSecret.equals(internalSecret)) {
            return "未經授權的訪問";
        }
        try {
            resultService.exportExcel();
        } catch (Exception e) {
            // 這裡可以導向錯誤處理或回傳 JSON 錯誤訊息
            e.printStackTrace();
        }
        return "排程任務已成功啟動";
    }
}
