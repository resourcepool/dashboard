package io.resourcepool.dashboard;


import io.resourcepool.dashboard.security.interceptor.CorsInterceptor;
import com.google.common.base.Predicates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

/**
 * Configuration SpringBoot.
 */
@SpringBootApplication
@EnableSwagger2
@EnableAspectJAutoProxy
@EnableConfigurationProperties
public class DashboardApplication extends WebMvcConfigurerAdapter {

    /**
     * Entry point.
     *
     * @param args the command args
     */
    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }

    /**
     * @return Swagger configuration class.
     */
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.resourcepool.dashboard.rest"))
                .paths(Predicates.not(PathSelectors.regex("/")))
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * @return Return a filesystem for Daos.
     */
    @Bean
    public FileSystem fileSystem() {
        return FileSystems.getDefault();
    }

    /**
     * CorsInterceptor factory.
     *
     * @return CorsInterceptor
     */
    @Bean
    public CorsInterceptor corsInterceptor() {
        return new CorsInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor());
    }


    /**
     * Builds the API information for the Swagger library.
     *
     * @return the API Information
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Dashboard REST Api Documentations with Swagger")
                .description("Dashboard REST Api Documentations with Swagger")
                .contact("Groupe Excilys")
                .version("1.0")
                .build();
    }

    /**
     * Set resource directory for media file
     *
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**").addResourceLocations("file:public/");
    }

}