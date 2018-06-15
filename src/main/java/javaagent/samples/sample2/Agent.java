package javaagent.samples.sample2;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * 使用javaagent结合javassist增强方法，在net.ameizi.samples2包下的方法中插入计算方法执行耗时的代码
 */

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("this is an perform monitor agent.");
        // 添加 Transformer
        ClassFileTransformer transformer = new PerformMonitorTransformer();
        inst.addTransformer(transformer);
    }

}
