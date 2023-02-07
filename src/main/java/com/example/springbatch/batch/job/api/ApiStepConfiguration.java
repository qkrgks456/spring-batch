package com.example.springbatch.batch.job.api;

import com.example.springbatch.batch.domain.ApiRequestVo;
import com.example.springbatch.batch.domain.ApiResponseVo;
import com.example.springbatch.batch.domain.Product;
import com.example.springbatch.batch.domain.ProductVo;
import com.example.springbatch.repository.ProductRepository;
import com.example.springbatch.service.api.ApiService1;
import com.example.springbatch.service.api.ApiService2;
import com.example.springbatch.service.api.ApiService3;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final ProductRepository productRepository;
    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;

    private final int chunkSize = 10;

    @Bean
    @Qualifier("apiMasterStep")
    public Step apiMasterStep() {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");
        return taskExecutor;
    }

    @Bean
    public Step apiSlaveStep() {
        return stepBuilderFactory.get("apiSlaveStep")
                .<Product, ApiRequestVo>chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Product> itemReader(@Value("#{stepExecutionContext['product']}") ProductVo productVo) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", productVo.getType());
        return new JpaPagingItemReaderBuilder<Product>()
                .pageSize(chunkSize)
                .parameterValues(map)
                .queryString("select p from Product p where type = :type order by id")
                .entityManagerFactory(entityManagerFactory)
                .name("jpaItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<Product, ApiRequestVo> itemProcessor() {
        Map<String, ItemProcessor<Product, ApiRequestVo>> processorMap = new HashMap<>();
        processorMap.put("1", item -> ApiRequestVo.builder()
                .id(item.getId())
                .productVo(new ProductVo(item))
                .build());
        processorMap.put("2", item -> ApiRequestVo.builder()
                .id(item.getId())
                .productVo(new ProductVo(item))
                .build());
        processorMap.put("3", item -> ApiRequestVo.builder()
                .id(item.getId())
                .productVo(new ProductVo(item))
                .build());
        Classifier<Product, ItemProcessor<?, ? extends ApiRequestVo>> classifier = classifiable -> processorMap.get(classifiable.getType());
        return new ClassifierCompositeItemProcessorBuilder<Product, ApiRequestVo>()
                .classifier(classifier)
                .build();
    }

    @Bean
    public ItemWriter itemWriter() {
        Map<String, ItemWriter<? super ApiRequestVo>> processorMap = new HashMap<>();
        processorMap.put("1", item -> {
            ApiResponseVo responseVo = apiService1.service(item);
            System.out.println("responseVo = " + responseVo);
        });
        processorMap.put("2", item -> {
            ApiResponseVo responseVo = apiService2.service(item);
            System.out.println("responseVo = " + responseVo);
        });
        processorMap.put("3", item -> {
            ApiResponseVo responseVo = apiService3.service(item);
            System.out.println("responseVo = " + responseVo);
        });

        Classifier<ApiRequestVo, ItemWriter<? super ApiRequestVo>> classifier =
                classifiable -> processorMap.get(classifiable.getProductVo().getType());
        return new ClassifierCompositeItemWriterBuilder<ApiRequestVo>()
                .classifier(classifier)
                .build();
    }


    @Bean
    public Partitioner partitioner() {
        return gridSize -> {
            Map<String, ExecutionContext> map = new HashMap<>();
            List<ProductVo> list = productRepository.findTypeName()
                    .stream()
                    .map(ProductVo::new)
                    .collect(Collectors.toList());
            int n = 0;
            for (ProductVo productVo : list) {
                ExecutionContext value = new ExecutionContext();
                value.put("product", productVo);
                map.put("partition" + n, value);
                n++;
            }
            return map;
        };
    }


}
