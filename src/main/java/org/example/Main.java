package org.example;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static class ProcessReader implements Callable {
        private InputStream inputStream;

        public ProcessReader(InputStream in) {
            this.inputStream = in;
        }

        @Override
        public Object call() throws Exception {
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.toList());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {


        System.out.println(Main.class.getResourceAsStream("src/main/resources/script.sh"));
        boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("windows");

        // ProcessBuilder builder = new ProcessBuilder();
        String filename;
        if(isWin) {
            System.out.println("========= WINDOWS");
            filename = "src/main/resources/script.bat";
            // builder.command(System.getProperty("user.dir") + "\\scripts\\script.sh");
        } else {
            System.out.println("========= UNIX");
            filename = "src/main/resources/script.sh";
            // builder.command("sh", "-c", System.getProperty("user.dir") + "/scripts/script.sh");
        }

        ProcessBuilder builder = new ProcessBuilder(filename);
        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            // Future<List<String>> future = pool.submit(task);

            // List<String> results = future.get();

            /*for(String s: results) {
                System.out.println(s);
            }*/

            int exitCode = process.waitFor();
            System.out.println("======> EXIT CODE: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }


        // ======================
/*            Process process = Runtime.getRuntime().exec("scripts/script.bat");
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if(exitVal == 0) {
                System.out.println("========> Success");
                System.out.println(output);
                System.exit(0);
            } else {
                System.out.println("========> ERROR");
            }*/
    }
}