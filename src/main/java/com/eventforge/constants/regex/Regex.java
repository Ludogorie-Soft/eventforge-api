package com.eventforge.constants.regex;

public class Regex {

    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String FACEBOOK_PATTERN = "(?:https?:\\/\\/)?(?:www\\.)?(mbasic.facebook|m\\.facebook|facebook|fb)\\.(com|me)\\/(?:(?:\\w\\.)*#!\\/)?(?:pages\\/)?(?:[\\w\\-\\.]*\\/)*([\\w\\-\\.]*)";

    public static final String FULL_NAME_PATTERN = "^([a-zA-Zа-яА-Я -]*)$";

    public static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]+$";

    public static final String CREDENTIAL_CHECK_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,72}$";

    public static final String EVENT_CATEGORIES_PATTERN = "^([a-zA-Zа-яА-Я ,-]*)$";

    public static final String IMAGE_PATTERN = "(.*\\.)(jpg|jpeg|png|gif)$";
}