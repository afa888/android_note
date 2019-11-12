package com.example.anno;


import com.example.annotion.BindPath;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import java.io.IOException;
import java.io.Writer;
import java.util.*;



@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {

    //生成文件的对象
    Filer filer;

    /**
     * 初始化
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }


    /**
     * 声明我们这个注解处理器要处理的注解是哪些
     *
     * @return 返回我们要处理的注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        /**
         * 拿到我们注解处理器名字
         * 1、getCanonicalName() 是获取所传类从java
         *  语言规范定义的格式输出。
         * 2、getName() 是返回实体类型名称
         * 3、getSimpleName() 返回从源代码中返回实例的名称。
         */
        types.add(BindPath.class.getCanonicalName());
        return types;

    }

    /**
     * 声明我们的注解处理器支持的java sdk版本号
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        /**
         * 拿到父类的processingEnv  获取getSourceVersion
         */
        return processingEnv.getSourceVersion();
    }

    /**
     * 这个方法 是注解处理器的核心方法  写文件就放在这里面进行写
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        /**
         *获取BindPath节点     通过这个Api能拿到所有这个
         *模块中所有的用到了BindPath注解的节点
         *比如我再LoginActivity这个类上用到了@BindPath注解
         * 获取到的 Set<? extends Element>集合就是被@BindPath注解后的类的所有节点
         */
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            //TypeElement这个是中java中的  在Android中没有
            TypeElement typeElement = (TypeElement) element;
            //这个获取的就是存放activity集合  map中的key
            String key = typeElement.getAnnotation(BindPath.class).value();
            String value = typeElement.getQualifiedName().toString();
            map.put(key, value);
        }
        //开始写文件
        if (map.size() > 0) {
            Writer writer = null;
            //创建类名  加上时间戳为了防止生成相同的类名
            String utilName = "ActivityUtils" + "Login";
            //创建源码文件
            try {
                JavaFileObject sourceFile = filer.createSourceFile("com.example.login." + utilName);
                writer = sourceFile.openWriter();
                /**
                 * 复制过来的  注意修改包名相同  ,类名替换成utilName
                 */
                writer.write("package com.example.login;\n" +
                        "\n" +
                        "import android.util.Log;\n" +
                        "import com.example.arouterlibrary.Arout.Arouter;\n" +
                        "import com.example.arouterlibrary.Arout.IArouter;\n" +
                        "import com.example.login.LoginActivity;\n" +
                        "\n" +
                        "/**\n" +
                        " * Author：yjn\n" +
                        " * Date：2019-11-16\n" +
                        " * Description：1\n" +
                        " *\n" +
                        " */\n" +
                        "public class " + utilName + " implements IArouter {\n" +
                        "    private static final String TAG = \"ActivityUtils\";\n" +
                        "    @Override\n" +
                        "    public void putActivity() {\n" +
                        "        Log.e(TAG, \"putActivity: \" );");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = map.get(key);
                    writer.write(" Arouter.getInstance().putActivity(\"" + key + "\", " + value + ".class);\n");
                }

                writer.write("    }\n" +
                        "}\n");


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
