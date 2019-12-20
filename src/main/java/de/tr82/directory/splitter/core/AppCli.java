package de.tr82.directory.splitter.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AppCli {

    public static void main(String[] args) throws IOException {
        if (args.length != 6) {
            System.out.println(
                    "Missing parameters. Please specify source and target base directories and plus chunk size.");
            return;
        }

        final Path sourceBasePath = new File(args[0]).toPath();
        final Path targetBasePath = new File(args[1]).toPath();
        final int firstBucketIndex = Integer.parseInt(args[2]);
        final long firstBucketSpaceLef = parseChunkSize(args[3]);
        final long bucketSizeMax = parseChunkSize(args[4]);
        final boolean dryRun = Boolean.parseBoolean(args[5]);

        DirectorySplitter splitter = new DirectorySplitter(sourceBasePath, targetBasePath, "RAWBLU_",
                firstBucketIndex, firstBucketSpaceLef, bucketSizeMax, dryRun);
        splitter.run();
    }

    private static long parseChunkSize(final String chunkSize) {
        if (chunkSize.endsWith("G")) {
            return Math.round(Double.valueOf(chunkSize.substring(0, chunkSize.length() - 1)) * 1024 * 1024 * 1024);
        } else if (chunkSize.endsWith("M")) {
            return Math.round(Double.valueOf(chunkSize.substring(0, chunkSize.length() - 1)) * 1024 * 1024);
        } else if (chunkSize.endsWith("K")) {
            return Math.round(Double.valueOf(chunkSize.substring(0, chunkSize.length() - 1)) * 1024);
        } else {
            return Long.valueOf(chunkSize.substring(0, chunkSize.length() - 1));
        }
    }
}
