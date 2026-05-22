package com.commons.swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.UiConfiguration;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.builders.ApiInfoBuilder;
/**
 * Created by songdh on 2018/9/20.
 */
@Configuration 
//@EnableSwagger2 
@EnableWebMvc 
public class SwaggerConfiguration  {
//    @Bean
//    public Docket createRestApi(){
//    	System.out.println("Swagger 目录生成启动======================");
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("aiweb API")
//        		.apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }

//    private ApiInfo apiInfo(){
//        return new ApiInfoBuilder()
//                .title("aiweb测试项目接口规范手册")
//                .description("测试")
//                .contact(new Contact("Mr.Song","www.google.com","*****@qq.com"))
//                .version("1.0")
//                .termsOfServiceUrl("http://aiweb/*")
//                .description("song测试手册")
//                .license("Apach")
//                .build();
//    }
//    @Bean
//    public UiConfiguration getUiConfig() {
//        return new UiConfiguration(
//                null,// url,暂不用
//                "none",       // docExpansion          => none | list
//                "alpha",      // apiSorter             => alpha
//                "schema",     // defaultModelRendering => schema
//                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
//                false,        // enableJsonEditor      => true | false
//                true);        // showRequestHeaders    => true | false
//    }
}
