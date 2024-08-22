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
        double minAvailability = Double.parseDouble(args[0]);
        double maxResponseTime = Long.parseLong(args[1]);

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

    private static LogEntry parseLogEntry(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            try {
                Date dateTime = sdf.parse(matcher.group(2));
                int responseCode = Integer.parseInt(matcher.group(3));
                long responseTime = (long) Double.parseDouble(matcher.group(4));

                return new LogEntry(dateTime, responseCode, responseTime);
            } catch (ParseException | NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
