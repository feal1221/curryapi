package com.curry.project.result;


import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ResultService {

    @Autowired
    private ResultRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    // 根據 ID 取得結果
//    public ResultVo getResultById(String id) {
//        return repository.findById(id).orElse(null);
//    }

    @Transactional
    public ResultVo saveResult(ResultVo result) {
        return repository.save(result);
    }

    public List<ResultVo> findAllResults() {
        return repository.findAll();
    }

    @Async
    public void exportExcel() throws Exception {
        byte[] excelContent;
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()){

            SXSSFSheet sheettt = workbook.createSheet("測驗結果");

            String[] columnNames = {"序號","暱稱", "性別","年齡", "答案1", "答案2", "答案3", "答案4", "答案5","答案6","測驗結果","建立時間"};
            SXSSFRow headerRow =  sheettt.createRow(0);
            for (int i = 0; i < columnNames.length; i++) {
                headerRow.createCell(i).setCellValue(columnNames[i]);
            }

            List<ResultVo> results = repository.findAll();
            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String[] genderMap = {"女","男","多元"};
            String[] ageMap = {"17歲以下","18-25歲","26-35歲","36-45歲","46-55歲","56-65歲","66歲以上"};
            for (ResultVo result : results) {
                SXSSFRow row = (SXSSFRow) sheettt.createRow(rowNum++);
                Integer index = rowNum-1;
                row.createCell(0).setCellValue(index);
                row.createCell(1).setCellValue(result.getUserName());
                Integer genderValue = result.getGender();
                String genderText = (genderValue != null && genderValue >= 1 && genderValue <= 3) ? genderMap[genderValue - 1] : "未知";
                row.createCell(2).setCellValue(genderText);
                Integer ageValue = result.getAge();
                String ageText = (ageValue != null && ageValue >= 1 && ageValue <= 7) ? ageMap[ageValue - 1] : "未知";
                row.createCell(3).setCellValue(ageText);
                row.createCell(4).setCellValue(result.getAns1());
                row.createCell(5).setCellValue(result.getAns2());
                row.createCell(6).setCellValue(result.getAns3());
                row.createCell(7).setCellValue(result.getAns4());
                row.createCell(8).setCellValue(result.getAns5());
                row.createCell(9).setCellValue(result.getAns6());
                row.createCell(10).setCellValue(result.getResultName());
                Cell dateCell = row.createCell(11);
                if (result.getCreatedTime() != null) {
                    // 格式化為字串，安全又美觀
                    dateCell.setCellValue(result.getCreatedTime().plusHours(8).format(formatter));
                } else {
                    dateCell.setCellValue("-"); // 或留白
                }
            }
//            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//            String fileName = "饋咖測驗遊戲活動頁專案-測驗結果" + dateStr + ".xlsx";

            // 2. 進行 URL 編碼，防止中文亂碼
//            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
//                    .replaceAll("\\+", "%20"); // 處理空格變 + 號的問題

            // 3. 設定正確的 Content-Disposition (支援現代瀏覽器)
//            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            workbook.write(bos);
            excelContent = bos.toByteArray();
            workbook.dispose(); // 清理暫存檔
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo("feal1221@gmail.com");
        helper.setSubject("饋咖測驗遊戲活動頁專案－測驗結果excel");
        helper.setText("您好，附件為本次活動的測驗結果 Excel 檔案，請查收。");
        String dateStr = LocalDate.now(ZoneId.of("Asia/Taipei")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fileName = "饋咖測驗遊戲活動頁專案-測驗結果" + dateStr + ".xlsx";
        helper.addAttachment(fileName, new ByteArrayResource(excelContent));
            mailSender.send(message);
            System.out.println("郵件已成功寄出");
        }catch (Exception e){
            System.err.println("郵件寄送失敗: " + e.getMessage());
        }


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