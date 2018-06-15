package javaagent.samples.sample2;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 使用javaagent结合javassist增强方法，在javaagent.samples.sample2包下的方法中插入计算方法执行耗时的代码
 */
public class PerformMonitorTransformer implements ClassFileTransformer {

    private static final String PACKAGE_PREFIX = "javaagent.samples.sample2";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            String currentClassName = className.replaceAll("/", ".");

            // 仅仅提升这个包中的类
            if (!currentClassName.startsWith(PACKAGE_PREFIX)) {
                return null;
            }

            System.out.println("now transform: [" + currentClassName + "]");

            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
            CtBehavior[] methods = ctClass.getDeclaredBehaviors();
            for (CtBehavior method : methods) {
                // 提升方法
                enhanceMethod(method);
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 提升方法
     * @param method
     * @throws Exception
     */
    private void enhanceMethod(CtBehavior method) throws Exception {
        if (method.isEmpty()) {
            return;
        }
        final String methodName = method.getName();
        // 不提升main方法
        if (methodName.equalsIgnoreCase("main")) {
            return;
        }

        ExprEditor editor = new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                methodCall.replace(genSource(methodName));
            }
        };

        method.instrument(editor);
    }

    /**
     * 方法增强代码
     *
     * javassist的核心关键之处
     *
     * $0	方法调用的目标对象。它不等于 this，它代表了调用者。 如果方法是静态的，则 $0 为 null
     * $1, $2 ..	方法的参数
     * $args	方法参数数组.它的类型为 Object[]
     * $_	返回值
     * $$	所有实参。例如, m($$) 等价于 m($1,$2,...)
     * $r	返回结果的类型，用于强制类型转换
     * $w	包装器类型，用于强制类型转换
     * $cflow(...)	cflow 变量
     * $class	一个 java.lang.Class 对象，表示当前正在修改的类
     * $sig	类型为 java.lang.Class 的参数类型数组
     * $type	一个 java.lang.Class 对象，表示返回值类型
     * $class	一个 java.lang.Class 对象，表示当前正在修改的类
     * $proceed	调用表达式中方法的名称
     *
     * @param methodName
     * @return
     */
    private String genSource(String methodName) {
        StringBuilder source = new StringBuilder();
        source.append("{")
                .append("long start = System.nanoTime();\n") // 前置增强: 加入时间戳
                .append("$_ = $proceed($$);\n") // 保留原有的代码处理逻辑
                .append("System.out.print(\"method:[" + methodName + "]\");").append("\n")
                .append("System.out.println(\" cost:[\" +(System.nanoTime() -start)+ \"ns]\");") // 后置增强
                .append("}");
        return source.toString();
    }

}
