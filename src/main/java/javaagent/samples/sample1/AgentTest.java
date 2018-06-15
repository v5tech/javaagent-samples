package javaagent.samples.sample1;

public class AgentTest {

    // add VM options: -javaagent:javaagent-samples-1.0.jar=agent1,agent2
    public static void main(String[] args) {
        System.out.println("Hello,Agent!");
    }

}
