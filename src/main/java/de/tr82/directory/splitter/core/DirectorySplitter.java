package de.tr82.directory.splitter.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

public class DirectorySplitter {
    private final Path sourceBasePath;
    private final Path targetBasePath;
    private final String bucketNamePrefix;
    private final long bucketSizeMax;
    private final boolean dryRun;

    private int currentBucketNo = 1;
    private long currentBucketSpaceLeft;

    private Consumer<String> logger = System.out::println;

    public DirectorySplitter(final Path sourceBaseFolder, final Path targetBaseFolder, final String bucketNamePrefix,
                             final int firstBucketIndex, final long bucketSizeMax) {
        this(sourceBaseFolder, targetBaseFolder, bucketNamePrefix, firstBucketIndex, bucketSizeMax, bucketSizeMax,
                false);
    }

    public DirectorySplitter(final Path sourceBaseFolder, final Path targetBaseFolder, final String bucketNamePrefix,
                             final int firstBucketIndex, final long firstBucketSpaceLeft, final long bucketSizeMax) {
        this(sourceBaseFolder, targetBaseFolder, bucketNamePrefix, firstBucketIndex, firstBucketSpaceLeft, bucketSizeMax, false);
    }

    public DirectorySplitter(final Path sourceBaseFolder, final Path targetBaseFolder, final String bucketNamePrefix,
                             final int firstBucketIndex, final long firstBucketSpaceLeft, final long bucketSizeMax,
                             final boolean dryRun) {
        this.sourceBasePath = sourceBaseFolder;
        this.targetBasePath = targetBaseFolder;
        this.bucketNamePrefix = bucketNamePrefix;
        this.bucketSizeMax = bucketSizeMax;
        this.currentBucketNo = firstBucketIndex;
        this.currentBucketSpaceLeft = firstBucketSpaceLeft;
        this.dryRun = dryRun;
    }

    public void setLogger(final Consumer<String> logger) {
        this.logger = logger;
    }

    public void run() throws IOException {
        printOperationInfo();

        Files.walk(sourceBasePath)
                .filter(path -> Files.isRegularFile(path))
                .sorted()
                .forEach(this::handlePath);
        if (!dryRun && Files.list(sourceBasePath).findAny().isPresent()) {
            Files.walk(sourceBasePath).forEach((Path path) -> path.toFile().delete());
        }

        printOperationResult();
    }

    private void printOperationInfo() {
        logger.accept("----------------------");
        logger.accept("| Directory Splitter |");
        logger.accept("----------------------");
        logger.accept("");
        logger.accept("Selected source directory       : " + sourceBasePath.toString());
        logger.accept("Selected target directory       : " + targetBasePath.toString());
        logger.accept("Name of buckets                 : " + bucketNamePrefix + "###");
        logger.accept("Starting with bucket            : " + bucketNamePrefix + String.format("%03d", currentBucketNo));
        logger.accept("Remaining space in first bucket : " + formatSpace(currentBucketSpaceLeft));
        logger.accept("Max size of following buckets   : " + formatSpace(bucketSizeMax));
        logger.accept("");

        if (dryRun) {
            logger.accept("Dry run selected - the following operations are just simulated!");
            logger.accept("");
        }
    }

    private String formatSpace(double size) {
        double result = 0.0;
        if (size >= 1024 * 1024 * 1024) {
            return String.format("%.2f GB", size / (1024d * 1024d * 1024d));
        } else if (size >= 1024 * 1024) {
            return String.format("%.2f MB", size / (1024d * 1024d));
        } else if (size >= 1024) {
            return String.format("%.2f KB", size / 1024d);
        } else {
            return String.format("%.0f Bytes", size);
        }
    }

    private void handlePath(Path sourcePath) {
        final long currentFileSize = sourcePath.toFile().length();

        if (currentBucketSpaceLeft - currentFileSize < 0) {
            currentBucketSpaceLeft = bucketSizeMax;
            currentBucketNo++;
        }

        Path targetPath = targetBasePath.resolve(bucketNamePrefix + String.format("%03d", currentBucketNo));
        Path targetFile = targetPath.resolve(sourceBasePath.relativize(sourcePath));

        if (!dryRun) {
            try {
                targetFile.getParent().toFile().mkdirs();
                Files.move(sourcePath, targetFile, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        logger.accept(sourcePath.toString() + " --> " + targetFile.toString());
        currentBucketSpaceLeft -= currentFileSize;
    }

    private void printOperationResult() {
        logger.accept("");
        if (dryRun) {
            logger.accept("Simulation finished!");
        } else {
            logger.accept("Operation finished!");
        }
        logger.accept("");
    }
}
