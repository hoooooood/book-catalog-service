package com.example.bookcatalog.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookValidatorFactory {

    private final StandardBookValidator standardBookValidator;
    private final UpdateBookValidator updateBookValidator;

    @Autowired
    public BookValidatorFactory(StandardBookValidator standardBookValidator,
                              UpdateBookValidator updateBookValidator) {
        this.standardBookValidator = standardBookValidator;
        this.updateBookValidator = updateBookValidator;
    }

    public BookValidator getValidator(String validatorType) {
        switch (validatorType.toLowerCase()) {
            case "standard":
                return standardBookValidator;
            case "update":
                return updateBookValidator;
            default:
                throw new IllegalArgumentException("Unknown validator type: " + validatorType);
        }
    }
}

