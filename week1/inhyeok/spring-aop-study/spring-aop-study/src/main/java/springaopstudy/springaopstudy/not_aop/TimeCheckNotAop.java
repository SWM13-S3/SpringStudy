package springaopstudy.springaopstudy.not_aop;


import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Log4j2
public class TimeCheckNotAop {

    public Object calculateExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        // 해당 클래스 처리 전의 시간
        StopWatch sw = new StopWatch();
        sw.start();

        // 해당 클래스의 메소드 실행
        Object result = pjp.proceed();

        // 해당 클래스 처리 후의 시간
        sw.stop();
        long executionTime = sw.getTotalTimeMillis();

        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        String task = className + "." + methodName;

        log.debug("[ExecutionTime] " + task + "-->" + executionTime + "(ms)");

        return result;
    }
}
