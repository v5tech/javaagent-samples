package javaagent.samples.sample3.example;

public class AgentTest {

    private void fun1() throws Exception {
        System.out.println("this is fun 1.");
        Thread.sleep(500);
    }

    private void fun2() throws Exception {
        System.out.println("this is fun 2.");
        Thread.sleep(1000);
    }

    // add VM options: -javaagent:javaagent-samples-1.0.jar
    public static void main(String[] args) throws Exception {

        AgentTest test = new AgentTest();
        test.fun1();
        test.fun2();

    }

}
