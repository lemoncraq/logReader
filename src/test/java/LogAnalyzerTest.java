

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class LogAnalyzerTest {
    @Test
    public void testParseLogEntry_ValidLogEntry() throws ParseException {
        String logEntry = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0\n";
        LogEntry entry = LogAnalyzer.parseLogEntry(logEntry);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
        Date expectedDate = sdf.parse("14/06/2017:16:47:02");

        assertNotNull(entry);
        assertEquals(expectedDate, entry.getDateTime());
        assertEquals(200, entry.getResponseCode());
        assertEquals(44.510983, entry.getResponseTime(), 0.000001);
    }

    @Test
    public void testParseLogEntry_InvalidLogEntry(){
        String invalidLogEntry = "INVALID LOG";
        LogEntry logEntry = LogAnalyzer.parseLogEntry(invalidLogEntry);
        assertNull(logEntry);
    }
}
