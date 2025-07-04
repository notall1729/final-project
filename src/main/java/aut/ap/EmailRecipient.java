package aut.ap.database;

import aut.ap.User;
import jakarta.persistence.*;

@Entity
@Table(name = "email_recipients")
public class EmailRecipient {
    @Id
    @ManyToOne
    @JoinColumn(name = "email_id")
    private Email email;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(name = "is_read")
    private boolean isRead;


    public EmailRecipient(){

    }
    public EmailRecipient(Email email, boolean isRead, User recipient) {
        this.email = email;
        this.isRead = isRead;
        this.recipient = recipient;
    }

    public Email getEmail() {
        return email;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
