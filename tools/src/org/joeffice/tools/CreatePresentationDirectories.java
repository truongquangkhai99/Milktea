package org.joeffice.tools;

import java.io.*;

/**
 * This class will create the directories for the 30 days development. In each directory, it will create a
 * presentation.txt file C:\Java\projects\Joeffice\tools\src>javac org\joeffice\tools\CreatePresentationDirectories.java
 * java -cp . org.joeffice.tools.CreatePresentationDirectories
 *
 * @author Anthony Goubard
 */
public class CreatePresentationDirectories {

    public static void main(String[] args) throws IOException {
        File presentationsDir = new File("C:\\Java\\projects\\Joeffice\\admin\\marketing\\presentations");
        for (int i = 1; i <= 30; i++) {
            File dayDir = new File(presentationsDir, "day-" + i);
            dayDir.mkdir();
            File presentationFile = new File(dayDir, "presentation.txt");
            presentationFile.createNewFile();
        }
    }
}
