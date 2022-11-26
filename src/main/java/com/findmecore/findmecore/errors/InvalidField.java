package com.findmecore.findmecore.errors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class InvalidField {
    private String field;
    private String errorMessage;
}
