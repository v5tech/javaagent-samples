package javaagent.samples.sample4;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * add VM options: -javaagent:javaagent-samples-1.0.jar
 */
public class AgentTest {

    public static void main(String[] args) throws Exception {
        boolean is = true;
        while (is) {
            List<Object> list = new ArrayList<>();
            list.add(UUID.randomUUID());
        }
    }

}
