package com.jpodlasnisky.ilegra.desafio.steps;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import com.jpodlasnisky.ilegra.desafio.config.Properties;
import com.jpodlasnisky.ilegra.desafio.model.BaseEntity;
import com.jpodlasnisky.ilegra.desafio.parser.FileParser;
import com.jpodlasnisky.ilegra.desafio.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LnReader implements Tasklet, StepExecutionListener {

    private List<BaseEntity> entities;
    private String filename;

    @Autowired
    private Properties properties;

    private FileParser parser;

    @Override
    public void beforeStep(org.springframework.batch.core.StepExecution stepExecution) {
        log.info("LnReader - beforeStep");

        entities = new ArrayList<>();

        try {
            File newFile = Utils.findFirst(
                    properties.getDirectory() + properties.getInput(),
                    properties.getExtension());

            filename = newFile.getName();

            FileUtils.moveFile(newFile, new File(properties.getDirectory() + properties.getProcessing() + '/' + filename));

            File processingFile = Utils.findFirst( properties.getDirectory() + properties.getProcessing(), properties.getExtension());

            parser = new FileParser(processingFile);

        } catch (Exception e) {
            log.error("LnReader - beforeStep - Error: " + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        parser.closeReader();

        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();

        executionContext.put("entities", this.entities);
        executionContext.put("filename", this.filename);

        log.info("Finished reading lines");

        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("LnReader - execute");

        BaseEntity entity = null;

        while ((entity = parser.readLine()) != null) {
            log.debug("LnReader - execute - entity: " + entity);
            entities.add(entity);
        }

        return RepeatStatus.FINISHED;

    }

}
