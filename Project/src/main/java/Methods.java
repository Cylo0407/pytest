import com.ibm.wala.types.annotations.Annotation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Methods {
    public static void creatDot(Tools tools) {

        try {
            BufferedWriter Cwriter = new BufferedWriter(new FileWriter("class.dot"));
            String classTitle = "digraph my_class {\n";
            Cwriter.write(classTitle);
            for (Map.Entry<String, Set<String>> entry : tools.classMap.entrySet()) {
                for (String str : entry.getValue()) {
                    String classLine = "   \"" + entry.getKey() + "\" -> \"" + str + "\";\n";
                    Cwriter.write(classLine);
                }
            }
            Cwriter.write("}");
            Cwriter.close();
            BufferedWriter Mwriter = new BufferedWriter(new FileWriter("method.dot"));
            String methodTitle = "digraph my_method {\n";
            Mwriter.write(methodTitle);
            for (Map.Entry<String, Set<String>> entry : tools.methodMap.entrySet()) {
                String key = entry.getKey().split(" ")[1];
                for (String str : entry.getValue()) {

                    String value = str.split(" ")[1];
                    String methodLine = "   \"" + key + "\" -> \"" + value + "\";\n";
                    Mwriter.write(methodLine);
                }
            }
            Mwriter.write("}");
            Mwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFiles(String target_path, List<File> fileList) {

        File dir = new File(target_path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                //是文件夹的话就是要递归再深入查找文件
                if (files[i].isDirectory()) {
                    loadFiles(files[i].getAbsolutePath(), fileList); // 获取文件绝对路径
                } else {
                    if (files[i].getName().endsWith(".class")) // 是否是.class文件
                        fileList.add(files[i]);
                }
            }
        }
    }

    public static void saveF(String method, Set<String> methods, Tools tools) {

        if (!tools.methodMap.containsKey(method)) return;

        Set<String> methodNames = tools.methodMap.get(method);
        for (String methodName : methodNames) {
            if (tools.methodMap.containsKey(methodName) && (!tools.tmpMethodMap.contains(methodName))) {
                    tools.tmpMethodMap.add(methodName);
                    saveF(methodName, methods, tools);
            } else {
                boolean flag = false;
                Collection<Annotation> annotations = tools.BT_methodMap.get(methodName).getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.getType().toString().equals("<Application,Lorg/junit/Test>")) {
                        flag = true;
                        break;
                    }
                }
                if (tools.BT_methodMap.get(methodName).isInit() || tools.BT_methodMap.get(methodName).isClinit())
                    flag = false;
                if (flag) methods.add(methodName);
            }
        }
    }
}
