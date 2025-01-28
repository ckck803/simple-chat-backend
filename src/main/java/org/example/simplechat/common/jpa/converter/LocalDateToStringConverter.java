package org.example.simplechat.common.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalDateToStringConverter implements AttributeConverter<LocalDate, String> {

    // private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return (attribute == null) ? null : attribute.format(FORMATTER);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : LocalDate.parse(dbData, FORMATTER);
    }
}