import com.ibm.wala.classLoader.ShrikeBTMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;

import java.util.*;

public class Analysis {
    public static void Analysis(CallGraph graph, Tools tools) {
        // 遍历cg中所有的节点
        for (CGNode node : graph) {
            // node中包含了很多信息，包括类加载器、方法信息等，这里只筛选出需要的信息
            if (node.getMethod() instanceof ShrikeBTMethod) {
                // node.getMethod()返回一个比较泛化的IMethod实例，不能获取到我们想要的信息
                // 一般地，本项目中所有和业务逻辑相关的方法都是ShrikeBTMethod对象
                ShrikeBTMethod method = (ShrikeBTMethod) node.getMethod();
                // 使用Primordial类加载器加载的类都属于Java原生类，我们一般不关心。

                if ("Application".equals(method.getDeclaringClass().getClassLoader().toString())) {
                    // 获取声明该方法的类的内部表示
                    String classInnerName = method.getDeclaringClass().getName().toString();
                    tools.classMap.putIfAbsent(classInnerName, new HashSet<>());
                    tools.ClassMethodMap.putIfAbsent(classInnerName, new HashSet<>());
                    // 获取方法签名
                    String signature = method.getSignature();
                    String fullName = classInnerName + " " + signature;
                    // 存储信息
                    tools.methodMap.putIfAbsent(fullName, new HashSet<>());
                    tools.BT_methodMap.putIfAbsent(fullName, method);
                    tools.ClassMethodMap.get(classInnerName).add(classInnerName + " " + signature);
//                    System.out.println(classInnerName + " " + signature);

                    Iterator<CGNode> predNodes = graph.getPredNodes(node);
                    while (predNodes.hasNext()) {
                        CGNode preNode = predNodes.next();
                        if (preNode.getMethod() instanceof ShrikeBTMethod) {
                            ShrikeBTMethod preMethod = (ShrikeBTMethod) preNode.getMethod();
                            // 使用Primordial类加载器加载的类都属于Java原生类，我们一般不关心。

                            if ("Application".equals(preMethod.getDeclaringClass().getClassLoader().toString())) {
                                // 获取声明该方法的类的内部表示
                                String preClassName = preMethod.getDeclaringClass().getName().toString();
                                tools.classMap.get(classInnerName).add(preClassName);

                                // 获取方法签名
                                String preSignature = preMethod.getSignature();
                                tools.methodMap.get(fullName).add(preClassName + " " + preSignature);
//                                System.out.println(preClassName + " " + preSignature);
                            }
                        }
                    }
                }
            } else {
                System.out.println(String.format("'%s'不是一个ShrikeBTMethod：%s", node.getMethod(), node.getMethod().getClass()));
            }
        }
        // 移除set为空的项
        tools.classMap.entrySet().removeIf(setEntry -> setEntry.getValue().isEmpty());
        tools.methodMap.entrySet().removeIf(setEntry -> setEntry.getValue().isEmpty());
    }
}
