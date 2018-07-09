package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

public class CompareDir {
    // compare between 2 directories
    public static boolean compareDir(File dir1, File dir2) throws IOException {
        File[] fileList1 = dir1.listFiles();
        File[] fileList2 = dir2.listFiles();
        Arrays.sort(fileList1);
        Arrays.sort(fileList2);
        HashMap<String, File> map1;
        if (fileList1.length == fileList2.length) {
            map1 = new HashMap<String, File>();
            for (int i = 0; i < fileList1.length; i++) {
                map1.put(fileList1[i].getName(), fileList1[i]);
            }
            return compareNow(fileList2, map1);
        } else {
            return false;
        }
    }

    private static boolean compareNow(File[] fileArr, HashMap<String, File> map) throws IOException
    {
        for(File currentFile : fileArr)
        {
            String fName = currentFile.getName();
            File fComp = map.get(fName);
            if(fComp!=null)
            {
                if(fComp.isDirectory())
                {
                    return false;
                }
                else
                {
                    final byte[] bytes1 = Files.readAllBytes(currentFile.toPath());
                    final byte[] bytes2 = Files.readAllBytes(fComp.toPath());
                    if(!Arrays.equals(bytes1,bytes2))
                    {
                        return false;
                    }
                }
            }
            else
            {
                System.out.println(currentFile.getName()+"\t\t"+"only in " + currentFile.getParent());
                return false;
            }
        }
        return true;
    }
}
