package codesmell.entity;

import java.util.HashMap;
import java.util.Map;

public class XML extends SmellyElement {

    private String XMLname;
    private boolean hasSmell;
    private Map<String, String> data;

    public XML(String XMLname) {
        this.XMLname = XMLname;
        data = new HashMap<>();
    }

    public void setHasSmell(boolean hasSmell) {
        this.hasSmell = hasSmell;
    }

    public void addDataItem(String name, String value) {
        data.put(name, value);
    }

    @Override
    public String getElementName() {
        return XMLname;
    }

    @Override
    public boolean getHasSmell() {
        return hasSmell;
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }
}
