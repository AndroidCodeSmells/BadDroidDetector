package codesmell.entity;


import java.util.ArrayList;
import java.util.List;

public class MethodChild  {

    public String getMethodName() {
        return methodName;
    }

    private String methodName;
    private List<String> childs;

    public MethodChild(String methodName) {
        this.methodName = methodName;
        childs = new ArrayList<String>();
    }

    public void addCallMethod(String name) {
        childs.add(name);
    }

    public boolean has(String callNmae){
        if (childs.contains(callNmae)) {
            return true;
        }
        return false;
    }

}
