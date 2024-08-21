import java.util.Date;

/**
 * Запись лога
 */
public class LogEntry {
    private Date dateTime;
    private int responseCode;
    private long responseTime;

    public LogEntry(Date dateTime, int responseCode, long responseTime) {
        this.dateTime = dateTime;
        this.responseCode = responseCode;
        this.responseTime = responseTime;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
}
