package codesmell.entity;

public class Variable {

    public String getvarName() {
        return variableName;
    }

    private String variableName;

    public boolean isUsedInOnSaveInstanceState() {
        return isUsedInOnSaveInstanceState;
    }

    public void setUsedInOnSaveInstanceState(boolean usedInOnSaveInstanceState) {
        isUsedInOnSaveInstanceState = usedInOnSaveInstanceState;
    }

    private boolean isUsedInOnSaveInstanceState;

    public String getVariableName() {
        return variableName;
    }

    public String getType() {
        return type;
    }

    private String type;

    public Variable(String Name, String Type) {
        this.variableName = Name;
        this.type = Type;
        this.isUsedInOnSaveInstanceState = false;
    }




}
