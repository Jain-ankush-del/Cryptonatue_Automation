package framework.utils;

import framework.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineUtils {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CommandLineUtils() {}

    public static void execCommand(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static Process execCommandAndReturnProcess(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static List<String> execCommandAndGetOutputAsList(String command) {
        Process process;
        List<String> outputLines = new ArrayList<>();

        try {
            process = execCommandAndReturnProcess(command);
            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputLine;
            while ((outputLine = stdOutput.readLine()) != null) {
                outputLines.add(outputLine);
            }
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }

        return outputLines;
    }
}