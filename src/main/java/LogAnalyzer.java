import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyzer {
    private static final String logPattern =
            //IP
            "^(\\S+)" +
                    " - - " +
                    //DateTime
                    "\\[(\\d{2}/\\d{2}/\\d{4}:\\d{2}:\\d{2}:\\d{2} [+-]\\d{4})]" +
                    " \".*\" " +
                    //Response code
                    "(\\d{3})" +
                    " \\d+ " +
                    //Response time
                    "(\\d+\\.\\d+)";
    private static final Pattern pattern = Pattern.compile(logPattern);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");

    public static void main(String[] args) throws Exception {
        double minAvailability = 0;
        double maxResponseTime = 0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-u":
                    minAvailability = Double.parseDouble(args[++i]);
                    break;
                case "-t":
                    maxResponseTime = Long.parseLong(args[++i]);
                    break;
                default:
                    System.out.println("Используйте java -jar analyze.jar -u <minAvailability> -t <maxResponseTime>");
                    return;
            }
        }
        if (minAvailability == 0 || maxResponseTime == 0) {
            System.out.println("Используйте java -jar analyze.jar -u <minAvailability> -t <maxResponseTime>");
            return;
        }


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Date startInterval = null;
        int totalRequests = 0;
        int failedRequests = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            LogEntry logEntry = parseLogEntry(line);
            if (logEntry != null) {
                totalRequests++;
                boolean isFailure = logEntry.getResponseCode() >= 500 || logEntry.getResponseTime() >= maxResponseTime;
                if (isFailure) {
                    failedRequests++;
                }

                double availability = 100.0 * (totalRequests - failedRequests) / totalRequests;

                if (availability < minAvailability) {
                    if (startInterval == null) {
                        startInterval = logEntry.getDateTime();
                    } else {
                        System.out.println("Interval: " +
                                sdf.format(startInterval) +
                                " - " +
                                sdf.format(logEntry.getDateTime()) +
                                " Availability: " +
                                availability + "%");

                        startInterval = null;
                        totalRequests = 0;
                        failedRequests = 0;
                    }
                }
            }
        }
    }

    public static LogEntry parseLogEntry(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            try {
                Date dateTime = sdf.parse(matcher.group(2));
                int responseCode = Integer.parseInt(matcher.group(3));
                double responseTime = Double.parseDouble(matcher.group(4));

                return new LogEntry(dateTime, responseCode, responseTime);
            } catch (ParseException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
