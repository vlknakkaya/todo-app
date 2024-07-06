package com.todoapp.config;

import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableCouchbaseRepositories(basePackages = { "com.todoapp" })
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    public static final String SERVER = "localhost";
    public static final String USERNAME = "Administrator";
    public static final String PASSWORD = "123456";
    public static final String BUCKET_NAME = "todoapp";

    @Override
    public String getConnectionString() {
        return SERVER;
    }

    @Override
    public String getUserName() {
        return USERNAME;
    }

    @Override
    public String getPassword() {
        return PASSWORD;
    }

    @Override
    public String getBucketName() {
        return BUCKET_NAME;
    }

    @Override
    public QueryScanConsistency getDefaultConsistency() {
        return QueryScanConsistency.REQUEST_PLUS;
    }

    @Bean
    @Lazy
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    @Lazy
    public ValidatingCouchbaseEventListener validatingCouchbaseEventListener() {
        return new ValidatingCouchbaseEventListener(localValidatorFactoryBean());
    }

}
