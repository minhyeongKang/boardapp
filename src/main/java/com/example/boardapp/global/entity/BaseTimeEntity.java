package com.example.boardapp.global.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 모든 Entity에서 공통으로 사용하는 생성/수정 시간 필드
 * 테이블에서는 createdAt, modifiedAt 컬럼과 매핑됨
 * (DB에서 DEFAULT SYSDATE 및 UPDATE TRIGGER로 관리)
 */
@Getter
@Setter
public abstract class BaseTimeEntity {
    protected LocalDateTime createdAt;
    protected LocalDateTime modifiedAt;
}
