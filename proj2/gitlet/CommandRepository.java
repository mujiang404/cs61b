package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommandRepository {

    static File PROJECT_DIR = new File(".");

    static File HEAD_FILE = new File(".gitlet/head");

    static File COMMITS_FLODER = new File(".gitlet/commits");

    static File BLOBS_FLODER = new File(".gitlet/blobs");

    static File STAGES_FLODER = new File(".gitlet/stages");

    static File BRANCHS_FLODER = new File(".gitlet/branches");

    static File MASTER_FILE = new File(".gitlet/branches/master");


    public static void init() {

        if (new File(".gitlet").exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        File GitletDir = new File(".gitlet");
        GitletDir.mkdirs();
        File head = new File(".gitlet/head");
        try {
            head.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File commits = new File(".gitlet/commits");
        commits.mkdirs();
        File blobs = new File(".gitlet/blobs");
        blobs.mkdirs();
        File stages = new File(".gitlet/stages");
        try {
            stages.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File branchs = new File(".gitlet/branches");
        branchs.mkdirs();
        File masterFile = new File(".gitlet/branches/master");
        try {
            masterFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }





        Utils.writeContents(HEAD_FILE, "master");


        Commit initialCommit = new Commit();
        initialCommit.makeCommitFile();


        Utils.writeContents(MASTER_FILE, initialCommit.getCommitHashCode());

        Stages stagesObj = new Stages();
        Utils.writeObject(STAGES_FLODER, stagesObj);


    }

    public static void add(String fileName) {
        if (!(new File(fileName).exists())) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();

        boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

        boolean isFileContentEqulsCommitFile = true;

        if (isCommitIncludeFile) {
            isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));
        }


        boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

        boolean isFileContentEqulsStagesFile = true;

        int getTheStagesFileMode = currentStages.modeOfStage(fileName);

        if (isStagesIncludeFile && (getTheStagesFileMode == 1)) {

            isFileContentEqulsStagesFile = (currentStages.isTheSameFile(fileName));

        }



        if (isStagesIncludeFile && isCommitIncludeFile && isFileContentEqulsCommitFile) {
            currentStages.removeStage(fileName);
        } else if (isStagesIncludeFile && (getTheStagesFileMode == 0)) {
            currentStages.removeStage(fileName);
        } else if (isStagesIncludeFile) {
            if (!(isFileContentEqulsStagesFile)) {
                currentStages.removeStage(fileName);
                currentStages.addStageOfAddition(fileName);
            }
        } else if ((!isStagesIncludeFile) && (isCommitIncludeFile) && (isFileContentEqulsStagesFile)) {
            ;
        } else {
            currentStages.addStageOfAddition(fileName);
        }

        currentStages.storeCurrentStage();

    }

    public static void remove(String fileName) {
        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();


        boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

        boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));


        int getTheStagesFileMode = currentStages.modeOfStage(fileName);

        if (!(isCommitIncludeFile || isStagesIncludeFile)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        } else if (isStagesIncludeFile && (getTheStagesFileMode == 1)) {
            currentStages.removeStage(fileName);
        } else {
            if  (!isStagesIncludeFile) {
                currentStages.addStageOfRemoval(fileName);

            }
        }

        currentStages.storeCurrentStage();

    }

    public static void commitCommand(String message) {



        Commit commit = Commit.makeNewCommit(message);
        Stages stagesObject = Stages.getCurrentStages();
        Map<String, Stage> stages = stagesObject.getStages();

        if (stages.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        for (Map.Entry<String, Stage> entry : stages.entrySet()) {
            commit.processStage(entry.getValue());

        }
        stagesObject.clearStages();
        stagesObject.storeCurrentStage();
        commit.setCommitHashCode();

        Utils.changeCurrentBranchContent(commit.getCommitHashCode());

        commit.makeCommitFile();


    }

    public static void log() {
        String commitHashCode = Commit.getCurrentCommit().getCommitHashCode();
        while (commitHashCode != null) {
            Commit commit = Commit.getGivenHashCommitObj(commitHashCode);
            commit.printLog();
            commitHashCode = commit.getParent();
        }
    }

    public static void globalLog() {
        List<String> commitFloderFiles = Utils.plainFilenamesInCommitFloder();
        for (String fileName : commitFloderFiles) {
            Commit commit = Commit.getGivenHashCommitObj(fileName);
            commit.printLog();
        }
    }

    public static void findCommit(String message) {
        int numberOfCommit = 0;
        List<String> commitFloderFiles = Utils.plainFilenamesInCommitFloder();
        for (String fileName : commitFloderFiles) {
            Commit commit = Commit.getGivenHashCommitObj(fileName);
            if (commit.getMessage().equals(message)) {
                numberOfCommit++;
                System.out.println(fileName);
            }
        }
        if (numberOfCommit == 0) {

            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        List<String> branchNames = Utils.plainFilenamesInBranchFloder();
        for (String branchName : branchNames) {
            if (Utils.isCurrentBranch(branchName)) {
                System.out.println("*" + branchName);
                continue;
            }
            System.out.println(branchName);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        Stages stages = Stages.getCurrentStages();
        Map<String, Stage> stagesMap = stages.getStages();
        List<String> additionFileNames = new ArrayList<>();
        List<String> removalFileNames = new ArrayList<>();
        for (Stage stage : stagesMap.values()) {
            if (stage.getMode() == 1) {
                additionFileNames.add(stage.getFileName());
            } else {
                removalFileNames.add(stage.getFileName());
            }
        }
        Collections.sort(additionFileNames);
        Collections.sort(removalFileNames);
        for (String fileName : additionFileNames) {
            System.out.println(fileName);
        }
        System.out.println();


        System.out.println("=== Removed Files ===");
        for (String fileName : removalFileNames) {
            System.out.println(fileName);
        }
        System.out.println();



        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();

        List<String> projectFileNameList = Utils.plainFilenamesIn(Repository.CWD);


        List<String> deleteAndModifyFileNames = new ArrayList<>();
        List<String> untrackedFileNames = new ArrayList<>();

        Set<String> deleteFileNames = new HashSet<>();

        for (String stageFileName : currentStages.getStages().keySet()) {
            boolean isProjIncludeFile = Utils.isProjectIncludeFile(stageFileName);


            boolean isCommitIncludeFile = (currentCommit.isExistFile(stageFileName));

            boolean isStagesIncludeFile = (currentStages.isExistFile(stageFileName));



            int getTheStagesFileMode = currentStages.modeOfStage(stageFileName);



            if ((!isProjIncludeFile) && (getTheStagesFileMode == 1)) {
                deleteFileNames.add(stageFileName);
            }
        }

        for (String commitFilename : currentCommit.getFiles().keySet()) {
            boolean isProjectIncludeFile = Utils.isProjectIncludeFile(commitFilename);


            boolean isCommitIncludeFile = (currentCommit.isExistFile(commitFilename));




            boolean isStagesIncludeFile = (currentStages.isExistFile(commitFilename));



            int getTheStagesFileMode = currentStages.modeOfStage(commitFilename);



            if ((!isProjectIncludeFile) && (!isStagesIncludeFile)) {
                deleteFileNames.add(commitFilename);
            } else if ((!isProjectIncludeFile) && (isStagesIncludeFile) && (getTheStagesFileMode == 1)) {
                deleteFileNames.add(commitFilename);
            }
        }
        for (String deleteFileName : deleteFileNames) {
            deleteAndModifyFileNames.add(deleteFileName);
        }



        for (String fileName : projectFileNameList) {


            boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

            boolean isFileContentEqulsCommitFile = true;

            if (isCommitIncludeFile) {
                isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));
            }


            boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

            boolean isFileContentEqulsStagesFile = true;

            int getTheStagesFileMode = currentStages.modeOfStage(fileName);

            if (isStagesIncludeFile && (getTheStagesFileMode == 1)) {

                isFileContentEqulsStagesFile = (currentStages.isTheSameFile(fileName));

            }



            if (isStagesIncludeFile && (!isFileContentEqulsStagesFile)) {
                deleteAndModifyFileNames.add(fileName);
            } else if ((!isStagesIncludeFile) && isCommitIncludeFile && (!isFileContentEqulsCommitFile)) {
                deleteAndModifyFileNames.add(fileName);
            } else if ((!isCommitIncludeFile) && (!isStagesIncludeFile)) {
                untrackedFileNames.add(fileName);
            } else if ((isStagesIncludeFile && getTheStagesFileMode == 0) && isCommitIncludeFile) {
                untrackedFileNames.add(fileName);
            }


        }

        Collections.sort(deleteAndModifyFileNames);
        Collections.sort(untrackedFileNames);

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String deleteAndModifyFileName : deleteAndModifyFileNames) {
            if (deleteFileNames.contains(deleteAndModifyFileName)) {
                System.out.println(deleteAndModifyFileName + " (deleted)");
            } else {
                System.out.println(deleteAndModifyFileName + " (modified)");
            }
        }
        System.out.println();


        System.out.println("=== Untracked Files ===");
        for (String untrackedFileName : untrackedFileNames) {
            System.out.println(untrackedFileName);
        }
        System.out.println();


    }





    public static void checkoutCurrentCommitFile(String fileName) {
        Commit commit = Commit.getCurrentCommit();
        if (!(commit.isExistFile(fileName))) {

            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String commitFileContent = Blob.getGivenFileCotent(commit.getFileHash(fileName));

        if (Utils.isProjectIncludeFile(fileName)) {
            if (!commit.isCommitFileProjectFileSame(fileName)) {
                Utils.overwriteFileContent(fileName, commitFileContent);
            }

        } else {
            Utils.makeProjectFile(fileName, commitFileContent);
        }

    }



    public static void checkoutGivenCommitFile(String commitHash, String fileName) {
        Commit commit = null;

        if (commitHash.length() == 40) {
            commit = Commit.getGivenHashCommitObj(commitHash);
        } else if (commitHash.length() == 6) {
            commit = Commit.getGivenShortenedHashCommitObj(commitHash);
        }

        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        if (!(commit.isExistFile(fileName))) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String commitFileContent = Blob.getGivenFileCotent(commit.getFileHash(fileName));

        if (Utils.isProjectIncludeFile(fileName)) {
            if (!commit.isCommitFileProjectFileSame(fileName)) {
                Utils.overwriteFileContent(fileName, commitFileContent);
            }
        } else {
            Utils.makeProjectFile(fileName, commitFileContent);
        }

    }


    public static void checkoutBranch(String branchName) {
        if (Utils.isCurrentBranch(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        if (!Utils.isBranchExist(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();

        List<String> projectFileNameList = Utils.plainFilenamesIn(Repository.CWD);

        List<String> untrackedFileNames = new ArrayList<>();

        for (String fileName : projectFileNameList) {


            boolean isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

            boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

            boolean isFileContentEqulsStagesFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            int getTheStagesFileMode = currentStages.modeOfStage(fileName);

            if ((!isCommitIncludeFile) && (!isStagesIncludeFile)) {
                untrackedFileNames.add(fileName);

            }

        }

        Commit commitBeCheckedout = Commit.getGivenBranchCommit(branchName);

        for (String untrackedFileName : untrackedFileNames) {
            if ((commitBeCheckedout.isExistFile(untrackedFileName)) && currentCommit.isCommitFileProjectFileSame(untrackedFileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            } else if (!commitBeCheckedout.isExistFile(untrackedFileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        currentStages.clearStages();

        currentStages.storeCurrentStage();

        Utils.clearCWDFiles();



        for (Map.Entry<String, String> entry : commitBeCheckedout.getFiles().entrySet()) {
            Utils.makeProjectFile(entry.getKey(),  Blob.getGivenFileCotent(entry.getValue()));
        }

        Utils.changBranch(branchName);





    }

    /*

    public static void checkoutBranch(String branchName) {
        if (Utils.isCurrentBranch(branchName)) {
            throw new GitletException("No need to checkout the current branch.");
        }

        if (!Utils.isBranchExist(branchName)) {
            throw new GitletException("No such branch exists.");
        }

        String fileName = null;

        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();

        List<String> projectFileNameList = Utils.plainFilenamesInProject();


        List<String> modifyAndUntrackedFileNames = new ArrayList<>();


        Set<String> deleteFileNames = new HashSet<>();

        for (String stageFileName : currentStages.getStages().keySet()) {
            boolean isProjIncludeFile = Utils.isProjectIncludeFile(stageFileName);
            if (!isProjIncludeFile) {
                deleteFileNames.add(stageFileName);

            }
        }
        for (String commitFilename : currentCommit.getFiles().keySet()) {
            boolean isProjectIncludeFile = Utils.isProjectIncludeFile(commitFilename);
            if (!isProjectIncludeFile) {
                deleteFileNames.add(commitFilename);
            }
        }



        for (String projectName : projectFileNameList) {


            boolean isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

            boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

            boolean isFileContentEqulsStagesFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            int getTheStagesFileMode = currentStages.modeOfStage(fileName);

            if (isStagesIncludeFile && (!isFileContentEqulsStagesFile)) {
                modifyAndUntrackedFileNames.add(projectName);
            } else if (!(isStagesIncludeFile) && isCommitIncludeFile && (!isFileContentEqulsCommitFile)) {
                modifyAndUntrackedFileNames.add(projectName);
            } else if ((!isCommitIncludeFile) && (!isStagesIncludeFile)) {
                modifyAndUntrackedFileNames.add(projectName);
            } else if ((isStagesIncludeFile && getTheStagesFileMode == 0) && isCommitIncludeFile) {
                modifyAndUntrackedFileNames.add(projectName);
            }


        }

        Commit commitBeCheckedout = Commit.getGivenBranchCommit(branchName);
        for (Map.Entry<String, String> entry : commitBeCheckedout.getFiles().entrySet()) {
            if (deleteFileNames.contains(entry.getKey())) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }

        for (String fileBeChecked : modifyAndUntrackedFileNames) {
            if (!(commitBeCheckedout.isExistFile(fileBeChecked))) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            } else if (!(commitBeCheckedout.isCommitFileProjectFileSame(fileBeChecked))) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }

        }

        Utils.changBranch(branchName);

        currentStages.clearStages();

        currentStages.storeCurrentStage();

        Utils.clearCWDFiles();

        for (Map.Entry<String, String> entry : commitBeCheckedout.getFiles().entrySet()) {
            Utils.makeProjectFile(entry.getKey(), Blob.getGivenFileCotent(entry.getValue()));
        }

    }


     */

    public static void branchCommand(String branchName) {
        if (Utils.isBranchExist(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        File branchFile = new File(BRANCHS_FLODER, branchName);
        try {
            branchFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(branchFile, Commit.getCurrentCommit().getCommitHashCode());


    }

    public static void rmbranchCommand(String branchName) {
        if (!Utils.isBranchExist(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Utils.isCurrentBranch(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        Utils.getGivenBranch(branchName).delete();
    }

    public static void resetCommand(String commitHash) {
        if (!Commit.isCommitExit(commitHash)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();

        List<String> projectFileNameList = Utils.plainFilenamesIn(Repository.CWD);

        List<String> untrackedFileNames = new ArrayList<>();

        for (String fileName : projectFileNameList) {


            boolean isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

            boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

            boolean isFileContentEqulsStagesFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            int getTheStagesFileMode = currentStages.modeOfStage(fileName);

            if ((!isCommitIncludeFile) && (!isStagesIncludeFile)) {
                untrackedFileNames.add(fileName);
            }
        }

        Commit commitBeCheckedout = null;

        if (commitHash.length() == 40) {
            commitBeCheckedout = Commit.getGivenHashCommitObj(commitHash);
        } else if (commitHash.length() == 6) {
            commitBeCheckedout = Commit.getGivenShortenedHashCommitObj(commitHash);
        }

        for (String untrackedFileName : untrackedFileNames) {
            if ((commitBeCheckedout.isExistFile(untrackedFileName)) && currentCommit.isCommitFileProjectFileSame(untrackedFileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            } else if (!commitBeCheckedout.isExistFile(untrackedFileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        currentStages.clearStages();

        currentStages.storeCurrentStage();

        Utils.clearCWDFiles();



        for (Map.Entry<String, String> entry : commitBeCheckedout.getFiles().entrySet()) {
            Utils.makeProjectFile(entry.getKey(),  Blob.getGivenFileCotent(entry.getValue()));
        }

        Utils.changHeadToHash(commitBeCheckedout.getCommitHashCode());


    }

    /*

    public static void resetCommand(String commitHash) {
        if (!Commit.isCommitExit(commitHash)) {
            throw new GitletException("No commit with that id exists.");
        }


        String fileName = null;

        Commit currentCommit = Commit.getCurrentCommit();

        Stages currentStages = Stages.getCurrentStages();

        List<String> projectFileNameList = Utils.plainFilenamesInProject();


        List<String> modifyAndUntrackedFileNames = new ArrayList<>();


        Set<String> deleteFileNames = new HashSet<>();

        for (String stageFileName : currentStages.getStages().keySet()) {
            boolean isProjIncludeFile = Utils.isProjectIncludeFile(stageFileName);
            if (!isProjIncludeFile) {
                deleteFileNames.add(stageFileName);

            }
        }
        for (String commitFilename : currentCommit.getFiles().keySet()) {
            boolean isProjectIncludeFile = Utils.isProjectIncludeFile(commitFilename);
            if (!isProjectIncludeFile) {
                deleteFileNames.add(commitFilename);
            }
        }



        for (String projectName : projectFileNameList) {


            boolean isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

            boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

            boolean isFileContentEqulsStagesFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            int getTheStagesFileMode = currentStages.modeOfStage(fileName);

            if (isStagesIncludeFile && (!isFileContentEqulsStagesFile)) {
                modifyAndUntrackedFileNames.add(projectName);
            } else if (!(isStagesIncludeFile) && isCommitIncludeFile && (!isFileContentEqulsCommitFile)) {
                modifyAndUntrackedFileNames.add(projectName);
            } else if ((!isCommitIncludeFile) && (!isStagesIncludeFile)) {
                modifyAndUntrackedFileNames.add(projectName);
            } else if ((isStagesIncludeFile && getTheStagesFileMode == 0) && isCommitIncludeFile) {
                modifyAndUntrackedFileNames.add(projectName);
            }


        }

        Commit commitBeCheckedout = null;

        if (commitHash.length() == 40) {
            commitBeCheckedout = Commit.getGivenHashCommitObj(commitHash);
        } else if (commitHash.length() == 6) {
            commitBeCheckedout = Commit.getGivenShortenedHashCommitObj(commitHash);
        }


        for (Map.Entry<String, String> entry : commitBeCheckedout.getFiles().entrySet()) {
            if (deleteFileNames.contains(entry.getKey())) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }

        for (String fileBeChecked : modifyAndUntrackedFileNames) {
            if (!(commitBeCheckedout.isExistFile(fileBeChecked))) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            } else if (!(commitBeCheckedout.isCommitFileProjectFileSame(fileBeChecked))) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }

        }

        Utils.changBranch(commitHash);

        currentStages.clearStages();

        currentStages.storeCurrentStage();

        Utils.clearCWDFiles();

        for (Map.Entry<String, String> entry : commitBeCheckedout.getFiles().entrySet()) {
            Utils.makeProjectFile(entry.getKey(), Blob.getGivenFileCotent(entry.getValue()));
        }

    }




     */

    public static void merge(String branchName) {
        Stages currentStages = Stages.getCurrentStages();
        if (currentStages.isStagesEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        if (Utils.isCurrentBranch(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        if (Utils.isBranchExist(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        Commit currentCommit = Commit.getCurrentCommit();

        List<String> projectFileNameList = Utils.plainFilenamesIn(Repository.CWD);

        List<String> untrackedFileNames = new ArrayList<>();

        for (String fileName : projectFileNameList) {


            boolean isFileContentEqulsCommitFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            boolean isCommitIncludeFile = (currentCommit.isExistFile(fileName));

            boolean isStagesIncludeFile = (currentStages.isExistFile(fileName));

            boolean isFileContentEqulsStagesFile = (currentCommit.isCommitFileProjectFileSame(fileName));

            int getTheStagesFileMode = currentStages.modeOfStage(fileName);

            if ((!isCommitIncludeFile) && (!isStagesIncludeFile)) {
                untrackedFileNames.add(fileName);

            }

        }

        Commit commitBeCheckedout = Commit.getGivenBranchCommit(branchName);

        for (String untrackedFileName : untrackedFileNames) {
            if ((commitBeCheckedout.isExistFile(untrackedFileName)) && currentCommit.isCommitFileProjectFileSame(untrackedFileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            } else if (!commitBeCheckedout.isExistFile(untrackedFileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        String currentBranch = Utils.getCurrentBranch();


        String splitPointName = Commit.getSplitPoint(currentBranch, branchName);

        Commit givenCommit = Commit.getGivenBranchCommit(branchName);

        String currentCommitHash = currentCommit.getCommitHashCode();

        String givenCommitHash = givenCommit.getCommitHashCode();

        if (splitPointName.equals(Commit.getGivenBranchCommit(branchName).getCommitHashCode())) {
            checkoutBranch(currentBranch);
        } else if (splitPointName.equals(Commit.getGivenBranchCommit(currentBranch).getCommitHashCode())) {
            return;
        } else {
            boolean isConflict = false;

            Set<String> allFiles = Commit.getAllFile(currentCommitHash, givenCommitHash, splitPointName);
            for (String fileNameBeExam :  allFiles) {
                if (Commit.isFileDeleted(fileNameBeExam, currentCommitHash, splitPointName) && (Commit.isFileModified(fileNameBeExam, givenCommitHash, splitPointName))) {
                    String unionContent = "<<<<<<< HEAD\n" + "=======\n" + givenCommit.getCommitFileCotentFromName(fileNameBeExam) + "\n" + ">>>>>>>";
                    Stages.stagedForAdditionFromContent(fileNameBeExam, unionContent);
                    isConflict =  true;

                } else if (Commit.isFileModified(fileNameBeExam, currentCommitHash, splitPointName) && Commit.isFileModified(fileNameBeExam, givenCommitHash, splitPointName) && !(currentCommit.getFileHash(fileNameBeExam).equals(givenCommit.getFileHash(fileNameBeExam)))) {
                    String unionContent = "<<<<<<< HEAD\n" + currentCommit.getCommitFileCotentFromName(fileNameBeExam) + "\n" + "=======\n" + givenCommit.getCommitFileCotentFromName(fileNameBeExam) + "\n" + ">>>>>>>";
                    Stages.stagedForAdditionFromContent(fileNameBeExam, unionContent);
                    isConflict =  true;

                } else if (Commit.isFileModified(fileNameBeExam, currentCommitHash, splitPointName) && Commit.isFileDeleted(fileNameBeExam, givenCommitHash, splitPointName)) {
                    String unionContent = "<<<<<<< HEAD\n" + currentCommit.getCommitFileCotentFromName(fileNameBeExam) + "\n" + "=======\n" + ">>>>>>>";
                    Stages.stagedForAdditionFromContent(fileNameBeExam, unionContent);
                    isConflict =  true;

                } else if (!(Commit.isFileAdded(fileNameBeExam, currentCommitHash, splitPointName)) && Commit.isFileAdded(fileNameBeExam, givenCommitHash, splitPointName)) {
                    String unionContent = givenCommit.getCommitFileCotentFromName(fileNameBeExam);
                    Stages.stagedForAdditionFromContent(fileNameBeExam, unionContent);

                }
            }

            commitCommand("Merged " + branchName + " into " + currentBranch + ".");
            Commit.getCurrentCommit().changeSecondParent(givenCommitHash);
            if (isConflict) {
                System.out.println("Encountered a merge conflict.");
            }


        }

    }
















}
