package de.tr82.directory.splitter.core.old;

import java.nio.file.Path;
import java.util.function.Consumer;

public class FileSetHandler implements Consumer<Path> {
    private final Path sourceBasePath;
    private final Path targetBasePath;
    private final String chunkNamePrefix;
    private Path targetPath;
    private final long maxChunkSize;
    private long currentMaxChunkSize;
    private long currentChunkSize;
    private int chunkNum;
    private final MoveFileOperation moveFileOperation;

    public FileSetHandler(final Path sourceBasePath, final Path targetBasePath, final String chunkNamePrefix,
                          final long maxFirstChunkSize, final long maxChunkSize, final int firstChunkIndex,
                          final MoveFileOperation moveFileOperation) {
        this.sourceBasePath = sourceBasePath;
        this.targetBasePath = targetBasePath;
        this.chunkNamePrefix = chunkNamePrefix;
        this.currentChunkSize = maxFirstChunkSize;
        this.maxChunkSize = maxChunkSize;
        this.chunkNum = firstChunkIndex;
        this.moveFileOperation = moveFileOperation;
    }

    @Override
    public void accept(Path path) {
        long fileSize = path.toFile().length();

        if (currentChunkSize + fileSize > currentMaxChunkSize) {
            currentMaxChunkSize = maxChunkSize;
            currentChunkSize = 0;
            chunkNum++;
            targetPath = targetBasePath.resolve(String.format(chunkNamePrefix + "%03d", chunkNum));
        }

        Path targetFile = targetPath.resolve(sourceBasePath.relativize(path));
        moveFileOperation.moveFile(path, targetFile);

        currentChunkSize += fileSize;
    }
}
