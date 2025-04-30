package com.ranger13love.dbdesign;
 import lombok.Data;
 import lombok.Builder;
 @Data
 @Builder
public class Performance {
    private int employee_id;
    private String employee_name;
    private int case_number;
    private double case_point;
    private int rank;
}
