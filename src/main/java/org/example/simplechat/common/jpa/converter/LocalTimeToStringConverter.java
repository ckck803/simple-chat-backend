package org.example.simplechat.common.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
@Slf4j
public class LocalTimeToStringConverter implements AttributeConverter<LocalTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // 엔티티 -> 데이터베이스로 변환
    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        return attribute != null ? attribute.format(FORMATTER) : null;
    }

    // 데이터베이스 -> 엔티티로 변환
    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        return dbData != null ? LocalTime.parse(dbData, FORMATTER) : null;
    }
}