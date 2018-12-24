package de.tr82.directory.splitter.core;

import de.tr82.junit5.extensions.TemporaryFolder;
import de.tr82.junit5.extensions.TemporaryFolderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TemporaryFolderExtension.class)
public class DirectorySplitterTest {

    private TemporaryFolder tempSourceFolder;
    private TemporaryFolder tempTargetFolder;

    @Test
    public void testWithThreeFilesIntoSameBucket() throws IOException {
        File sourceFile1 = tempSourceFolder.createBinaryFile("file1.bin", 333);
        File targetFile1 = new File(tempTargetFolder.getPath().resolve("bucket_001").toFile(), "file1.bin");
        File sourceFile2 = tempSourceFolder.createBinaryFile("file2.bin", 333);
        File targetFile2 = new File(tempTargetFolder.getPath().resolve("bucket_001").toFile(), "file2.bin");
        File sourceFile3 = tempSourceFolder.createBinaryFile("file3.bin", 333);
        File targetFile3 = new File(tempTargetFolder.getPath().resolve("bucket_001").toFile(), "file3.bin");

        DirectorySplitter splitter = new DirectorySplitter(tempSourceFolder.getPath(), tempTargetFolder.getPath(), 1000);
        splitter.run();

        assertFalse(sourceFile1.exists());
        assertFalse(sourceFile2.exists());
        assertFalse(sourceFile3.exists());
        assertTrue(targetFile1.exists());
        assertTrue(targetFile2.exists());
        assertTrue(targetFile3.exists());
    }

    @Test
    public void testWithThreeFilesIntoSeparateBucketsEach() throws IOException {
        File sourceFile1 = tempSourceFolder.createBinaryFile("file1.bin", 1000);
        File targetFile1 = new File(tempTargetFolder.getPath().resolve("bucket_001").toFile(), "file1.bin");
        File sourceFile2 = tempSourceFolder.createBinaryFile("file2.bin", 1000);
        File targetFile2 = new File(tempTargetFolder.getPath().resolve("bucket_002").toFile(), "file2.bin");
        File sourceFile3 = tempSourceFolder.createBinaryFile("file3.bin", 1000);
        File targetFile3 = new File(tempTargetFolder.getPath().resolve("bucket_003").toFile(), "file3.bin");

        DirectorySplitter splitter = new DirectorySplitter(tempSourceFolder.getPath(), tempTargetFolder.getPath(), 1000);
        splitter.run();

        assertFalse(sourceFile1.exists());
        assertFalse(sourceFile2.exists());
        assertFalse(sourceFile3.exists());
        assertTrue(targetFile1.exists());
        assertTrue(targetFile2.exists());
        assertTrue(targetFile3.exists());
    }
}
