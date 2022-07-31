package springaopstudy.springaopstudy.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ExampleTestWithAop {
    public void excute() throws InterruptedException {
        Thread.sleep(100);
        System.out.println("excute Method는 실행되었습니다.");
    }
}
