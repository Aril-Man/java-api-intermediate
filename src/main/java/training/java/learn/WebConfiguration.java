package training.java.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import training.java.learn.resolver.UserArgResolver;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private UserArgResolver userArgResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(userArgResolver);
    }
}
