package javaagent.samples.sample1;

import java.lang.instrument.Instrumentation;

/**
 *
 * https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html
 *
 * public static void premain(String agentArgs, Instrumentation inst);  //①
 * public static void premain(String agentArgs);  //②
 *
 * 注：①和②同时存在时，①会优先被执行，而②则会被忽略。
 */

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("---------public static void premain(String agentArgs, Instrumentation inst)--------->" + agentArgs);
    }

    public static void premain(String agentArgs) {
        System.out.println("---------public static void premain(String agentArgs)--------->" + agentArgs);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("---------public static void agentmain(String agentArgs, Instrumentation inst)--------->" + agentArgs);
    }

    public static void agentmain(String agentArgs) {
        System.out.println("---------public static void agentmain(String agentArgs)--------->" + agentArgs);
    }


}
