package com.studi.grupo3.emailconsumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Email message model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage implements Serializable {

    @JsonProperty("to")
    private String to;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("from")
    private String from;

    @Override
    public String toString() {
        return "EmailMessage{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
