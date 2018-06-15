package javassist.iedis.crack;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * 破解思路二
 *
 * http://liaojiacan.me/2017/10/12/Iedis破解思路/index.html
 *
 * 简单粗暴的做法。最后用生成的类替换原jar包中的类文件
 *
 * jar -uvf iedis-2.43.jar com/
 *
 */
public class IedisCrack2 {

    public static void main(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();

        pool.insertClassPath("C:\\Users\\Administrator\\Desktop\\iedis-2.43.jar");

        CtClass clazz = pool.get("com.seventh7.widget.iedis.a.p");

        try {

            CtMethod[] mds = clazz.getDeclaredMethods();
            for(CtMethod method : mds){
                if(method.getLongName().startsWith("com.seventh7.widget.iedis.a.p.f")){
                    System.out.println("Inject :: SUCCESS!");
                    try {
                        method.insertBefore("if(true){return true;} ");
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
            clazz.writeFile("/tmp");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
