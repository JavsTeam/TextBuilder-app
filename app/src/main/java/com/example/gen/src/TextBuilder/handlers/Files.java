package com.example.gen.src.TextBuilder.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;

public class Files {
    /**
     * Retrieves a file from the src/ directory of the project by its name.
     * If such a file is found, returns its object representation.
     * Else creates a file with that name in the files/ directory by default
     * and then returns its object representation.
     *
     * @param fileName Full file name including its extension
     * @return {@link File} representation of the file with the given name
     */
    public static File getFile(String fileName) {
        File file;
        return (file = recursiveFileSearch(Dir.SRC.get(), fileName)) != null ?
                file : createFile(fileName);
    }

    public static String getPath(String fileName) {
        return getFile(fileName).getPath();
    }

    /**
     * Creates a file with given name in a given directory.
     *
     * @param fileName        Full file name including its extension
     * @param pathToDirectory Path to directory where to create a new file
     * @return Object representation of created file or if file was not created returns <code>null</code>
     * @throws IOException         If {@code pathToDirectory} is invalid.
     *                             Exception is being caught in the method. If so, method returns <code>null</code>
     * @throws FileSystemException If {@code fileName} is invalid or clashes wit an existing file's name in a directory.
     *                             Exception is being caught in the method. If so, method returns <code>null</code>
     */
    public static File createFile(String fileName, String pathToDirectory) {
        File newFile = new File(pathToDirectory + "/" + fileName);
        try {
            if (newFile.createNewFile()) return newFile;
            throw new FileSystemException("Unacceptable file name: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a file with given name in a directory given as object.
     *
     * @param fileName  Full file name including its extension
     * @param directory Object representation of directory where to create a new file
     * @return Object representation of created file or if file was not created returns <code>null</code>
     * @throws IOException         If {@code pathToDirectory} is invalid.
     *                             Exception is being caught in the method. If so, method returns <code>null</code>
     * @throws FileSystemException If {@code fileName} is invalid or clashes wit an existing file's name in a directory.
     *                             Exception is being caught in the method. If so, method returns <code>null</code>
     */
    public static File createFile(String fileName, File directory) {
        return createFile(fileName, directory.getPath());
    }

    /**
     * Creates a file with given name in a files/ directory by default.
     *
     * @param fileName Full file name including its extension
     * @return Object representation of created file or if file was not created returns <code>null</code>
     * @throws FileSystemException If {@code fileName} is invalid or clashes wit an existing file's name in a directory.
     *                             Exception is being caught in the method. If so, method returns <code>null</code>
     */
    public static File createFile(String fileName) {
        return createFile(fileName, Dir.FILES.get().getPath());
    }

    public static boolean isFileExist(String fileName) {
        return recursiveFileSearch(Dir.SRC.get(), fileName) != null;
    }

    public static boolean isDirExist(String dirName) {
        return recursiveDirSearch(Dir.SRC.get(), dirName) != null;
    }

    public static File getDirectory(String dirName) {
        File file;
        return (file = recursiveDirSearch(Dir.SRC.get(), dirName)) != null ?
                file : createDirectory(dirName, Dir.DATA.get().getPath());
    }

    public static File createDirectory(String dirName, String pathToSource) {
        File newFile = new File(pathToSource + "/" + dirName);
        File dir;
        if (isDirExist(dirName) && (dir = getDirectory(dirName)).getPath().equals(newFile.getPath())) {
            return dir;
        }
        if (newFile.mkdir()) {
            return newFile;
        }
        try {
            throw new FileSystemException("Unacceptable directory name or invalid path");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File recursiveFileSearch(File current, String fileName) {
        if (current.isDirectory()) {
            for (File content : current.listFiles()) {
                File result = recursiveFileSearch(content, fileName);
                if (result != null) {
                    return result;
                }
            }
            return null;
        } else {
            return current.getName().equals(fileName) ? current : null;
        }
    }

    private static File recursiveDirSearch(File current, String dirName) {
        if (current.isDirectory()) {
            if (current.getName().equals(dirName)) {
                return current;
            }
            for (File content : current.listFiles()) {
                File result = recursiveDirSearch(content, dirName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public enum Dir {
        SRC(new File("src")),
        PROJECT(getDirectory("TextBuilder")),
        DATA(getDirectory("data")),
        PROCESSED(getDirectory("processed")),
        FILES(getDirectory("files"));

        File dir;

        Dir(File dir) {
            this.dir = dir;
        }

        public File get() {
            return dir;
        }
    }
}
