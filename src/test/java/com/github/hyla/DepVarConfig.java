package com.github.hyla;

import com.github.hyla.depvar.DependentVarContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class DepVarConfig {

    @Bean
    public DependentVarContext dependentVarContext() {
        return new DependentVarContext();
    }
}
