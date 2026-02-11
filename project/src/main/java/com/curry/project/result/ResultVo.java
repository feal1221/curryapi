package com.curry.project.result;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.UUID;

//@Builder
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "result")
//@Entity
public class ResultVo {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "id")
    private UUID id;

    private String resultName; // 例如：月光花

    @Column(length = 1000)
    private String imageUrl;   // 儲存產生的圖片完整連結

    private String description; // 預設分享文字

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time",insertable=false,updatable=false)
    private OffsetDateTime createdTime;
}
