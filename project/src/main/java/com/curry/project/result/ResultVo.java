package com.curry.project.result;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "result")
@Entity
public class ResultVo {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_name", length = 50)
    @NotBlank(message = "userName is required")
    @Size(max = 50, message = "userName must be at most 50 characters")
    private String userName;

    @Min(value = 1, message = "gender format error")
    @Max(value = 3, message = "gender format error")
    @NotNull(message = "gender is required")
    private Integer gender;

    @Min(value = 1, message = "age format error")
    @Max(value = 7, message = "age format error")
    @NotNull(message = "age is required")
    private Integer age;

    @NotBlank(message = "ans1 is required")
    @Size(max = 50, message = "ans1 must be at most 50 characters")
    private String ans1;

    @NotBlank(message = "ans2 is required")
    @Size(max = 50, message = "ans2 must be at most 50 characters")
    private String ans2;

    @Size(max = 50, message = "ans3 must be at most 50 characters")
    @NotBlank(message = "ans3 is required")
    private String ans3;

    @Size(max = 50, message = "ans4 must be at most 50 characters")
    @NotBlank(message = "ans4 is required")
    private String ans4;

    @Size(max = 50, message = "ans5 must be at most 50 characters")
    @NotBlank(message = "ans5 is required")
    private String ans5;

    @Size(max = 50, message = "ans6 must be at most 50 characters")
    @NotBlank(message = "ans6 is required")
    private String ans6;

    @Size(max = 50, message = "result must be at most 50 characters")
    @NotBlank(message = "resultName is required")
    @Column(name = "result_name")
    private String resultName;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time",insertable = false,updatable =false)
    @org.hibernate.annotations.Generated
    private OffsetDateTime createdTime;
}
