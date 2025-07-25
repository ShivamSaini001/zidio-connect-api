package com.zidio.connect.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EducationDto {

    private Long educationId;
    private String collegeName;
    private String courseName;
    private String branch;
    private String universityName;
    private int startingYear;
    private int passingYear;
    private String resultType; // percentage, CGPA
    private double result;
}
