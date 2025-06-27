package com.example.bookcatalog.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookValidatorFactoryTest {

    @Mock
    private StandardBookValidator standardBookValidator;

    @Mock
    private UpdateBookValidator updateBookValidator;

    @InjectMocks
    private BookValidatorFactory bookValidatorFactory;

    @Test
    void getValidator_ShouldReturnStandardValidator_WhenTypeIsStandard() {
        String validatorType = "standard";
        BookValidator result = bookValidatorFactory.getValidator(validatorType);
        assertSame(standardBookValidator, result);
    }

    @Test
    void getValidator_ShouldReturnUpdateValidator_WhenTypeIsUpdate() {
        
        String validatorType = "update";

        
        BookValidator result = bookValidatorFactory.getValidator(validatorType);

        
        assertSame(updateBookValidator, result);
    }

    @Test
    void getValidator_ShouldReturnStandardValidator_WhenTypeIsStandardUpperCase() {
        
        String validatorType = "STANDARD";

        
        BookValidator result = bookValidatorFactory.getValidator(validatorType);

        
        assertSame(standardBookValidator, result);
    }

    @Test
    void getValidator_ShouldReturnUpdateValidator_WhenTypeIsUpdateMixedCase() {
        
        String validatorType = "UpDaTe";

        
        BookValidator result = bookValidatorFactory.getValidator(validatorType);

        
        assertSame(updateBookValidator, result);
    }

    @Test
    void getValidator_ShouldThrowIllegalArgumentException_WhenTypeIsUnknown() {
        
        String validatorType = "invalid";

         
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookValidatorFactory.getValidator(validatorType));
        assertEquals("Unknown validator type: " + validatorType, exception.getMessage());
    }

    @Test
    void getValidator_ShouldThrowIllegalArgumentException_WhenTypeIsNull() {
        String validatorType = "test";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookValidatorFactory.getValidator(validatorType));
        assertEquals("Unknown validator type: test", exception.getMessage());
    }

    @Test
    void getValidator_ShouldThrowIllegalArgumentException_WhenTypeIsEmpty() {
        
        String validatorType = "";

         
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookValidatorFactory.getValidator(validatorType));
        assertEquals("Unknown validator type: ", exception.getMessage());
    }
}
