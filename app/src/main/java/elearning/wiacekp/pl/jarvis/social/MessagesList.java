package elearning.wiacekp.pl.jarvis.social;

public class MessagesList {
    private String phone;
    private String date;
    private String message;
    private boolean inbox;
    private int threadId;

    public MessagesList(String phone, String date, String message, boolean inbox, int threadId) {
        this.phone = phone;
        this.date = date;
        this.message = message;
        this.inbox = inbox;
        this.threadId = threadId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInbox() {
        return inbox;
    }

    public void setInbox(boolean inbox) {
        this.inbox = inbox;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}
