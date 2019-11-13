package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    private FileOutputStream fo;
    public FileUtil(String filePath) {
        File target = new File(filePath);//"res\\filetest.txt"

        try {
            File parent = target.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            this.fo = new FileOutputStream(target, true);
            fo.write("hello".getBytes());
            System.out.println(target.getAbsolutePath());
            System.out.println("done");
            fo.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("error in init file util");
        } catch (IOException ie) {
            System.out.println("error in init file util");
        }
    }

    public static void main(String[] args) {
        FileUtil test = new FileUtil("res\\filetest.txt");
        
    }
}
