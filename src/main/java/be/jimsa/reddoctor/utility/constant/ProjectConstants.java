package be.jimsa.reddoctor.utility.constant;

public class ProjectConstants {

    private ProjectConstants() {
    }

    // ======================== Application ======================== //
    public static final String APP_VERSION = "/v0.9";


    // ======================== DOCTOR ======================== //
    public static final String DOCTOR_DOCUMENT_NAME = "Doctor endpoint";
    public static final String DOCTOR_DOCUMENT_DESCRIPTION = "Rest APIs Doctor details";
    public static final String DOCTOR_PATH = APP_VERSION + "/doctors";


    // ======================== Patient ======================== //
    public static final String PATIENT_DOCUMENT_NAME = "Patient endpoint";
    public static final String PATIENT_DOCUMENT_DESCRIPTION = "Rest APIs Patient details";
    public static final String PATIENT_PATH = APP_VERSION + "/patients";


    // ======================== General ======================== //
    public static final String GENERAL_TYPE_FIELD = "type";
    public static final String GENERAL_DATE_FIELD = "date";
    public static final String GENERAL_SORT_FIELD = "sort_field";
    public static final String GENERAL_PUBLIC_ID_FIELD = "public_id";
    public static final String GENERAL_SORT_DIRECTION_ASC_FIELD = "asc";
    public static final String GENERAL_SORT_DIRECTION_DESC_FIELD = "desc";
    public static final String GENERAL_PAGE_DEFAULT_VALUE = "1";
    public static final String GENERAL_SIZE_DEFAULT_VALUE = "10";
    public static final String GENERAL_SORT_FIELD_DEFAULT_VALUE = "time";
    public static final String GENERAL_SORT_DIRECTION = "sort_direction";


    // ======================== Appointment ======================== //
    public static final String APPOINTMENT = "Appointment";
    public static final String APPOINTMENT_DTO_DOCUMENT_NAME = "Appointment";
    public static final String APPOINTMENT_DTO_DOCUMENT_DESCRIPTION = "This is 'AppointmentDto'";
    public static final String APPOINTMENT_TIME_FIELD = "startTime";
    public static final String APPOINTMENT_TYPE_FIELD_DEFAULT_VALUE = "all";
    public static final String APPOINTMENT_DATABASE_TABLE_NAME = "appointments";
    public static final String APPOINTMENT_DATABASE_JOIN_ID = "appointment_id";


    // ======================== Patient ======================== //
    public static final String PATIENT = "Patient";
    public static final String PATIENT_FIELD = "patient";
    public static final String PATIENT_DTO_DOCUMENT_NAME = "Patient";
    public static final String PATIENT_DTO_DOCUMENT_DESCRIPTION = "This is 'PatientDto'";
    public static final String PATIENT_PHONE_NUMBER_FIELD = "phone_number";
    public static final String PATIENT_DATABASE_TABLE_NAME = "patients";
    public static final String PATIENT_DATABASE_JOIN_ID = "patient_id";


    // ======================== Document ======================== //
    public static final String GENERAL_DOCUMENT_TYPE_FIELD_PATTERN_MESSAGE = "type field must be 'open' or 'all'";
    public static final String GENERAL_DOCUMENT_PAGE_EXAMPLE = "3";
    public static final String GENERAL_DOCUMENT_SIZE_EXAMPLE = "25";
    public static final String GENERAL_DOCUMENT_SORT_DIRECTION_FIELD_DESCRIPTION = "'asc' or 'desc'";

    public static final String APPOINTMENT_DOCUMENT_POST_SUMMERY = "Doctor: Create appointments";
    public static final String APPOINTMENT_DOCUMENT_POST_DESCRIPTION = "Create some new appointments using POST method and getting request body";
    public static final String APPOINTMENT_DOCUMENT_GET_SUMMERY = "Doctor/Patient: Read all appointments with different types";
    public static final String APPOINTMENT_DOCUMENT_GET_DESCRIPTION = "Read all appointments using GET method, by type";
    public static final String APPOINTMENT_DOCUMENT_DELETE_SUMMERY = "Doctor: Delete an appointment";
    public static final String APPOINTMENT_DOCUMENT_DELETE_DESCRIPTION = "Delete an appointment using DELETE method and getting a valid public_id as a parameter";
    public static final String PATIENT_DOCUMENT_POST_SUMMERY = "Patient: Update an appointment";
    public static final String PATIENT_DOCUMENT_POST_DESCRIPTION = "Update an appointment with adding a patient (TAKEN action)";
    public static final String PATIENT_DOCUMENT_GET_SUMMERY = "Patient: Read appointments";
    public static final String PATIENT_DOCUMENT_GET_DESCRIPTION = "Reading all the patient's appointments";


    // ======================== Validation ======================== //
    public static final String GENERAL_TYPE_ALL = "all";
    public static final String GENERAL_TYPE_OPEN = "open";
    public static final String GENERAL_VALIDATION_TYPE_PATTERN = "open|all";
    public static final String GENERAL_VALIDATION_TYPE_FIELD_PATTERN_MESSAGE = "type field must be 'open' or 'all'";
    public static final String GENERAL_VALIDATION_PAGE_POSITIVE_INTEGER = "page must be a positive number";
    public static final String GENERAL_VALIDATION_SIZE_POSITIVE_INTEGER = "size must be a positive number";
    public static final String GENERAL_VALIDATION_DATE_MESSAGE = "date must NOT be null/blank/empty";
    public static final String GENERAL_VALIDATION_SORT_DIRECTION_PATTERN = "asc|desc";
    public static final String GENERAL_VALIDATION_SORT_DIRECTION_PATTERN_MESSAGE = "sort direction must be 'asc' or 'desc'";

    public static final String APPOINTMENT_VALIDATION_START_MESSAGE = "start_time must NOT be null/blank/empty";
    public static final String APPOINTMENT_VALIDATION_END_MESSAGE = "ent_time must NOT be null/blank/empty";
    public static final String APPOINTMENT_VALIDATION_PUBLIC_ID_NULL_MESSAGE = "public_id must BE null on the creation operation";
    public static final String APPOINTMENT_VALIDATION_TYPE_NULL_MESSAGE = "type must BE null on the creation operation";
    public static final String APPOINTMENT_VALIDATION_PATIENT_NULL_MESSAGE = "patient must BE null on the creation operation";
    public static final String PATIENT_VALIDATION_NAME_NOT_EMPTY_MESSAGE = "name must NOT be empty";
    public static final String PATIENT_VALIDATION_NAME_NOT_BLANK_MESSAGE = "name must NOT be blank";
    public static final String PATIENT_VALIDATION_PHONE_NUMBER_NOT_EMPTY_MESSAGE = "phone_number must NOT be empty";
    public static final String PATIENT_VALIDATION_PHONE_NUMBER_NOT_BLANK_MESSAGE = "phone_number must NOT be blank";
    public static final String PATIENT_VALIDATION_PHONE_NUMBER_PATTERN = "^9\\d{9}$";
    public static final String PATIENT_VALIDATION_PHONE_NUMBER_FIELD_EXAMPLE = "9131231234";
    public static final String PATIENT_VALIDATION_PHONE_NUMBER_PATTERN_MESSAGE = "phone_number most be 10 digits, with started by '9'";
    public static final int PATIENT_VALIDATION_MAX_LENGTH = 100;
    public static final int PATIENT_VALIDATION_MIN_LENGTH = 4;
    public static final String PATIENT_VALIDATION_LENGTH_MESSAGE = "name's length must be between " + PATIENT_VALIDATION_MIN_LENGTH + " and " + PATIENT_VALIDATION_MAX_LENGTH + " characters";


    // ======================== Exception ======================== //
    public static final String GENERAL_EXCEPTION_LOG_PATTERN = "%s : %s";
    public static final String GENERAL_EXCEPTION_REGEX = "%s %s";
    public static final String GENERAL_EXCEPTION_MESSAGE = "message";
    public static final String GENERAL_EXCEPTION_PATH = "path";

    public static final String EXCEPTION_DATE_MESSAGE = "This day has been initialized before!";
    public static final String EXCEPTION_START_END_FORMAT_MESSAGE = "The start time should be less than the end time";
    public static final String EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE = "The resource with provided public_id not founded!";
    public static final String EXCEPTION_NOT_FOUND_RESOURCE_BY_PHONE_NUMBER_MESSAGE = "The resource with provided phone_number not founded!";
    public static final String EXCEPTION_NOT_ACCEPTABLE_RESOURCE_MESSAGE = "The resource with provided public_id founded with a relation, so you can not remove it!";
    public static final String EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE = "The resource already exists!";

    // ======================== Response ======================== //
    public static final String RESPONSE_DOCUMENT_NAME = "App Response";
    public static final String RESPONSE_DOCUMENT_DESCRIPTION = "This is 'ResponseDto'";
    public static final String RESPONSE_DOCUMENT_ACTION_DESCRIPTION = "What is the final result? It will be true if the process is finished without any faults, otherwise, it is false.";
    public static final String RESPONSE_DOCUMENT_ACTION_EXAMPLE = "true";
    public static final String RESPONSE_DOCUMENT_TIMESTAMP = "The time of response";
    public static final String RESPONSE_DATE_TIME_FORMAT_EXAMPLE = "16/09/2024 23:11:14 PM";
    public static final String RESPONSE_DOCUMENT_RESULT = "The result of the response, including boolean, object, list, and so on";


    // ======================== DATE TIME ======================== //
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss a";


    // ======================== UTILITY ======================== //
    public static final String ANONYMOUS_USER = "AnonymousUser";

    public static final long LOG_DEFAULT_TIMEOUT = 15;
    public static final String LOG_PATTERN = "Executed {} in {} ms";

    public static final String PUBLIC_ID_PATTERN = "^[A-Za-z0-9-_]+$";
    public static final String PUBLIC_ID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    public static final int PUBLIC_ID_MIN_LENGTH = 32;
    public static final int PUBLIC_ID_MAX_LENGTH = 512;
    public static final int PUBLIC_ID_DEFAULT_LENGTH = 64;

}
