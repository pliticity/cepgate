package pl.iticity.dbfds.model;

public class Mail {

    private String subject;

    private String recipient;

    private String message;

    private String[] files;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }
}
