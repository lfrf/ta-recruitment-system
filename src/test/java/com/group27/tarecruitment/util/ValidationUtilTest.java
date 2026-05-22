package com.group27.tarecruitment.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ValidationUtilTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.util}</p>
 */
class ValidationUtilTest {

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void splitCsvShouldTrimRemoveEmptyAndDeduplicate() {
        List<String> values = ValidationUtil.splitCsv(" Java, Python , ,Java,SQL,Python ");
        assertEquals(List.of("Java", "Python", "SQL"), values);
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void parsePositiveIntShouldHandleBoundaryAndInvalidInputs() {
        assertEquals(1, ValidationUtil.parsePositiveInt("1"));
        assertNull(ValidationUtil.parsePositiveInt("0"));
        assertNull(ValidationUtil.parsePositiveInt("-2"));
        assertNull(ValidationUtil.parsePositiveInt("abc"));
        assertNull(ValidationUtil.parsePositiveInt(" "));
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void emailValidationShouldAcceptWellFormedAddressOnly() {
        assertTrue(ValidationUtil.isValidEmail("alice.zhang@example.com"));
        assertTrue(ValidationUtil.isValidEmail("alice+ta@sub.example.co.uk"));
        assertFalse(ValidationUtil.isValidEmail("alice.example.com"));
        assertFalse(ValidationUtil.isValidEmail("alice@"));
        assertFalse(ValidationUtil.isValidEmail(" "));
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void normalizeAndValidateApplicationStatusShouldBeCaseInsensitive() {
        assertEquals(ValidationUtil.STATUS_SUBMITTED,
                ValidationUtil.normalizeApplicationStatus("submitted"));
        assertEquals(ValidationUtil.STATUS_OFFERED,
                ValidationUtil.normalizeApplicationStatus(" OFFERED "));
        assertEquals("UNKNOWN", ValidationUtil.normalizeApplicationStatus("UNKNOWN"));

        assertTrue(ValidationUtil.isValidApplicationStatus("withdrawn"));
        assertFalse(ValidationUtil.isValidApplicationStatus("pending"));
    }
}

