package task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver {
    class MatchTask implements Callable<Map<String, ArrayList<SourceLocation>>> {
        private final Matcher m_Matcher;
        private final String m_Block;

        public MatchTask(Matcher matcher, String block) {
            m_Matcher = matcher;
            m_Block = block;
        }

        @Override
        public Map<String, ArrayList<SourceLocation>> call() throws Exception {
            return m_Matcher.match(m_Block);
        }

    }

    private final List<String> m_Names;
    private int m_BlockLines = 1000;

    Driver(List<String> names, int blockLines) {
        m_Names = names;
        m_BlockLines = blockLines;
    }

    public void run(String filename) throws IOException, ExecutionException, InterruptedException {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(filename).getFile()));
        } catch (FileNotFoundException e) {
            System.err.printf("Resource %s not found\n", filename);
            return;
        } catch (Exception e) {
            System.err.printf("Unable to open file %s\n", filename);
            return;
        }

        Aggregator aggregator = new Aggregator();
        Matcher m = new Matcher(new HashSet<String>(m_Names));

        ExecutorService executor = Executors.newCachedThreadPool();

        StringBuilder block = new StringBuilder();
        int block_start_line = 0;
        int block_start_char = 0;
        int lines_read = 0;
        int chars_read = 0;

        try {
            String line = reader.readLine();
            while (line != null) {
                block.append(line);
                block.append("\n");
                lines_read++;
                // TODO: Handle CRLF line endings
                chars_read += line.length() + 1;

                if (lines_read % m_BlockLines == 0) {
                    aggregator.append(block_start_line, block_start_char,
                            executor.submit(new MatchTask(m, block.toString())));
                    block = new StringBuilder();
                    block_start_line = lines_read;
                    block_start_char = chars_read;
                }

                line = reader.readLine();
            }
        } finally {
            reader.close();
        }

        aggregator.append(block_start_line, block_start_char,
                executor.submit(new MatchTask(m, block.toString())));

        aggregator.aggregate();

        executor.shutdown();

        aggregator.print();
    }
}
