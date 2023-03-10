package com.example.springbatch.batch.job;

import com.example.springbatch.batch.domain.Test;
import com.example.springbatch.batch.domain.TestVo;
import com.example.springbatch.batch.listner.TestJobListener;
import com.example.springbatch.batch.listner.TestStepListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.querydsl.reader.QuerydslNoOffsetPagingItemReader;
import org.springframework.batch.item.querydsl.reader.QuerydslPagingItemReader;
import org.springframework.batch.item.querydsl.reader.expression.Expression;
import org.springframework.batch.item.querydsl.reader.options.QuerydslNoOffsetNumberOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static com.example.springbatch.batch.domain.QTest.test;

@Configuration
@RequiredArgsConstructor
public class RetryJobTest {

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final TestStepListener testStepListener;

    @Value("${chunkSize:20}")
    private int chunkSize;

    @Bean
    public Job testJob2() {
        return jobBuilderFactory.get("testJob2")
                .start(retryStep())
                .build();
    }

    @Bean
    public Step retryStep() {
        return stepBuilderFactory.get("retryStep")
                .listener(testStepListener)
                .<Test, TestVo>chunk(chunkSize)
                .reader(querydslReader2())
                .processor(testProcessor())
                .writer(testWriter())
                .build();
    }


    @Bean
    public QuerydslPagingItemReader<Test> querydslReader() {
        return new QuerydslPagingItemReader<>(entityManagerFactory, chunkSize,
                queryFactory -> queryFactory.select(test)
                        .from(test)
                        .where(test.age.eq(1))
                        .orderBy(test.id.asc()));
    }


    @Bean
    public QuerydslNoOffsetPagingItemReader<Test> querydslReader2() {
        return new QuerydslNoOffsetPagingItemReader<>(entityManagerFactory, chunkSize,
                new QuerydslNoOffsetNumberOptions<>(test.id, Expression.ASC),
                queryFactory -> queryFactory.select(test)
                        .from(test)
                        .where(test.age.eq(1)));
    }


    @Bean
    public ItemProcessor<Test, TestVo> testProcessor() {
        return item -> { // item??? reader?????? ????????? ??????
            TestVo testVo = new TestVo();
            testVo.setId(item.getId());
            // writer??? ????????? ?????? return
            return testVo;
        };
    }

    @Bean
    public JdbcBatchItemWriter<TestVo> testWriter() {
        return new JdbcBatchItemWriterBuilder<TestVo>()
                .dataSource(dataSource)
                .sql("update test set ch = '?????????' where id in (:id)")
                .beanMapped()
                .build();
    }

    /* @Bean
    public TaskExecutor multiThreadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // ????????? ???????????? ???????????????????
        executor.setMaxPoolSize(8); // 4?????? ???????????? ????????? ???????????? ?????????, ????????? ???????????? ?????? task??? ????????? ??????????????? ?????? ???????????? ???????????????????
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }*/

    /*@Bean
    public SynchronizedItemStreamReader<Test> synchronizedItemReader() {
        return new SynchronizedItemStreamReaderBuilder<Test>()
                .delegate(new JpaPagingItemReaderBuilder<Test>()
                        .pageSize(chunkSize)
                        .queryString("select t from Test t order by id")
                        .entityManagerFactory(entityManagerFactory)
                        .name("retryReader")
                        .build())
                .build();
    }*/


    @Bean
    public JpaPagingItemReader<Test> testReader() {
        // reader ????????? jpa ??????
        return new JpaPagingItemReaderBuilder<Test>()
                .pageSize(chunkSize)
                .queryString("select t from Test t where t.age = 1 order by t.id")
                .entityManagerFactory(entityManagerFactory)
                .name("testReader")
                .build();
    }

    @Bean
    public ItemProcessor<Test, Test> jpaProcessor() {
        return item -> {
            item.setCh("?????????");
            return item;
        };
    }


    @Bean
    public JpaItemWriter<Test> jpaWriter() {
        return new JpaItemWriterBuilder<Test>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


}
