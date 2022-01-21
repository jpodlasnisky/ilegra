package com.jpodlasnisky.ilegra.desafio.steps;

import com.jpodlasnisky.ilegra.desafio.config.Properties;
import com.jpodlasnisky.ilegra.desafio.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class Decision implements JobExecutionDecider {

    @Autowired
    private Properties properties;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        log.info("Looking for the next step/file to execute");

        File[] files = Utils.findAll(properties.getDirectory() + properties.getInput(), properties.getExtension());
        if (ArrayUtils.isEmpty(files)) {
            return FlowExecutionStatus.STOPPED;
        }
        return FlowExecutionStatus.COMPLETED;

    }
}
