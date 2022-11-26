package com.findmecore.findmecore.errors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorDto extends ErrorDto {

    private List<InvalidField> invalidFields = new ArrayList<>();

    public ValidationErrorDto(int status, String message) {
        super(status, message);
    }
}
