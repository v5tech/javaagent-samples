package javaagent.sample.transformer;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 匹配返回值为String的方法，在其方法内插入代码，输出方法名、方法参数列表、方法返回值信息等
 */
public class InjectPrintTransformer implements ClassFileTransformer {


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            CtClass clazz = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classfileBuffer));
            CtMethod[] mds = clazz.getMethods();
            CtClass stringClass = ClassPool.getDefault().getCtClass(String.class.getName());
            for(CtMethod md:mds){
                if(md.getReturnType().equals(stringClass)){
                    String name = md.getLongName();
                    md.insertAfter("System.out.println(\""+name+"\"); \n " +
                            "System.out.println(java.util.Arrays.toString($args)); \n " +
                            "System.out.println(\"return:\"+$_);");
                }
            }
            return  clazz.toBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return null;
    }
}
