package de.tr82.directory.splitter.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DirectorySplitter {
    private final Path sourceBasePath;
    private final Path targetBasePath;
    private final long bucketSizeMax;

    private int currentBucketNo = 1;
    private long currentBucketSpaceLeft;

    public DirectorySplitter(final Path sourceFolder, final Path targetFolder, final long bucketSizeMax) {
        this.sourceBasePath = sourceFolder;
        this.targetBasePath = targetFolder;
        this.bucketSizeMax = bucketSizeMax;
        this.currentBucketSpaceLeft = bucketSizeMax;
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

        Path targetPath = targetBasePath.resolve("bucket_" + String.format("%03d", currentBucketNo));
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
