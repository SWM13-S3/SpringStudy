package springaopstudy.springaopstudy.not_aop;


import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Log4j2
public class TimeCheckNotAopVer2 {

    StopWatch sw = new StopWatch();

    public void BeforeCalculateExecutionTime(){
        // 해당 클래스 처리 전의 시간
        sw.start();
    }

    public void AfterCalculateExecutionTime(Object mainLogic){
        sw.stop();
        long executionTime = sw.getTotalTimeMillis();

        ExampleTestNotAop targetLogic = (ExampleTestNotAop) mainLogic;//원래는 여기에 어뎁터 패턴을 사용해야 더 확장성 있어지지만 생략한다.
        String className = targetLogic.getClass().getName();
        String task = className + ".excute";

        System.out.println("[ExecutionTime] " + task + "-->" + executionTime + "(ms)");
    }
}
