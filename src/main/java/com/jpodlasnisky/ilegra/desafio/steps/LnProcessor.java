package com.jpodlasnisky.ilegra.desafio.steps;

import com.jpodlasnisky.ilegra.desafio.dto.ReportDTO;
import com.jpodlasnisky.ilegra.desafio.model.BaseEntity;
import com.jpodlasnisky.ilegra.desafio.model.Customer;
import com.jpodlasnisky.ilegra.desafio.model.Sale;
import com.jpodlasnisky.ilegra.desafio.model.Salesman;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LnProcessor implements Tasklet, StepExecutionListener {

    private List<BaseEntity> entities;
    private ReportDTO reportDTO;

    @Override
    public void beforeStep(org.springframework.batch.core.StepExecution stepExecution) {
        log.info("Processing entities");
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();

        this.entities = (List<BaseEntity>) executionContext.get("entities");
    }

    @Override
    public ExitStatus afterStep(org.springframework.batch.core.StepExecution stepExecution) {
        log.info("Processing entities finished");
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        executionContext.put("reportDTO", reportDTO);

        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(org.springframework.batch.core.StepContribution contribution, org.springframework.batch.core.scope.context.ChunkContext chunkContext) throws Exception {
        log.info("Processing entities");
        AtomicInteger salesmanAmount = new AtomicInteger();
        AtomicInteger customersAmount = new AtomicInteger();
        List<Sale> sales = new ArrayList<>();

        entities.forEach(entity -> {
            log.info("Processing entity: {}", entity);
            if (entity instanceof Salesman) {
                salesmanAmount.getAndIncrement();
                log.info("Salesman amount: {}", salesmanAmount);
            } else if (entity instanceof Customer) {
                customersAmount.getAndIncrement();
                log.info("Customer amount: {}", customersAmount);
            } else if (entity instanceof Sale) {
                Sale sale = (Sale) entity;
                sales.add(sale);
                log.info("Sale amount: {}", sales.size());
            }
        });

        this.reportDTO = ReportDTO.builder()
                .totalSalesman(NumberUtils.parseNumber(salesmanAmount.toString(), Integer.class))
                .totalCustomer(NumberUtils.parseNumber(customersAmount.toString(), Integer.class))
                .build();

        if(CollectionUtils.isNotEmpty(sales)) {
            sales.sort((s1, s2) -> s2.getTotal().compareTo(s1.getTotal()));

            this.reportDTO.setMostExpensiveSale(sales.get(0).getSaleId());
            this.reportDTO.setWorstSalesman(Iterables.getLast(sales).getSalesman());
        }
        log.info("Processing entities finished");

        return RepeatStatus.FINISHED;
    }

}
