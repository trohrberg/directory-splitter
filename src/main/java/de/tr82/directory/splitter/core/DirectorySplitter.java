package de.tr82.directory.splitter.core;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectorySplitter {

    private final Path sourceBasePath;
    private final Path targetBasePath;
    private final String chunkNamePrefix;
    private final long firstChunkSize;
    private final long maxChunkSize;

    private final boolean dryRun;

    private final OperationLogger logger;

    private long currentChunkSize = 0;
    private int chunkNum = 1;

    public DirectorySplitter(final String sourceBasePath, final String targetBasePath, final String chunkNamePrefix,
                             final long firstChunkSize, final long maxChunkSize, final boolean dryRun,
                             final OperationLogger logger) {
        this.sourceBasePath = FileSystems.getDefault().getPath(sourceBasePath);
        this.targetBasePath = FileSystems.getDefault().getPath(targetBasePath);
        this.chunkNamePrefix = chunkNamePrefix;
        this.firstChunkSize = firstChunkSize;
        this.maxChunkSize = maxChunkSize;
        this.dryRun = dryRun;
        this.logger = logger;
    }

    public void run() throws IOException {
        printOperationInfo();
        Files.walkFileTree(sourceBasePath, new MovingFileVisitor());
        printOperationCompleted();
    }

    private void printOperationInfo() {
        logger.log("----------------------");
        logger.log("| Directory Splitter |");
        logger.log("----------------------");
        logger.log("");
        logger.log("Selected source directory    : " + sourceBasePath.toString());
        logger.log("Selected target directory    : " + targetBasePath.toString());
        logger.log("Naming convention of buckets : " + chunkNamePrefix + "###");
        logger.log("");

        if (dryRun) {
            logger.log("DRY RUN - All operations are just simulated!");
            logger.log("The following operations would be performed if executed without 'dryRun' flag:");
            logger.log("");
        }
    }

    private void printOperationCompleted() {
        logger.log("");

        if (dryRun) {
            logger.log("Simulation completed: " + chunkNum + " buckets would have been created.");
        } else {
            logger.log("Operation completed: " + chunkNum + " buckets have been created.");
        }
    }

    public interface OperationLogger {
        void log(final String message);
    }

    private class MovingFileVisitor extends SimpleFileVisitor<Path> {
        private long localMaxChunkSize = firstChunkSize;
        private Path targetPath = targetBasePath.resolve(String.format(chunkNamePrefix + "%03d", chunkNum));

        @Override
        public FileVisitResult visitFile(Path sourcefile, BasicFileAttributes attrs) throws IOException {
            long fileSize = sourcefile.toFile().length();

            if (currentChunkSize + fileSize > localMaxChunkSize) {
                localMaxChunkSize = maxChunkSize;
                currentChunkSize = 0;
                chunkNum++;
                targetPath = targetBasePath.resolve(String.format(chunkNamePrefix + "%03d", chunkNum));
            }

            Path targetFile = targetPath.resolve(sourceBasePath.relativize(sourcefile));

            if (dryRun) {
                logger.log(sourcefile.toString() + " --> " + targetFile.toString());
            } else {
                Files.move(sourcefile, targetFile, StandardCopyOption.ATOMIC_MOVE);
            }

            currentChunkSize += fileSize;

            return super.visitFile(sourcefile, attrs);
        }
    }
}
