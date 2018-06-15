package javaagent.samples.sample2;

/**
 * add VM options: -javaagent:javaagent-samples-1.0.jar
 */
public class AgentTest {

    private void fun1() {
        System.out.println("this is fun 1.");
    }

    private void fun2() {
        System.out.println("this is fun 2.");
    }

    public static void main(String[] args) {

        AgentTest test = new AgentTest();
        test.fun1();
        test.fun2();

        System.out.println("------------------------------------");

        Another another = new Another();
        another.fun3();
        another.fun4();

    }

}
