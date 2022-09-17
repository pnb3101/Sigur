package sigur;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SigurApplication {
    private static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext =
                new AnnotationConfigApplicationContext("sigur");

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
    }
}
