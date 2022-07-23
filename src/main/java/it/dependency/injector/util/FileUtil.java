package it.dependency.injector.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final LoggerUtil _log = new LoggerUtil(FileUtil.class.getName());

    public static List<File> getAllFiles(String root){
        List<File> allFiles = new ArrayList<>();
        if(!root.isEmpty()){
            File rootDirectory = new File(root);
            if(rootDirectory.isDirectory()){
                File[] listofFiles=rootDirectory.listFiles();
                if(listofFiles != null){
                    for(File f:listofFiles){
                        if(f.isDirectory()){
                            allFiles.addAll(getAllFiles(f.getAbsolutePath()));
                        }else{
                            allFiles.add(f);
                        }
                    }
                }
            }else{
                allFiles.add(rootDirectory);
            }
        }
        return allFiles;
    }

    public static BufferedReader getBufferedReader(File file){
        try {
            return new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            _log.error("creating null reader for file: "+file.getName());
            return new BufferedReader(BufferedReader.nullReader());
        }
    }
}
