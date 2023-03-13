package me.forum.Config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class EncodingPostProcessor implements BeanPostProcessor {
	@Override
    public Object postProcessBeforeInitialization(Object bean, String name)
            throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter) {
            List<HttpMessageConverter<?>> convs = ((RequestMappingHandlerAdapter) bean).getMessageConverters();
            for (HttpMessageConverter<?> conv: convs) {
                if (conv instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) conv).setSupportedMediaTypes(
                        Arrays.asList(new MediaType("text", "html", 
                            StandardCharsets.UTF_8)));
                }
            }
        }
        return bean;
    }
	
	@Override
    public Object postProcessAfterInitialization(Object bean, String name)
            throws BeansException {
        return bean;
    }
}
