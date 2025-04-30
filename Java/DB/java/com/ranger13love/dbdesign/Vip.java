package com.ranger13love.dbdesign;
import lombok.Data;
import lombok.Builder;
@Data
@Builder
public class Vip {
    private int vip_id;
    private int phone_number;
    private String name;
    private int valid_times;
    private int invalid_times;
    private String type;
    private int barbershop_id;
}
