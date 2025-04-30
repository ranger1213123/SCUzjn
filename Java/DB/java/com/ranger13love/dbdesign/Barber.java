package com.ranger13love.dbdesign;
import lombok.Data;
import lombok.Builder;
@Data
@Builder
public class Barber {
    private int barber_id;
    private String barber_name;
    private double salary;
    private double view_point;
    private int case_number;
    private int position_id;
}
