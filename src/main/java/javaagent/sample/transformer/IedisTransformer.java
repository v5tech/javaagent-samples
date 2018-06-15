package javaagent.sample.transformer;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 采用javaagent的方式破解iedis
 *
 * 在idea.exe.vmoptions或idea64.exe.vmoptions中添加-javaagent:javaagent-samples-1.0.jar=iedis
 *
 */
public class IedisTransformer implements ClassFileTransformer {

    private final static String IDEA_LIB="C:/Program Files/JetBrains/IntelliJ IDEA 2017.2.3/lib/*";
    private final static String IDEIS_LIB="C:/Users/Administrator/.IntelliJIdea2017.2/config/plugins/Iedis/lib/*";

    public IedisTransformer() {
        try {
            ClassPool.getDefault().appendClassPath(IDEA_LIB);
            ClassPool.getDefault().appendClassPath(IDEIS_LIB);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        /**
         * 只处理com/seventh7/widget/iedis
         */
        if(className.startsWith("com/seventh7/widget/iedis")){
            try {
                CtClass clazz = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod[] methods = clazz.getDeclaredMethods();
                CtClass string = ClassPool.getDefault().getCtClass(String.class.getName());
                for(CtMethod method :methods){

                    /**
                     * 采用javaagent的方式破解iedis
                     *
                     * 其核心思路为采用javaagent结合javassist动态修改字节码（此处主要是修改com.seventh7.widget.iedis.a.p类中的f方法返回值为true，从而绕过验证）
                     *
                     */
                    if(method.getLongName().startsWith("com.seventh7.widget.iedis.a.p.f")){
                        System.out.println("Inject :: SUCCESS!");
                        method.insertBefore("if(true){return true;} ");
                        continue;
                    }

                    /**
                     * 采用javassist在方法返回值为String的方法前插入以下代码，用来输出其完整的方法名（包名+类名+方法名）、方法参数列表、方法返回值信息等
                     */
                    if(method.getReturnType().equals(string)){
                        String name = method.getLongName();
                        method.insertAfter("System.out.println(\"--------------------\");" +
                                " System.out.println(\""+name+"\"); " +
                                " System.out.println(java.util.Arrays.toString($args)); " +
                                " System.out.println(\"return:\"+$_);");
                    }
                }

                return clazz.toBytecode();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
