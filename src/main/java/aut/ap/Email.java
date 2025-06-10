package aut.ap;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name  = "emails")
public class Email {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String subject;
    private String body;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Email(){

    }

    public Email(User sender, String subject, LocalDateTime sentAt, String body) {
        this.sender = sender;
        this.subject = subject;
        this.sentAt = sentAt;
        this.body = body;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
