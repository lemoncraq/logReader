import org.junit.Test;

import static org.junit.Assert.*;

public class LogAnalyzerEdgeCasesTest {
    @Test
    public void testParseLogEntry_EmptyLogEntry() {
        String emptyLogEntry = "";
        LogEntry logEntry = LogAnalyzer.parseLogEntry(emptyLogEntry);
        assertNull(logEntry);
    }

    @Test
    public void testParseLogEntry_MalformedLogEntry() {
        String malformedLogEntry = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 500 - - \"-\" \"@list-item-updater\" prio:0\n";
        LogEntry logEntry = LogAnalyzer.parseLogEntry(malformedLogEntry);
        assertNull(logEntry);
    }
}
