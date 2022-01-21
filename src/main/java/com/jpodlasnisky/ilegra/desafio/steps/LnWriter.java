package com.jpodlasnisky.ilegra.desafio.steps;

import com.jpodlasnisky.ilegra.desafio.config.Properties;
import com.jpodlasnisky.ilegra.desafio.dto.ReportDTO;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
public class LnWriter implements Tasklet, StepExecutionListener {

    @Autowired
    private Properties properties;

    private String filename;
    private ReportDTO reportDTO;

    private String getProcessedFilePath() {
        return properties.getDirectory() + properties.getProcessed() + "/" + filename;
    }

    private String getProcessingFilePath() {
        return properties.getDirectory() + properties.getProcessing() + "/" + filename;
    }

    private String getFileOutputPath(String filename) {
        return properties.getDirectory() + properties.getOutput() + "/" + filename + properties.getOutputExtension();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();

        reportDTO = (ReportDTO) executionContext.get("reportDTO");
        filename = (String) executionContext.get("filename");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        try {
            FileUtils.moveFile(new File(getProcessingFilePath()), new File(getProcessedFilePath()));
        } catch (IOException e) {
            log.error("Error when tried to move file from processing folder to processed folder", e);
            return ExitStatus.FAILED;
        }

        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("Writing file {}", filename);

        String filename = Utils.removeExtension(this.filename);

        File file = new File(getFileOutputPath(filename));

        FileUtils.writeStringToFile(file, reportDTO.report(), "UTF-8");

        log.debug("File {} written", filename);

        return RepeatStatus.FINISHED;
    }

}
