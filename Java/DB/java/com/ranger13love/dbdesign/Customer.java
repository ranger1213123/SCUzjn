package com.ranger13love.dbdesign;
import lombok.Data;
import lombok.Builder;
@Data
@Builder
public class Customer {
    private int customer_id;
    private int phone_number;
    private int vip_number;
}
