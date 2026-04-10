package com.group27.tarecruitment.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ValidationUtil {
    public static final String STATUS_SUBMITTED = "Submitted";
    public static final String STATUS_OFFERED = "Offered";
    public static final String STATUS_UNSUCCESSFUL = "Unsuccessful";
    public static final String STATUS_WITHDRAWN = "Withdrawn";

    private static final Set<String> ALLOWED_APPLICATION_STATUSES = Collections.unmodifiableSet(
            new LinkedHashSet<>(Arrays.asList(
                    STATUS_SUBMITTED,
                    STATUS_OFFERED,
                    STATUS_UNSUCCESSFUL,
                    STATUS_WITHDRAWN
            ))
    );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    public static Integer parsePositiveInt(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static List<String> splitCsv(String value) {
        if (isBlank(value)) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    public static boolean isValidEmail(String value) {
        return !isBlank(value) && EMAIL_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isPositiveInteger(String value) {
        return parsePositiveInt(value) != null;
    }

    public static String normalizeApplicationStatus(String value) {
        String normalized = trimToEmpty(value);
        if (STATUS_SUBMITTED.equalsIgnoreCase(normalized)) {
            return STATUS_SUBMITTED;
        }
        if (STATUS_OFFERED.equalsIgnoreCase(normalized)) {
            return STATUS_OFFERED;
        }
        if (STATUS_UNSUCCESSFUL.equalsIgnoreCase(normalized)) {
            return STATUS_UNSUCCESSFUL;
        }
        if (STATUS_WITHDRAWN.equalsIgnoreCase(normalized)) {
            return STATUS_WITHDRAWN;
        }
        return normalized;
    }

    public static boolean isValidApplicationStatus(String value) {
        return ALLOWED_APPLICATION_STATUSES.contains(normalizeApplicationStatus(value));
    }
}
