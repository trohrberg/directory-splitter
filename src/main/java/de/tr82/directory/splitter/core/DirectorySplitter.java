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

    private boolean dryRun;

    private long currentChunkSize = 0;
    private int chunkNum = 1;

    public DirectorySplitter(final String sourceBasePath, final String targetBasePath, final String chunkNamePrefix,
                             final long firstChunkSize, final long maxChunkSize, final boolean dryRun) {
        this.sourceBasePath = FileSystems.getDefault().getPath(sourceBasePath);
        this.targetBasePath = FileSystems.getDefault().getPath(targetBasePath);
        this.chunkNamePrefix = chunkNamePrefix;
        this.firstChunkSize = firstChunkSize;
        this.maxChunkSize = maxChunkSize;
        this.dryRun = dryRun;
    }

    public void run() throws IOException {
        printOperationInfo();
        Files.walkFileTree(sourceBasePath, new MovingFileVisitor());
        printOperationCompleted();
    }

    private void printOperationInfo() {
        System.out.println("----------------------");
        System.out.println("| Directory Splitter |");
        System.out.println("----------------------");
        System.out.println();
        System.out.println("Selected source directory    : " + sourceBasePath.toString());
        System.out.println("Selected target directory    : " + targetBasePath.toString());
        System.out.println("Naming convention of buckets : " + chunkNamePrefix + "###");
        System.out.println();

        if (dryRun) {
            System.out.println("DRY RUN - All operations are just simulated!");
            System.out.println("The following operations would be performed if executed without 'dryRun' flag:");
            System.out.println();
        }
    }

    private void printOperationCompleted() {
        System.out.println();

        if (dryRun) {
            System.out.println("Simulation completed: " + chunkNum + " buckets would have been created.");
        } else {
            System.out.println("Operation completed: " + chunkNum + " buckets have been created.");
        }
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
                System.out.println(sourcefile.toString() + " --> " + targetFile.toString());
            } else {
                Files.move(sourcefile, targetFile, StandardCopyOption.ATOMIC_MOVE);
            }

            currentChunkSize += fileSize;

            return super.visitFile(sourcefile, attrs);
        }
    }
}
