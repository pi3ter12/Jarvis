package elearning.wiacekp.pl.jarvis.helpclasses;

import java.io.File;


public class FilesList {
    private int id;
    private File file;
    private boolean isDirectory;

    public FilesList(int id, File file, boolean isDirectory) {
        this.id = id;
        this.file = file;
        this.isDirectory = isDirectory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
