package org.chesscorp.club.model.token;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Single system token.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@SequenceGenerator(name = "token_seq", initialValue = 1, allocationSize = 1, sequenceName = "token_seq")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    private Long id;
    @Column(nullable = false)
    private TokenType type;
    @Column(nullable = false, length = 36)
    private String text;
    @Column(nullable = false)
    private OffsetDateTime issueDate;
    @Column(nullable = true)
    private OffsetDateTime expirationDate;
    @Column(nullable = true, length = 32)
    private String systemReference;
    @Column(nullable = false)
    private Integer usages;

    public Token() {
    }

    public Token(TokenType type, String text,
                 OffsetDateTime issueDate, OffsetDateTime expirationDate,
                 String systemReference) {
        this.type = type;
        this.text = text;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.systemReference = systemReference;
        this.usages = 0;
    }

    public Long getId() {
        return id;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public OffsetDateTime getIssueDate() {
        return issueDate;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public String getSystemReference() {
        return systemReference;
    }

    public Integer getUsages() {
        return usages;
    }

    public void setUsages(Integer usages) {
        this.usages = usages;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", systemReference='" + systemReference + '\'' +
                ", usages='" + usages + '\'' +
                '}';
    }
}
