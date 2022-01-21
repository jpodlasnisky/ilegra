package com.jpodlasnisky.ilegra.desafio.cron;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class Cron {

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private Job job;

    @Scheduled(cron="*/5 * * * * *")
    public void cron(){
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            JobExecution execution = launcher.run(job, jobParameters);

            log.info("Execution status {}", execution.getStatus());

        } catch (JobExecutionAlreadyRunningException e) {
            log.error("JobExecutionAlreadyRunningException", e);
        } catch (JobRestartException e) {
            log.error("JobRestartException", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("JobInstanceAlreadyCompleteException", e);
        } catch (JobParametersInvalidException e) {
            log.error("JobParametersInvalidException", e);
        }
    }
}
