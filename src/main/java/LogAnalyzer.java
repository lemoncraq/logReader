import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogAnalyzer {
    public static void main(String[] args) throws Exception {
        double minAvailability = Double.parseDouble(args[0]);
        double maxResponseTime = Long.parseLong(args[1]);
        //TODO: Для сборки переделать на InputStreamReader(System.in)
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/access.log"));

        List<LogEntry> logEntries = new ArrayList<>();

        String line;

        while ((line = reader.readLine()) != null) {
//            LogEntry entry
            System.out.println(line);
        }
        System.out.println(minAvailability);
        System.out.println(maxResponseTime);
    }
}
