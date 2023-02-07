package com.example.springbatch.batch.job;

import com.example.springbatch.batch.domain.Test;
import com.example.springbatch.batch.listner.ReaderListener;
import com.example.springbatch.batch.listner.TestListener;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.concurrent.Future;

@Configuration
@RequiredArgsConstructor
public class RetryJobTest {

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final TestListener testListener;
    private final ReaderListener readerListener;
    private final int chunkSize = 2;

    @Bean
    public Job retryJob() {
        return jobBuilderFactory.get("retryJob")
                .start(retryStep())
                .build();
    }

    @Bean
    public Step retryStep() {
        return stepBuilderFactory.get("retryStep")
                .<Test, Test>chunk(chunkSize)
                .reader(synchronizedItemReader())
                .processor(retryProcessor())
                .writer(retryWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Test> retryReader() {
        return new JpaPagingItemReaderBuilder<Test>()
                .pageSize(chunkSize)
                .queryString("select t from Test t order by id")
                .entityManagerFactory(entityManagerFactory)
                .name("retryReader")
                .build();
    }

    @Bean
    public SynchronizedItemStreamReader<Test> synchronizedItemReader() {
        return new SynchronizedItemStreamReaderBuilder<Test>()
                .delegate(new JpaPagingItemReaderBuilder<Test>()
                        .pageSize(chunkSize)
                        .queryString("select t from Test t order by id")
                        .entityManagerFactory(entityManagerFactory)
                        .name("retryReader")
                        .build())
                .build();
    }

    @Bean
    public ItemProcessor<Test, Test> retryProcessor() {
        return item -> {
            Thread.sleep(1000);
            item.setAge(3);
            return item;
        };
    }

    @Bean
    public AsyncItemProcessor<Test, Test> asyncItemProcessor() {
        AsyncItemProcessor<Test, Test> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(retryProcessor()); // 위임
        asyncItemProcessor.setTaskExecutor(multiThreadTaskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public JdbcBatchItemWriter<Test> retryWriter() {
        return new JdbcBatchItemWriterBuilder<Test>()
                .dataSource(dataSource)
                .sql("update test set age = :age where id = :id")
                .beanMapped()
                .build();
    }

    @Bean
    public AsyncItemWriter<Test> asyncItemWriter() {
        AsyncItemWriter<Test> asyncItemWriter = new AsyncItemWriter<>();

        asyncItemWriter.setDelegate(retryWriter()); // 위임

        return asyncItemWriter;
    }

    @Bean
    public TaskExecutor multiThreadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 몇개의 스레드를 관리할것인지?
        executor.setMaxPoolSize(8); // 4개의 스레드가 작업을 처리하고 있는데, 나머지 처리되지 않은 task가 있을때 얼마만큼의 최대 스레드를 생성할것인지?
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }


}
