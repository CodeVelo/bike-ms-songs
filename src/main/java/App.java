import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App
{
    private final String SRC = "Bike-MS-2019";
    private final String DEST  = "ToPhone";
    private final String KILL_FILE = "mp3-kill-file.txt";
    @SuppressWarnings("SpellCheckingInspection")
    private final String DROPBOX_ENV = "DROPBOX";
    private String sourceDir;
    private String destinationDir;
    private String killFile;
    private List killFileContent;

    public static void main(String[] args)
    {
        App app = new App();
        System.out.println(app.sourceDir);
        System.out.println(app.destinationDir);
        System.out.println(app.killFile);
        app.loadKillFile();
        try (Stream<Path> paths = Files.list(Paths.get(app.sourceDir)))
        {
//            paths.forEach(filePath -> System.out.println(filePath));
            paths
                .filter(Files::isRegularFile)
                .filter(filePath -> filePath.toString().contains(".mp3"))
                .filter(filePath -> !(app.killFileContent.contains(filePath.getFileName().toString())))
                .forEach(filePath ->
                {
                    System.out.println(filePath);
                    copyFiles(filePath, app.destinationDir);
                });
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

    public App()
    {
        @SuppressWarnings("SpellCheckingInspection")
        String dropboxDir = System.getenv(DROPBOX_ENV);
        if (Strings.isNullOrEmpty(dropboxDir))
        {
            throw new RuntimeException("Can't find DROPBOX environment variable");
        }
        sourceDir = String.format("%s%s%s", dropboxDir, File.separator, SRC);
        destinationDir = String.format("%s%s%s", dropboxDir, File.separator, DEST);
        killFile = String.format("%s%s%s%s%s%s%s",
                dropboxDir,
                File.separator,
                "Utils",
                File.separator,
                "script",
                File.separator,
                KILL_FILE);
    }

    public App loadKillFile()
    {
        try
        {
            killFileContent = Files.readAllLines(new File(killFile).toPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    private static void copyFiles(final Path source, final String destination)
    {
        try
        {
            Files.copy(source,
                    Paths.get(destination + File.separator + source.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
