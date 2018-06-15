package javassist.iedis.crack;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * 破解思路一
 *
 * https://www.awei.org/2017/11/19/idea-iedis-plugin-2-41-po-jie-fang-fa/
 *
 * 最后将tmp目录下生成的class文件打包替换iedis-2.43.jar
 *
 * jar -uvf iedis-2.43.jar com/
 *
 */
public class IedisCrack1 {

    public static void main(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();

        pool.insertClassPath("C:\\Users\\Administrator\\Desktop\\iedis-2.43.jar");

        CtClass L = pool.get("com.seventh7.widget.iedis.L");
        CtClass p = pool.get("com.seventh7.widget.iedis.a.p");

        try {

            CtMethod ct = L.getDeclaredMethod("a", new CtClass[]{pool.get("java.lang.String")});
            ct.setBody("{ return \"{\\\"trailing\\\": false,\\\"daysLeft\\\": 2147483647,\\\"popup\\\": false,\\\"activated\\\": true}\"; }");

            ct = p.getDeclaredMethod("f", new CtClass[]{});
            ct.setBody("{ this.d();\n" +
                    " this.a(2147483647, false);\n" +
                    " return false; }");

            L.writeFile("/tmp");
            p.writeFile("/tmp");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
