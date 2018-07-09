package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;

public class Application {
    public static void main(String[] args) throws IOException {
        File tempDestinationDir = new File(System.getProperty("user.home")+ File.separator + "csv");
        if (tempDestinationDir.isDirectory() && tempDestinationDir.exists())
            FileUtils.forceDelete(tempDestinationDir);
        String tempDestinationDirName = tempDestinationDir.getAbsolutePath();
        // copy the csv files to temp directory since during test we delete them
        File sourceCsvDir = new File("list-sort/src/main/resources/csv/inputFiles/8800");
        sourceCsvDir = new File(sourceCsvDir.getAbsolutePath());
        FileUtils.copyDirectory(sourceCsvDir, tempDestinationDir);

        final List<File> allCsvFilesInDirSortedByName = getAllCsvFilesInDirSortedByName(sourceCsvDir)
                .collect(Collectors.toList());

        deleteDirectory(tempDestinationDir);
    }

    private static Stream<File> getAllCsvFilesInDirSortedByName(File sourceCsvDir) throws IOException {
        String fileNamePattern = "SiteID_(\\d*)-BatchID_(\\d*).csv";
        Pattern compiledFileNamePattern = Pattern.compile(fileNamePattern);

        Comparator<File> sortBy = (fileName1, fileName2) -> {
            Matcher compiledFileName1 = compiledFileNamePattern.matcher(fileName1.getName());
            Matcher compiledFileName2 = compiledFileNamePattern.matcher(fileName2.getName());
            compiledFileName1.find();
            compiledFileName2.find();
            Integer siteId1 = Integer.parseInt(compiledFileName1.group(1));
            Integer siteId2 = Integer.parseInt(compiledFileName2.group(1));
            if (siteId1.compareTo(siteId2) != 0)
                return siteId1.compareTo(siteId2);
            Integer batchId1 = Integer.parseInt(compiledFileName1.group(2));
            Integer batchId2 = Integer.parseInt(compiledFileName2.group(2));
            return batchId1.compareTo(batchId2);
        };
        System.out.println("sorted files to aggregate are : ");
        return Arrays.stream(sourceCsvDir.listFiles())
                .filter(filePath -> compiledFileNamePattern.matcher(filePath.getName()).find())
                .sorted(sortBy)
                .peek(x -> System.out.println(x.toString()));
    }

    private static void deleteDirectory(File fileToBeDeleted) throws IOException {
        FileUtils.deleteDirectory(fileToBeDeleted);
//        File[] allCsvFiles = fileToBeDeleted.listFiles();
//        if (allCsvFiles != null) {
//            Arrays.stream(allCsvFiles).forEach(file -> deleteDirectory(file));
//        }
//        if (fileToBeDeleted.isFile() && fileToBeDeleted.getName().toLowerCase().endsWith(".csv") ||
//                fileToBeDeleted.isDirectory())
//            fileToBeDeleted.delete();
    }

}
