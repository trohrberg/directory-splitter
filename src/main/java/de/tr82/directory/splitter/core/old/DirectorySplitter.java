package de.tr82.directory.splitter.core.old;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class DirectorySplitter {

    private final Path sourceBasePath;
    private final Path targetBasePath;
    private final String chunkNamePrefix;
    private final int firstChunkIndex;
    private final long firstChunkSize;
    private final long maxChunkSize;
    private final boolean dryRun;
    private final Consumer<String> logConsumer;

    public DirectorySplitter(final String sourceBasePath, final String targetBasePath, final String chunkNamePrefix,
                             final int firstChunkIndex, final long firstChunkSize, final long maxChunkSize,
                             final boolean dryRun, final Consumer<String> logConsumer) {
        this.sourceBasePath = FileSystems.getDefault().getPath(sourceBasePath);
        this.targetBasePath = FileSystems.getDefault().getPath(targetBasePath);
        this.chunkNamePrefix = chunkNamePrefix;
        this.firstChunkIndex = firstChunkIndex;
        this.firstChunkSize = firstChunkSize;
        this.maxChunkSize = maxChunkSize;
        this.dryRun = dryRun;
        this.logConsumer = logConsumer;
    }

    public void run() throws IOException {
        checkParameters();
        printOperationInfo();
        processFiles();
        printOperationCompleted();
    }

    private void checkParameters() {
        if (sourceBasePath == null || sourceBasePath.toString().isEmpty()) {
            throw new IllegalArgumentException("Source path cannot be null or empty.");
        }

        if (targetBasePath == null || targetBasePath.toString().isEmpty()) {
            throw new IllegalArgumentException("Target path cannot be null or empty.");
        }

        if (chunkNamePrefix == null || chunkNamePrefix.isEmpty()) {
            throw new IllegalArgumentException("Chunk name prefix cannot be null or empty.");
        }

        if (firstChunkIndex < 0) {
            throw new IllegalArgumentException("First chunk index cannot be less then 0.");
        }
    }

    private void printOperationInfo() {
        logConsumer.accept("----------------------");
        logConsumer.accept("| Directory Splitter |");
        logConsumer.accept("----------------------");
        logConsumer.accept("");
        logConsumer.accept("Selected source directory    : " + sourceBasePath.toString());
        logConsumer.accept("Selected target directory    : " + targetBasePath.toString());
        logConsumer.accept("Naming convention of buckets : " + chunkNamePrefix + "###");
        logConsumer.accept("Starting with bucket         : " + chunkNamePrefix + String.format("%03d", firstChunkIndex));
        logConsumer.accept("");

        if (dryRun) {
            logConsumer.accept("DRY RUN - All operations are just simulated!");
            logConsumer.accept("The following operations would be performed if executed without 'dryRun' flag:");
            logConsumer.accept("");
        }
    }

    private void processFiles() throws IOException {
        FileSetHandler handler = createFileSetHandler();
        Files.walk(sourceBasePath).filter(path -> Files.isRegularFile(path)).sorted().forEach(handler);
    }

    private FileSetHandler createFileSetHandler() {
        MoveFileOperation operation = null;
        if (dryRun) {
            operation = (Path source, Path target)
                    -> System.out.println(source.toString() + " --> " + target.toString());
        } else {
            operation = (Path source, Path target)
                    -> System.out.println(source.toString() + " --> " + target.toString());
        }

        return new FileSetHandler(sourceBasePath, targetBasePath, chunkNamePrefix, firstChunkSize, maxChunkSize,
                firstChunkIndex, operation);
    }

    private void printOperationCompleted() {
        logConsumer.accept("");

        if (dryRun) {
            logConsumer.accept("Simulation completed!");
        } else {
            logConsumer.accept("Operation completed!");
        }
    }
}
