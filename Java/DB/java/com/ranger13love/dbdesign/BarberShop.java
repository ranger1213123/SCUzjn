package com.ranger13love.dbdesign;
import lombok.Data;
import lombok.Builder;
@Data
@Builder
public class BarberShop {
    private int barbershop_id;//门店
    private double rent;
    private double utility_bills;
    private int barber_id;//理发师编号
    private String location;
}
