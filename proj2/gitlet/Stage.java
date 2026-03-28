package gitlet;


import java.io.File;
import java.io.Serializable;


public class Stage implements Serializable {
    private int mode;
    private String fileName;
    private String fileHashCode;

    static File STAGES_FLODER = new File(".gitlet/stages");

    public Stage(int mode, String fileName, String fileHashCode) {
        this.mode = mode;
        this.fileName = fileName;
        this.fileHashCode = fileHashCode;

    }

    public String getFileName() {
        return fileName;
    }

    public String getFileHashCode() {
        return fileHashCode;
    }

    public int getMode() {
        return mode;
    }





}
