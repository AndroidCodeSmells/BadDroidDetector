package codesmell.entity;

import codesmell.AbstractSmell;

import java.util.ArrayList;
import java.util.List;

public class File {
    private String app;

    public String getTag() {
        return tag;
    }

    private String tag;

    private String filePath;



    private List<AbstractSmell> codeSmells;

    public String getApp() {
        return app;
    }

    public List<AbstractSmell> getCodeSmells() {
        return codeSmells;
    }

    public String getFilePath() {
        return filePath;//((filePath != null && !filePath.isEmpty()));
    }

    public File(String app,String tag, String filePath ) {
        this.app = app;
        this.tag = tag;
        this.filePath = filePath;
        this.codeSmells = new ArrayList<>();
    }

    public FileType getFileType(){
        String ext = this.filePath.substring(this.filePath.lastIndexOf("."));

        if(ext.toLowerCase().equals(".xml"))
            return FileType.XML;
        else  if(ext.toLowerCase().equals(".java")){
            return FileType.JAVA;
        }else {
            return FileType.UNKNOWN;
        }
    }

    public void addSmell(AbstractSmell smell) {
        codeSmells.add(smell);
    }

    public enum FileType{
        JAVA,
        XML,
        UNKNOWN
    }
}
