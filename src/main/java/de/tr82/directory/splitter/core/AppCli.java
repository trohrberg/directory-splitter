package de.tr82.directory.splitter.core;

import java.io.IOException;

public class AppCli {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println(
                    "Missing parameters. Please specify source and target base directories and plus chunk size.");
            return;
        }

        final String sourceBasePath = args[0];
        final String targetBasePath = args[1];

        DirectorySplitter splitter = new DirectorySplitter(sourceBasePath, targetBasePath, "RAWBLU_",
                1, parseChunkSize("1.0G"), parseChunkSize(args[2]), true, System.out::println);
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
