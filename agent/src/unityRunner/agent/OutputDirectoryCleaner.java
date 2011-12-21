package unityRunner.agent;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * class to clean output directories
 */
public class OutputDirectoryCleaner extends DirectoryWalker {

    private LogParser logger;
    
    public OutputDirectoryCleaner(LogParser logger) {
        super();
        this.logger = logger;
    }

    public List clean(File startDirectory) {

        logger.logActivityStart("Cleanup");
        logger.log("Cleaning .meta and .svn files after build...");

        List results = new ArrayList();
        try {
            walk(startDirectory, results);
        } catch (IOException e) {
            logger.logException(e);
        }

        logger.logActivityEnd("Cleanup");

        return results;
    }

    protected boolean handleDirectory(File directory, int depth, Collection results) {
        // delete svn directories and then skip
        if (".svn".equals(directory.getName())) {
            logger.log("Removing directory: " + directory.getPath());
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                logger.logException(e);
            }
            return false;
        } else {
            return true;
        }

    }

    protected void handleFile(File file, int depth, Collection results) {
        // delete file and add to list of deleted
        if (file.getName().endsWith(".meta")) {
            logger.log("Removing file: " + file.getPath());
            file.delete();
            results.add(file);
        }
    }
}