package springaopstudy.springaopstudy.javaexample;

abstract class ex {
    public void proecss(){
        System.out.println("hello");
    }
    public abstract void pro2();
}

class ex2 extends ex {
    @Override
    public void pro2() {

    }
}
