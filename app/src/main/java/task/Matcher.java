package task;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class Matcher {
    private final Set<String> m_allNames;

    public Matcher(Set<String> names) {
        m_allNames = names;
    }

    private boolean isSeparator(char c) {
        return !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
    }

    public Map<String, ArrayList<SourceLocation>> match(String s) throws InvalidParameterException {
        if (s == null)
            throw new InvalidParameterException("s");

        Map<String, ArrayList<SourceLocation>> result = new HashMap<String, ArrayList<SourceLocation>>();

        int b_idx = 0;
        int row = 0;

        for (int idx = 0; idx < s.length(); ++idx) {
            char c = s.charAt(idx);

            if (!isSeparator(c)) {
                continue;
            }

            String word = s.substring(b_idx, idx);
            processWord(result, word, row, b_idx);

            if (c == '\n')
                row++;

            b_idx = idx + 1;
        }

        String word = s.substring(b_idx, s.length());
        processWord(result, word, row, b_idx);

        return result;
    }

    private void processWord(Map<String, ArrayList<SourceLocation>> result, String word, int row, int idx) {
        if (m_allNames.contains(word)) {

            if (!result.containsKey(word))
                result.put(word, new ArrayList<>());

            result.get(word).add(new SourceLocation(row, idx));
        }
    }
}
