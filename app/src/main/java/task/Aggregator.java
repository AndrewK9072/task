package task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Aggregator {
    private class BlockMatchResult {
        private int m_BlockLineStart;
        private int m_BlockCharStart;
        private Future<Map<String, ArrayList<SourceLocation>>> m_Matches;
    
        public BlockMatchResult(int blockLineStart, int blockCharStart,
                Future<Map<String, ArrayList<SourceLocation>>> matches) {
            this.m_BlockLineStart = blockLineStart;
            this.m_BlockCharStart = blockCharStart;
            this.m_Matches = matches;
        }
    
        public Future<Map<String, ArrayList<SourceLocation>>> getMatches() {
            return m_Matches;
        }
    
        public int getBlockCharStart() {
            return m_BlockCharStart;
        }
    
        public int getBlockLineStart() {
            return m_BlockLineStart;
        }
    }

    private List<BlockMatchResult> m_blockMatches;
    private Map<String, ArrayList<SourceLocation>> m_aggregatedMatches;

    public Aggregator() {
        m_blockMatches = new ArrayList<>();
        m_aggregatedMatches = new HashMap<>();
    }

    void append(int blockLineStart, int blockCharStart, Future<Map<String, ArrayList<SourceLocation>>> matches) {
        m_blockMatches.add(new BlockMatchResult(blockLineStart, blockCharStart, matches));
    }

    public void aggregate() throws InterruptedException, ExecutionException {
        m_aggregatedMatches = new HashMap<>();
        for (BlockMatchResult blockMatch : m_blockMatches) {
            blockMatch.getMatches().get().forEach((key, slocList) -> {
                if (slocList.isEmpty())
                    throw new RuntimeException("Empty slocList");

                if (!m_aggregatedMatches.containsKey(key)) {
                    m_aggregatedMatches.put(key, new ArrayList<>());
                }

                slocList.forEach(sloc -> {
                    m_aggregatedMatches.get(key).add(new SourceLocation(sloc.getLine() + blockMatch.getBlockLineStart(),
                            blockMatch.getBlockCharStart() + sloc.getChar()));
                });
            });
        }
    }

    public void print() {
        m_aggregatedMatches.forEach((key, slocList) -> {
            System.out.printf("%s --> [", key);

            for (int i = 0; i < slocList.size(); ++i) {
                System.out.print(slocList.get(i));

                if (i < slocList.size() - 1)
                    System.out.print(", ");
            }

            System.out.print("]\n");
        });
    }
}
