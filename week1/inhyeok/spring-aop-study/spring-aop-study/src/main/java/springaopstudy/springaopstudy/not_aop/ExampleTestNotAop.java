package springaopstudy.springaopstudy.not_aop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExampleTestNotAop {

    private final TimeCheckNotAopVer2 timeCheckNotAopVer2;

    public void excute() throws InterruptedException {
        timeCheckNotAopVer2.BeforeCalculateExecutionTime();
        Thread.sleep(1000);
        timeCheckNotAopVer2.AfterCalculateExecutionTime(this);
        System.out.println("메소드가 호출되었습니다.");
    }
}
