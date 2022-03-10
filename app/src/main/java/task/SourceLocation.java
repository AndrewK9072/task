package task;

public class SourceLocation {
    private int m_Line;
    private int m_Char;

    public SourceLocation(int line, int ch) {
        m_Line = line;
        m_Char = ch;
    }

    public int getLine() {
        return m_Line;
    }
    
    public int getChar() {
        return m_Char;
    }

    public String toString() {
        return String.format("[lineOffset=%d, charOffset=%d]", m_Line, m_Char);
    }
}
