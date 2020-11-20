import com.ibm.wala.classLoader.Language;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.impl.AllApplicationEntrypoints;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MakeGraph {
    public static CallGraph getGraph(Tools tools) throws IOException, InvalidClassFileException, ClassHierarchyException, CancelException {

        File exclusionFile = new FileProvider().getFile("exclusion.txt"); // 包含排除JAVA原生类的文件

        //构建AnalysisScope
        AnalysisScope scope =
                AnalysisScopeReader.readJavaScope(
                        "scope.txt", /*Path to scope file*/
                        exclusionFile, /*Path to exclusion file*/
                        ClassLoader.getSystemClassLoader());

        List<File> fileList = new ArrayList<>();
        Methods.loadFiles(tools.getProject_target(), fileList);

        for (File file : fileList) scope.addClassFileToScope(ClassLoaderReference.Application, file);

        // 使用0-CFA算法
        ClassHierarchy cha = ClassHierarchyFactory.makeWithRoot(scope);
        AllApplicationEntrypoints entrypoints = new AllApplicationEntrypoints(scope, cha);
        AnalysisOptions option = new AnalysisOptions(scope, entrypoints);
        SSAPropagationCallGraphBuilder builder = Util.makeZeroCFABuilder( Language.JAVA, option, new AnalysisCacheImpl(), cha, scope );
        return builder.makeCallGraph(option);
    }
}
