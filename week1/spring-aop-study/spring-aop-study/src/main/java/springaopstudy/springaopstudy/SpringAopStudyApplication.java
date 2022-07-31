package springaopstudy.springaopstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringAopStudyApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringAopStudyApplication.class, args);
	}

}
