package javaagent.sample;

import javaagent.sample.transformer.IedisTransformer;
import javaagent.sample.transformer.InjectPrintTransformer;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;

/**
 * javaagent示例
 *
 * -javaagent:javaagent-samples-1.0.jar=iedis # 破解iedis插件
 * -javaagent:javaagent-samples-1.0.jar=injectPrint # 匹配所有返回值为String的方法，并输出其完整的方法名、入参、返回值
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello,World!");
    }

    public static void premain(String agentOps, Instrumentation inst) {

        PrintStream out = null;
        try {
            out = new PrintStream("system.out");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(out);
        System.setErr(out);

        System.out.println("--------agentOps-------->" + agentOps);

        if ("iedis".equals(agentOps)) {
            inst.addTransformer(new IedisTransformer());
        } else if ("injectPrint".equals(agentOps)) {
            inst.addTransformer(new InjectPrintTransformer());
        }

    }

}
