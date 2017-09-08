
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author donato
 */
public class SchedaClass implements FileVisitor<Path> {

    public String log = "";
    Path destination;

    private boolean IsHidden(Path file) throws IOException {
        boolean hidden = false;

        //if ((Files.isHidden(file)) || (file.getFileName().startsWith("."))) {
        if (Files.isHidden(file)) {
            hidden = true;
        }

        return hidden;
    }

    // Utility method to print a better formatted time stamp
    private static Calendar toDate(FileTime ft) {
        //return DateFormat.getInstance().format(new Date(ft.toMillis()));
        Calendar cdr = new GregorianCalendar();
        cdr.setTime(new Date(ft.toMillis()));
        //return "Years: " + cdr.get(Calendar.YEAR) + " Month: " + cdr.get(Calendar.MONTH);
        return cdr;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        //System.out.print("preVisitDirectory: ");
        outputtext("preVisitDirectory: ");

        if (IsHidden(dir)) {
            //System.out.print("Hidden ");
            outputtext("Hidden ");

        }
        if (attrs.isDirectory()) {

            //System.out.format("Directory: %s ", dir);
            outputtext("Directory: " + dir);
        } else {
            //System.out.format("Other: %s ", dir);
            outputtext("Directory: " + dir);
        }
        String name = dir.getFileName().toString();
        String ext = name.substring(name.lastIndexOf(".") + 1);
        String output = "(" + attrs.size() + " bytes, lastModifiedTime: " + "Years: " + toDate(attrs.lastModifiedTime()).get(Calendar.YEAR) + " Month: " + toDate(attrs.lastModifiedTime()).get(Calendar.MONTH) + " extension " + ext + " )";
        //System.out.println(output);
        outputtext(output+"\n");
        if (IsHidden(dir)) {
            return SKIP_SUBTREE;
        }
        return CONTINUE;
    }
    private void outputtext(String test)
    {
        System.out.print(test);
        log = log + test;
    }
    private void createdir(String newDirectory) {
        Path newDirectoryPath = Paths.get(newDirectory);
        //if (!Files.exists(newDirectoryPath)) {
        try {
            Files.createDirectories(newDirectoryPath);
            //System.out.println("Cerate Dir " + newDirectoryPath.toString());
            outputtext("Cerate Dir " + newDirectoryPath.toString()+"\n");
        } catch (IOException e) {
            //System.err.println(e);
        }
        //}
    }

    private void move(Path source, String dest) throws IOException {
        String name = source.getFileName().toString();
        String ext = "";

        if (name.contains(".")) {
            int punto = name.lastIndexOf(".");
            ext = name.substring(punto);
            name = name.substring(0, punto);
        }
        dest = dest + "/" + name;
        String ren = "";
        Integer i = 0;
        if (Files.size(Paths.get(dest + ren + ext)) != Files.size(source)) {
            while (Files.exists(Paths.get(dest + ren + ext))) {

                i = i + 1;
                ren = i.toString();
            }
            try {
                Files.move(source, Paths.get(dest + ren + ext));
                //System.out.println("move " + source.toString() + "---->" + dest + ren + ext);
                outputtext("move " + source.toString() + "---->" + dest + ren + ext+"\n");

            } catch (IOException e) {
                //moving file failed.
                //System.out.println("unable to move the file " + source.toString());
                outputtext("unable to move the file " + source.toString()+"\n");
            }
        } else {
                //System.out.println("same filename and same size then bypass move " + source.toString());
                outputtext("same filename and same size then bypass move " + source.toString()+"\n");
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        //System.out.print("visitFile: ");
        outputtext("visitFile: ");
        if (IsHidden(file)) {
            //System.out.print("Hidden ");
            outputtext("Hidden ");
        }
        if (attrs.isSymbolicLink()) {
            //System.out.format("Symbolic link: %s ", file);
            outputtext("Symbolic link: " + file);
        } else if (attrs.isRegularFile()) {

            //System.out.format("Regular file: %s ", file);
            outputtext("Regular file: " + file);

        } else {
            //System.out.format("Other: %s ", file);
            outputtext("Other:  " + file);
        }
        String name = file.getFileName().toString();
        String ext = "";
        String dest = destination.toString() + "/" + toDate(attrs.lastModifiedTime()).get(Calendar.YEAR);
        dest = dest + "/" + toDate(attrs.lastModifiedTime()).get(Calendar.MONTH);
        if (name.contains(".")) {

            ext = name.substring(name.lastIndexOf(".") + 1);
            dest = dest + "/" + ext;
        }

        String output = " (" + attrs.size() + " bytes, lastModifiedTime: " + "Years: " + toDate(attrs.lastModifiedTime()).get(Calendar.YEAR) + " Month: " + toDate(attrs.lastModifiedTime()).get(Calendar.MONTH) + " extension " + ext + " )";
        
        outputtext(output + "\n");

        createdir(dest);
        move(file, dest);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        //System.out.print("vistiFileFailed: ");
        //System.err.println(exc);
        outputtext("visit File Failed: " + exc + "\n");
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        //System.out.print("postVisitDirectory: ");
        //System.out.format("Directory: %s%n", dir);
        outputtext("postVisitDirectory: " + dir + "\n");
        return CONTINUE;
    }

}
