package com.spring.email.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDTO  {

    private String[] toUser;
    private String subject;
    private String message;

}
