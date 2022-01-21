package com.jpodlasnisky.ilegra.desafio.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Salesman extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String cpf;
    private String name;
    private String salary;

}
