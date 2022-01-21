package com.jpodlasnisky.ilegra.desafio.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Properties {

    @Value("${dir}")
    private String directory;

    @Value("${input}")
    private String input;

    @Value("${output}")
    private String output;

    @Value("${processing}")
    private String processing;

    @Value("${processed}")
    private String processed;

    @Value("${extension}")
    private String extension;

    @Value("${outputExtension}")
    private String outputExtension;
}
