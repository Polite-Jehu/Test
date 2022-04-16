package com.eku.EKU.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class BoardList {
    private Long id;
    private String title;
    private String name;
    private Long no;
    private String department;
}
