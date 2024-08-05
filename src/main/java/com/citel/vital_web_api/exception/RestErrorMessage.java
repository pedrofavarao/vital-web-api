package com.citel.vital_web_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestErrorMessage {
    private HttpStatus statusCode;
    private String name;
    private String message;
}
