package gitlet;

// TODO: any imports you need here

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.sql.SQLData;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    static File COMMITS_FLODER = new File(".gitlet/commits");

    static File HEAD_FILE = new File(".gitlet/head");

    static File BRANCHS_FLODER = new File(".gitlet/branches");


    /** The message of this Commit. */
    private String message;


    private String timeStamp;


    private Map<String, String> files;


    private String parent;

    private String secondParent;

    private String commitHashCode;

    public Commit() {
        message = "initial commit";
        parent = null;
        secondParent = null;
        files = new TreeMap<>();

        Date initialTime = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        this.timeStamp = sdf.format(initialTime);

        commitHashCode = makeHashCode();
    }

    public Commit(String message, Commit parent) {
        this.message = message;
        this.parent = parent.getCommitHashCode();
        secondParent = null;
        files = parent.getFiles();

        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        this.timeStamp = sdf.format(currentTime);



        commitHashCode = null;

    }

    public Commit(String message, String timeStamp) {

    }

    /* TODO: fill in the rest of this class. */

    public String toString() {
        return message + timeStamp + parent + secondParent + filesString();
    }



    private String filesString() {
        if (files == null) {
            return null;
        } else {
            String allString = "";
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String keyAndValue = key + "=" + value;
                allString = allString + keyAndValue;


            }
            return allString;

        }
    }

    public String makeHashCode() {
        String commitHashCode = Utils.sha1(this.toString());
        return commitHashCode;
    }

    public void makeCommitFile() {
        String commitHashCode = makeHashCode();
        File commitFile = new File(COMMITS_FLODER, commitHashCode);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(commitFile, this);
    }

    public static Commit makeNewCommit(String message) {
        Commit currentCommit = getCurrentCommit();
        Commit newCommit = new Commit(message, currentCommit);
        return newCommit;

    }

    public boolean isExistFile(String fileName) {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            String key = entry.getKey();
            if (key.equals(fileName)) {
                return true;
            }
        }

        return false;
    }

    public String getFileHash(String fileName) {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            String key = entry.getKey();
            if (key.equals(fileName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean isCommitFileProjectFileSame(String fileName) {
        if (getFileHash(fileName).equals(Utils.getProjectFileHash(fileName))) {
            return true;
        }
        return false;
    }

    public static Commit getCurrentCommit() {
        File currentBranch = new File(BRANCHS_FLODER, Utils.readContentsAsString(HEAD_FILE));
        File currentCommitFile = new File(COMMITS_FLODER, Utils.readContentsAsString(currentBranch));
        Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);
        return currentCommit;
    }

    public String getCommitHashCode() {
        return commitHashCode;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void processStage(Stage stage) {
        if (stage.getMode() == 0) {
            files.remove(stage.getFileName());
        } else if (stage.getMode() == 1 && isExistFile(stage.getFileName())) {
            files.remove(stage.getFileName());
            files.put(stage.getFileName(), stage.getFileHashCode());
        } else {
            files.put(stage.getFileName(), stage.getFileHashCode());
        }
    }

    public void setCommitHashCode() {
        commitHashCode = makeHashCode();
    }

    public void printLog() {
        System.out.println("===");
        System.out.println("commit " +  commitHashCode);
        if (secondParent != null) {
            System.out.println("Merge: " + parent.substring(0, 8) + " " +  secondParent.substring(0, 8));
        }
        System.out.println("Date: " +  timeStamp);
        System.out.println(message + "\n");
    }

    public String getParent() {
        return parent;
    }

    public String getMessage() {
        return message;
    }

    public static Commit getGivenHashCommitObj(String givenCommitHash) {
        File givenHashCommitFile = new File(COMMITS_FLODER, givenCommitHash);
        if (!(givenHashCommitFile.exists())) {
            return null;
        }

        Commit givenHashCommit = Utils.readObject(givenHashCommitFile, Commit.class);
        return givenHashCommit;
    }

    public static Commit getGivenShortenedHashCommitObj(String shortenedCommitHash) {
        List<String> commitFiles = Utils.plainFilenamesInCommitFloder();
        for (String commitFile : commitFiles) {
            if (commitFile.substring(0, 7).equals(shortenedCommitHash)) {
                return getGivenHashCommitObj(commitFile);
            }
        }

        return null;
    }

    public static Commit getGivenBranchCommit(String branchName) {
        File commitFile = new File(COMMITS_FLODER, Utils.readContentsAsString(Utils.getGivenBranch(branchName)));
        return Utils.readObject(commitFile, Commit.class);
    }

    public static boolean isCommitExit(String givenCommitHash) {
        List<String> commitHashs = Utils.plainFilenamesInCommitFloder();
        if  (commitHashs.contains(givenCommitHash)) {
            return true;
        }
        return false;
    }

    public static String getSplitPoint(String currentBranch, String givenBranch) {
        Commit givenCommit = getGivenBranchCommit(givenBranch);
        Commit currentCommit = getGivenBranchCommit(currentBranch);

        String givenCommitParent = givenCommit.parent;
        String currentCommitParent = currentCommit.parent;

        while (true) {

            while (true) {
                if (givenCommitParent.equals(currentCommitParent)) {
                    break;
                }
                currentCommitParent = getGivenHashCommitObj(currentCommitParent).parent;

            }
            if  (givenCommitParent.equals(currentCommitParent)) {
                break;
            }
            givenCommitParent = getGivenHashCommitObj(givenCommitParent).parent;
        }

        return givenCommitParent;
    }

    public static List<String> findDeleteFileBetween(String currentCommitHash, String givenCommitHash) {
        Commit givenCommit = getGivenHashCommitObj(givenCommitHash);
        Commit currentCommit = getGivenHashCommitObj(currentCommitHash);

        List<String> deleteFiles = new ArrayList<>();

        for (String fileName : givenCommit.getFiles().keySet()) {
            if (!currentCommit.isExistFile(fileName)) {
                deleteFiles.add(fileName);
            }
        }
        return deleteFiles;

    }

    public static List<String> findAddFileBetween(String currentCommitHash, String givenCommitHash) {
        Commit givenCommit = getGivenHashCommitObj(givenCommitHash);
        Commit currentCommit = getGivenHashCommitObj(currentCommitHash);

        List<String> addFiles = new ArrayList<>();

        for (String fileName : currentCommit.getFiles().keySet()) {
            if (!givenCommit.isExistFile(fileName)) {
                addFiles.add(fileName);
            }
        }
        return addFiles;

    }

    public String getGivenFileNameHash(String givenFileName) {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            if (entry.getKey().equals(givenFileName)) {
                return entry.getValue();
            }
        }
        return null;
    }



    public static List<String> findModifyFileBetween(String currentCommitHash, String givenCommitHash) {
        Commit givenCommit = getGivenHashCommitObj(givenCommitHash);
        Commit currentCommit = getGivenHashCommitObj(currentCommitHash);

        List<String> modifyFiles = new ArrayList<>();

        for (Map.Entry<String, String> entry : currentCommit.getFiles().entrySet()) {

            if (givenCommit.isExistFile(entry.getKey()) && entry.getValue().equals(givenCommit.getFiles().get(entry.getKey()))) {
                modifyFiles.add(entry.getKey());
            }
        }
        return modifyFiles;
    }

    public static boolean isFileDeleted(String fileName, String currentCommitHash, String givenCommitHash) {
        List<String> deleteFiles = findDeleteFileBetween(currentCommitHash, givenCommitHash);
        if (deleteFiles.contains(fileName)) {
            return true;
        }
        return false;
    }

    public static boolean isFileAdded(String fileName, String currentCommitHash, String givenCommitHash) {
        List<String> addFiles = findAddFileBetween(currentCommitHash, givenCommitHash);
        if (addFiles.contains(fileName)) {
            return true;
        }
        return false;
    }

    public static boolean isFileModified(String fileName, String currentCommitHash, String givenCommitHash) {
        List<String> modifyFiles = findModifyFileBetween(currentCommitHash, givenCommitHash);
        if (modifyFiles.contains(fileName)) {
            return true;
        }
        return false;
    }

    public static Set<String> getAllFile(String currentCommitHash, String givenCommitHash, String splitPoint) {
        Set<String> allFiles = new HashSet<>();

        Commit currentCommit = Commit.getGivenHashCommitObj(currentCommitHash);
        Commit givenCommit = Commit.getGivenHashCommitObj(givenCommitHash);
        Commit splitPointCommit = Commit.getGivenHashCommitObj(splitPoint);

        for (String fileName :  currentCommit.getFiles().keySet()) {
            allFiles.add(fileName);
        }
        for (String fileName : givenCommit.getFiles().keySet()) {
            allFiles.add(fileName);
        }
        for (String fileName : splitPointCommit.getFiles().keySet()) {
            allFiles.add(fileName);
        }

        return allFiles;
    }

    public String getCommitFileCotentFromName(String fileName) {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                return Blob.getGivenFileCotent(entry.getValue());
            }
        }

        return null;
    }

    public void changeSecondParent(String secondParentHash) {
        secondParent =  secondParentHash;
    }













}
