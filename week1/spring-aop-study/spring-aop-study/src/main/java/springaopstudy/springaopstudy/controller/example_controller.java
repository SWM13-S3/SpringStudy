package springaopstudy.springaopstudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springaopstudy.springaopstudy.aop.ExampleTestWithAop;
import springaopstudy.springaopstudy.not_aop.ExampleTestNotAop;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class example_controller {

    private final ExampleTestWithAop exampleTestWithAop;
    private final ExampleTestNotAop exampleTestNotAop;

    @GetMapping("/aop")
    public String hello() throws InterruptedException {
        exampleTestWithAop.excute();
        return "ok";
    }

    @GetMapping("/notaop")
    public String hello2() throws InterruptedException {
        exampleTestNotAop.excute();
        return "ok";
    }
}
