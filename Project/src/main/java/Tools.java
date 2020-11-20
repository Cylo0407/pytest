import com.ibm.wala.classLoader.ShrikeBTMethod;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Tools {
    private String command;
    private String project_target;
    private String change_info;
    public Map<String, Set<String>> classMap = new HashMap<>();
    public Map<String, Set<String>> methodMap = new HashMap<>();
    public Map<String, Set<String>> ClassMethodMap = new HashMap<>(); // class-method对
    public Map<String, ShrikeBTMethod> BT_methodMap = new HashMap<>();
    public Set<String> tmpMethodMap = new HashSet<>();

    public Tools(String command, String project_target, String change_info) {
        this.command = command;
        this.project_target = project_target;
        this.change_info = change_info;
    }

    public String getCommand() { return this.command; }
    public String getProject_target() { return this.project_target; }
    public String getChange_info() { return this.change_info; }
}
