package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Blob {
    private String fileName;
    private String fileContent;
    private String fileHashCode;

    static File BLOBS_FLODER = new File(".gitlet/blobs");

    public Blob(String fileName) {
        this.fileName = fileName;

        this.fileContent =  Utils.getProjectFileContent(fileName);
        this.fileHashCode = makeFileHashCode();
    }

    public Blob(String fileName, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileHashCode = makeFileHashCode();
    }

    public String makeFileHashCode() {
        String fileHashCode = Utils.sha1(fileContent);
        return fileHashCode;
    }

    public void makeFileFile() {

        File fileFile = new File(BLOBS_FLODER, makeFileHashCode());
        try {
            fileFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(fileFile, fileContent);
    }

    public static String getGivenFileHashCode(String givenFileName) {
        String givenFileHashCode = Utils.sha1(Utils.readContentsAsString(new File(givenFileName)));
        return givenFileHashCode;

    }

    public static void putProjectFileToBlobFloder(String fileName) {
        Blob blob = new Blob(fileName);
        blob.makeFileFile();
    }

    public static File getGivenFileBlobFile(String givenFileHash) {
        return new File(BLOBS_FLODER, givenFileHash);
    }

    public static String getGivenFileCotent(String givenFileHash) {
        String givenFileCotent = Utils.readContentsAsString(getGivenFileBlobFile(givenFileHash));
        return givenFileCotent;
    }

    public static void putFileToBlobfromHash(String givenFileName, String givenFileContent) {
        Blob blob = new Blob(givenFileName, givenFileContent);
        if (Blob.isBlobFloderIncludeFile(blob.fileHashCode)) {
            return;
        }

        blob.makeFileFile();
    }

    public static boolean isBlobFloderIncludeFile(String givenFileHash) {
        File file = new File(BLOBS_FLODER, givenFileHash);
        return file.exists();
    }






}
