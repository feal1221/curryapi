package com.curry.project.result;


import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.export.recipient}")
    private String exportRecipient;


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

            // 取得所有資料用於 Excel
            List<ResultVo> results = repository.findAll();
            
            // 取得當下台北時間
            OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Taipei"));
            
            // 計算本週一 00:00:00 作為結束時間 (避免算到週一 00:00~08:00，下週執行時又重複計算)
            OffsetDateTime thisMondayStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .toLocalDate()
                    .atStartOfDay(ZoneId.of("Asia/Taipei"))
                    .toOffsetDateTime();
                    
            // 計算上週一 00:00:00 作為起始時間
            OffsetDateTime lastMondayStart = thisMondayStart.minusWeeks(1);
            
            // 配合資料庫時間是 +0 (UTC)，將查詢區間時間明確轉換為 UTC (+0)
            OffsetDateTime startUtc = lastMondayStart.withOffsetSameInstant(java.time.ZoneOffset.UTC);
            OffsetDateTime endUtc = thisMondayStart.withOffsetSameInstant(java.time.ZoneOffset.UTC);
            
            // 由於 between 預設為包含上下界 (<= end)，為了嚴謹，將結束時間扣減1毫秒，變為週日 23:59:59.999
            endUtc = endUtc.minus(1, java.time.temporal.ChronoUnit.MILLIS);

            List<ResultVo> weeklyResults = repository.findByCreatedTimeBetweenOrderByCreatedTimeAsc(startUtc, endUtc);
            int weeklyCount = weeklyResults.size();

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
                    dateCell.setCellValue(result.getCreatedTime().atZoneSameInstant(ZoneId.of("Asia/Taipei")).format(formatter));
//                    dateCell.setCellValue(result.getCreatedTime().plusHours(8).format(formatter));

                } else {
                    dateCell.setCellValue("-"); // 或留白
                }
            }
            workbook.write(bos);
            excelContent = bos.toByteArray();
            workbook.dispose(); // 清理暫存檔
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(exportRecipient);
        String dateStr = LocalDate.now(ZoneId.of("Asia/Taipei")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        helper.setSubject("饋咖測驗遊戲活動頁專案－每周測驗結果匯出 (" + dateStr + ")");
        String emailText = String.format(
                "您好，\n\n本周新增了 %d 筆資料。\n附件為截至目前的完整測驗結果 Excel 檔案，請查收。",
                weeklyCount
        );
        helper.setText(emailText);
        String fileName = "饋咖測驗遊戲活動頁專案-測驗結果" + dateStr + ".xlsx";
        helper.addAttachment(fileName, new ByteArrayResource(excelContent));
            mailSender.send(message);
            System.out.println("郵件已成功寄出");
        }catch (Exception e){
            System.err.println("郵件寄送失敗: " + e.getMessage());
        }
    }
}