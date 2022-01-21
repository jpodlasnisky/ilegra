package com.jpodlasnisky.ilegra.desafio.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Sale extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer saleId;
    private List<Item> itemList;
    private String salesman;

    public Double getTotal() {
        Double total = 0.0;
        try {
            for (Item item : itemList) {
                total += item.getQuantity() * item.getPrice();
            }
            return total;
        } catch (Exception e) {
            return total;
        }

    }

}
