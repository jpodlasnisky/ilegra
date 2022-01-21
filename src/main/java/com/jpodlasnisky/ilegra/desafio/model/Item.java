package com.jpodlasnisky.ilegra.desafio.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Item extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer itemId;
    private Integer quantity;
    private Double price;
}
