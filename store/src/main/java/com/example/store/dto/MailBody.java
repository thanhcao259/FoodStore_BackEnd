package com.example.store.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
public record MailBody(String to, String subject, String text) {

}
