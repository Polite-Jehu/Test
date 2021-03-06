package com.eku.EKU.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureGroupId;
    @Column(nullable = false)
    private String campusType;
    @Column(nullable = false)
    private String groupCode;
    @Column(nullable = false)
    private Short year;
    @Column(nullable = false)
    private Short semester;
}
