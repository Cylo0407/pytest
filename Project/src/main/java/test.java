import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;

import java.io.*;

public class test {
    public static void main(String[] args) throws CancelException, ClassHierarchyException, InvalidClassFileException, IOException {

        String command = args[0];
        String project_target = args[1];
        String change_info = args[2];
        Tools tools = new Tools(command, project_target, change_info); // 处理输入

        CallGraph graph = MakeGraph.getGraph(tools); // 构建依赖图
        Analysis.Analysis(graph, tools);// 分析节点
        //Methods.creatDot(tools);
        Select.selection(tools); // 变更选择
    }
}