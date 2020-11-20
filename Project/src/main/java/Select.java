import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Select {
    public static void selection(Tools tools) {
        String command = tools.getCommand();

        if (command.equals("-c")) exec_class_select(tools);
        else if (command.equals("-m")) exec_Method_select(tools);
    }

    public static void exec_class_select(Tools tools) {
        Set<String> changedClass = new HashSet<>(); // 存放与修改有关的类

        try { // 读取变更信息
            BufferedReader bf = new BufferedReader(new FileReader(tools.getChange_info()));
            String line;
            // 存放与修改有关的方法
            Set<String> changedMethod = new HashSet<>();
            while ((line = bf.readLine()) != null) {
                String[] strings = line.split(" ");
                changedClass.add(strings[0]);
                changedMethod.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 类选择
        try {
            BufferedWriter Cwriter = new BufferedWriter(new FileWriter("./selection-class.txt"));
            Set<String> methods = new HashSet<>();
            tools.tmpMethodMap = new HashSet<>();
            for (String className : changedClass) {
                for (String classMethod : tools.ClassMethodMap.get(className)) {
                    Methods.saveF(classMethod, methods, tools);
                }
            }
            for (String method : methods) {
                Cwriter.write(method + "\n");
            }
            Cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exec_Method_select(Tools tools) {
        Set<String> changedMethod = new HashSet<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(tools.getChange_info()));
            String str;
            while ((str = in.readLine()) != null) {
                changedMethod.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 方法选择
        try {

            BufferedWriter Mwriter = new BufferedWriter(new FileWriter("./selection-method.txt"));
            Set<String> methods = new HashSet<>();
            tools.tmpMethodMap = new HashSet<>();
            for (String methodName : changedMethod) {
                Methods.saveF(methodName, methods, tools);
            }
            for (String method : methods) {
                Mwriter.write(method + "\n");
            }
            Mwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
