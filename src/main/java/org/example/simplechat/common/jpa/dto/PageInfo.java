package org.example.simplechat.common.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    int pageNumber;
    int pageSize;
    int totalPages;
    int numberOfElements;
    Long totalElements;
}
