package de.tr82.directory.splitter.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DirectorySplitter {
    private final Path sourceBasePath;
    private final Path targetBasePath;
    private final String bucketNamePrefix;
    private final long bucketSizeMax;

    private int currentBucketNo = 1;
    private long currentBucketSpaceLeft;

    public DirectorySplitter(final Path sourceBaseFolder, final Path targetBaseFolder, final String bucketNamePrefix,
                             final long bucketSizeMax) {
        this(sourceBaseFolder, targetBaseFolder, bucketNamePrefix, bucketSizeMax, bucketSizeMax);
    }

    public DirectorySplitter(final Path sourceBaseFolder, final Path targetBaseFolder, final String bucketNamePrefix,
                             final long firstBucketSpaceLeft, final long bucketSizeMax) {
        this.sourceBasePath = sourceBaseFolder;
        this.targetBasePath = targetBaseFolder;
        this.bucketNamePrefix = bucketNamePrefix;
        this.bucketSizeMax = bucketSizeMax;
        this.currentBucketSpaceLeft = firstBucketSpaceLeft;
    }

    public void run() throws IOException {
        Files.walk(sourceBasePath)
                .filter(path -> Files.isRegularFile(path))
                .sorted()
                .forEach(this::handlePath);
    }

    private void handlePath(Path sourcePath) {
        final long currentFileSize = sourcePath.toFile().length();
        if (currentBucketSpaceLeft - currentFileSize < 0) {
            currentBucketSpaceLeft = bucketSizeMax;
            currentBucketNo++;
        }

        Path targetPath = targetBasePath.resolve(bucketNamePrefix + String.format("%03d", currentBucketNo));
        Path targetFile = targetPath.resolve(sourceBasePath.relativize(sourcePath));

        try {
            targetPath.toFile().mkdir();
            Files.move(sourcePath, targetFile, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentBucketSpaceLeft -= currentFileSize;
    }
}
