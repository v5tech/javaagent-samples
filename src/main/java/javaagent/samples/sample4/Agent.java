package javaagent.samples.sample4;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("this is an perform monitor agent.\n");

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Metric.printMemoryInfo();
            Metric.printGCInfo();
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

}
