package com.jpodlasnisky.ilegra.desafio.config;

import com.jpodlasnisky.ilegra.desafio.steps.Decision;
import com.jpodlasnisky.ilegra.desafio.steps.LnProcessor;
import com.jpodlasnisky.ilegra.desafio.steps.LnReader;
import com.jpodlasnisky.ilegra.desafio.steps.LnWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class Tasks {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public JobRepository jobRepository() throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        factory.setTransactionManager(transactionManager());
        return (JobRepository) factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public Decision decision() {
        return new Decision();
    }

    @Bean
    public LnReader linesReader() {
        return new LnReader();
    }

    @Bean
    public LnProcessor linesProcessor() {
        return new LnProcessor();
    }

    @Bean
    public LnWriter linesWriter() {
        return new LnWriter();
    }

    @Bean
    protected Step readLines() {
        return steps.get("readLines")
                .tasklet(linesReader())
                .build();
    }

    @Bean
    protected Step processLines() {
        return steps.get("processLines")
                .tasklet(linesProcessor())
                .build();
    }

    @Bean
    protected Step writeLines() {
        return steps.get("writeLines")
                .tasklet(linesWriter())
                .build();
    }

    @Bean
    public Job job() {
        Flow flow = new FlowBuilder<Flow>("flow1")
                .start(decision())
                .on(FlowExecutionStatus.COMPLETED.toString())
                .to(readLines())
                .next(processLines())
                .next(writeLines())
                .from(decision())
                .on(FlowExecutionStatus.STOPPED.toString())
                .stop()
                .end();

        return jobs.get("taskletsJob")
                .start(flow)
                .end()
                .build();
    }
}
