package codesmell;

import java.util.ArrayList;
import java.util.List;

public class TestFile {
    private String app, productionFilePath;
    private List<AbstractSmell> codeSmells;

    public String getApp() {
        return app;
    }

    public String getProductionFilePath() {
        return productionFilePath;
    }


    public List<AbstractSmell> getCodeSmells() {
        return codeSmells;
    }

    public boolean getHasProductionFile() {
        return ((productionFilePath != null && !productionFilePath.isEmpty()));
    }

    public TestFile(String app, String productionFilePath) {
        this.app = app;
        this.productionFilePath = productionFilePath;
        this.codeSmells = new ArrayList<>();
    }

    public void addSmell(AbstractSmell smell) {
        codeSmells.add(smell);
    }

}
