# JavaAgent

示例说明

①、 `javassist.iedis.crack.IedisCrack1`和`javassist.iedis.crack.IedisCrack2`为使用`javassist`破解`iedis`插件的代码.

最后将`tmp`目录下生成的`class`文件打包替换`iedis-2.43.jar`其操作命令如下:

```bash
jar -uvf iedis-2.43.jar com/
```

②、`javaagent.sample.App`演示了`javaagent`结合`javassist`案例

`javaagent.sample.transformer.IedisTransformer` 演示了使用`javaagent`结合`javassist`破解`iedis`插件

`javaagent.sample.transformer.InjectPrintTransformer` 演示了使用`javaagent`结合`javassist`匹配返回值为`String`的方法，在其方法内插入代码，输出方法名、方法参数列表、方法返回值信息等。

使用`javaagent`的方式破解`iedis`插件须知:

确保`src/main/resources/META-INF/MANIFEST.MF`文件内容如下所示:

```
Manifest-Version: 1.0
Premain-Class: javaagent.sample.App
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Can-Set-Native-Method-Prefix: true

```

最后执行`mvn assembly:assembly`打包，然后在`idea.exe.vmoptions`或`idea64.exe.vmoptions`中添加`-javaagent:javaagent-samples-1.0.jar=iedis`重启`idea`使其生效。

③、`javaagent.samples.sample1.Agent`演示了`javaagent`的基本用法，其内部声明了如下的两个方法:

```java
public static void premain(String agentArgs, Instrumentation inst);  // a
public static void premain(String agentArgs);  // b
```

a和b同时存在时，a会优先被执行，而b则会被忽略。具体参照 https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html 

`javaagent.samples.sample1.AgentTest` 运行启动类，在ide中运行时需添加jvm参数

add VM options:

`-javaagent:javaagent-samples-1.0.jar=agent1,agent2`


确保`src/main/resources/META-INF/MANIFEST.MF`文件内容如下所示:

```
Manifest-Version: 1.0
Premain-Class: javaagent.samples.sample1.Agent
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Can-Set-Native-Method-Prefix: true

```

最后执行`mvn assembly:assembly`打包，在`ide`中运行`javaagent.samples.sample1.AgentTest`观察控制台输出，其输出大致如下所示：

```
---------public static void premain(String agentArgs, Instrumentation inst)--------->agent1,agent2
Hello,Agent!
```


④、`javaagent.samples.sample2.Agent`演示了使用`javaagent`结合`javassist`增强方法，在`javaagent.samples.sample2`包下的方法中插入计算方法执行耗时的代码

其中`javaagent.samples.sample2.PerformMonitorTransformer`为核心业务代码，请务必理解该部分代码.

`javaagent.samples.sample2.AgentTest`为运行启动类，在ide中运行时需添加jvm参数
                                          
add VM options:

`-javaagent:javaagent-samples-1.0.jar`


确保`src/main/resources/META-INF/MANIFEST.MF`文件内容如下所示:

```
Manifest-Version: 1.0
Premain-Class: javaagent.samples.sample2.Agent
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Can-Set-Native-Method-Prefix: true

```

最后执行`mvn assembly:assembly`打包，在`ide`中运行`javaagent.samples.sample2.AgentTest`观察控制台输出，其输出大致如下所示：

```
this is an perform monitor agent.
now transform: [javaagent.samples.sample2.AgentTest]
this is fun 1.
method:[fun1] cost:[77399ns]
this is fun 2.
method:[fun2] cost:[18123ns]
------------------------------------
now transform: [javaagent.samples.sample2.Another]
this is fun 3.
method:[fun3] cost:[58521ns]
this is fun 4.
method:[fun4] cost:[18123ns]
```

⑤、`javaagent.samples.sample3.Agent`演示了使用`javaagent`结合`bytebuddy`增强方法，拦截`javaagent.samples.sample3.example`包下的方法，统计其运行所花费的时间

`javaagent.samples.sample3.RunTimeInterceptor`为自定义的`bytebuddy`拦截器，务必理解`javaagent.samples.sample3.Agent`和`javaagent.samples.sample3.RunTimeInterceptor`中的代码逻辑

`javaagent.samples.sample3.example.AgentTest`为运行启动类，在ide中运行时需添加jvm参数

add VM options:

`-javaagent:javaagent-samples-1.0.jar`


确保`src/main/resources/META-INF/MANIFEST.MF`文件内容如下所示:

```
Manifest-Version: 1.0
Premain-Class: javaagent.samples.sample3.Agent
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Can-Set-Native-Method-Prefix: true

```

最后执行`mvn assembly:assembly`打包，在`ide`中运行`javaagent.samples.sample3.example.AgentTest`观察控制台输出，其输出大致如下所示：

```
this is an perform monitor agent.

this is fun 1.
private void javaagent.samples.sample3.example.AgentTest.fun1() throws java.lang.Exception: took 500ms


this is fun 2.
private void javaagent.samples.sample3.example.AgentTest.fun2() throws java.lang.Exception: took 1000ms


public static void javaagent.samples.sample3.example.AgentTest.main(java.lang.String[]) throws java.lang.Exception: took 1500ms

```

⑥、`javaagent.samples.sample4.Agent`演示了使用`javaagent`每隔`5000`毫秒输出一次程序内存和GC情况

`javaagent.samples.sample4.Metric`里面是程序运行期间jvm内存和gc的业务逻辑代码

`javaagent.samples.sample4.AgentTest`为运行启动类，在ide中运行时需添加jvm参数

add VM options:

`-javaagent:javaagent-samples-1.0.jar`


确保`src/main/resources/META-INF/MANIFEST.MF`文件内容如下所示:

```
Manifest-Version: 1.0
Premain-Class: javaagent.samples.sample4.Agent
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Can-Set-Native-Method-Prefix: true

```

最后执行`mvn assembly:assembly`打包，在`ide`中运行`javaagent.samples.sample4.AgentTest`观察控制台输出，其输出大致如下所示：

```
this is an perform monitor agent.


init: 256MB	 max: 3621MB	 used: 6MB	 committed: 245MB	 use rate: 2%
init: 2MB	 max: 0MB	 used: 6MB	 committed: 8MB	 use rate: 82%

name: PS Scavenge	 count:0	 took:0	 pool name:[PS Eden Space, PS Survivor Space]
name: PS MarkSweep	 count:0	 took:0	 pool name:[PS Eden Space, PS Survivor Space, PS Old Gen]

init: 256MB	 max: 3621MB	 used: 148MB	 committed: 991MB	 use rate: 14%
init: 2MB	 max: 0MB	 used: 7MB	 committed: 8MB	 use rate: 83%

name: PS Scavenge	 count:10	 took:96	 pool name:[PS Eden Space, PS Survivor Space]
name: PS MarkSweep	 count:0	 took:0	 pool name:[PS Eden Space, PS Survivor Space, PS Old Gen]

init: 256MB	 max: 3621MB	 used: 837MB	 committed: 1483MB	 use rate: 56%
init: 2MB	 max: 0MB	 used: 7MB	 committed: 8MB	 use rate: 84%

name: PS Scavenge	 count:12	 took:98	 pool name:[PS Eden Space, PS Survivor Space]
name: PS MarkSweep	 count:0	 took:0	 pool name:[PS Eden Space, PS Survivor Space, PS Old Gen]

init: 256MB	 max: 3621MB	 used: 592MB	 committed: 1360MB	 use rate: 43%
init: 2MB	 max: 0MB	 used: 7MB	 committed: 8MB	 use rate: 84%

name: PS Scavenge	 count:14	 took:99	 pool name:[PS Eden Space, PS Survivor Space]
name: PS MarkSweep	 count:0	 took:0	 pool name:[PS Eden Space, PS Survivor Space, PS Old Gen]
```

# 参考文档


* JavaAgent


https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html


https://zeroturnaround.com/rebellabs/how-to-inspect-classes-in-your-jvm


* javassist


https://jboss-javassist.github.io/javassist/tutorial/tutorial.html


https://jboss-javassist.github.io/javassist/tutorial/tutorial2.html


http://zhxing.iteye.com/blog/1703305


* bytebuddy


http://bytebuddy.net


https://notes.diguage.com/byte-buddy-tutorial

