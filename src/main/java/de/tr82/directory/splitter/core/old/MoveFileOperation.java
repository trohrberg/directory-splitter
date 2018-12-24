package de.tr82.directory.splitter.core.old;

import java.nio.file.Path;

public interface MoveFileOperation {
    public void moveFile(final Path source, final Path target);
}
