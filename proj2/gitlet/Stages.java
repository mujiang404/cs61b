package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Stages implements Serializable {
    private Map<String, Stage> stages;

    static File STAGES_FLODER = new File(".gitlet/stages");

    public Stages() {
        this.stages = new HashMap<>();
    }

    public boolean isExistFile(String fileName) {
        if (getStage(fileName) == null) {
            return false;
        }


        return true;
    }

    private String getFileHash(String fileName) {
        if (getStage(fileName) == null) {
            return null;
        }

        return getStage(fileName).getFileHashCode();
    }

    public boolean isTheSameFile(String fileName) {
        if (getFileHash(fileName) == Utils.getProjectFileHash(fileName)) {
            return true;
        }
        return false;
    }

    public int modeOfStage(String fileName) {

        if (getStage(fileName) == null) {
            return -1;
        }

        return getStage(fileName).getMode();
    }

    public static Stages getCurrentStages() {
        Stages stages = Utils.readObject(STAGES_FLODER, Stages.class);
        return stages;
    }

    public void removeStage(String fileName) {
        if (getStage(fileName) == null) {
            return;
        }
        stages.remove(fileName);
    }

    private Stage getStage(String fileName) {
        for (Stage stage : this.stages.values()) {
            if(stage.getFileName().equals(fileName)) {
                return stage;
            }
        }

        return null;
    }

    public void addStageOfAddition(String fileName) {
        String currentFileHash = Utils.getProjectFileHash(fileName);
        int currentFileMode = 1;
        Stage stage = new Stage(currentFileMode, fileName, currentFileHash);
        this.stages.put(fileName, stage);

        Blob.putProjectFileToBlobFloder(fileName);
    }

    public void storeCurrentStage() {
        Utils.writeObject(STAGES_FLODER, this);
    }

    public void addStageOfRemoval(String fileName) {
        String currentFileHash = Utils.getProjectFileHash(fileName);
        int currentFileMode = 0;
        Stage stage = new Stage(currentFileMode, fileName, currentFileHash);
        this.stages.put(fileName, stage);
    }

    public Map<String, Stage> getStages() {

        return stages;
    }

    public void clearStages() {

        this.stages.clear();
    }

    public boolean isStagesEmpty() {
        return this.stages.isEmpty();
    }

    public static void stagedForAdditionFromContent(String fileName, String content) {
        Blob.putFileToBlobfromHash(fileName, content);

        Stage newStage = new Stage(1, fileName, Utils.sha1(content));

        Stages currentStages = getCurrentStages();
        currentStages.getStages().put(fileName, newStage);
        currentStages.storeCurrentStage();


    }

    public static void stagedForRemovalFromContent(String fileName, String content) {
        Stage newStage = new Stage(0, fileName, Utils.sha1(content));
        Stages currentStages = getCurrentStages();
        currentStages.getStages().put(fileName, newStage);
        currentStages.storeCurrentStage();
    }









}
