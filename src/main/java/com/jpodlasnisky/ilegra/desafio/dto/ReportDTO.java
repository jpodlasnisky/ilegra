package com.jpodlasnisky.ilegra.desafio.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ReportDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private Integer totalSalesman;
    private Integer totalCustomer;
    private Integer mostExpensiveSale;
    private String worstSalesman;

    public String report() {
        return "Total de vendedores: " + totalSalesman + "\n" +
                "Total de clientes: " + totalCustomer + "\n" +
                "Venda mais cara: " + mostExpensiveSale + "\n" +
                "Vendedor que mais vendeu: " + worstSalesman;
    }
}
