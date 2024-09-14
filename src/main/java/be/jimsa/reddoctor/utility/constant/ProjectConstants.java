package be.jimsa.reddoctor.utility.constant;

public class ProjectConstants {

    private ProjectConstants() {
    }

    // ======================== Application ======================== //
    public static final String APP_VERSION = "v0.9";


    // ======================== DOCTOR ======================== //
    public static final String DOCTOR_DOCUMENT_NAME = "Doctor endpoint";
    public static final String DOCTOR_DOCUMENT_DESCRIPTION = "Rest APIs Doctor details";
    public static final String DOCTOR_PATH = "/" + APP_VERSION + "/doctors";


    // ======================== Patient ======================== //
    public static final String PATIENT_DOCUMENT_NAME = "Patient endpoint";
    public static final String PATIENT_DOCUMENT_DESCRIPTION = "Rest APIs Patient details";
    public static final String PATIENT_PATH = "/" + APP_VERSION + "/patients";


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
    public static final String PATIENT_DTO_DOCUMENT_DESCRIPTION = "This is 'PatientDto', and will ignored when it's null";
    public static final String PATIENT_PHONE_NUMBER_FIELD = "phone_number";
    public static final String PATIENT_DATABASE_TABLE_NAME = "patients";
    public static final String PATIENT_DATABASE_JOIN_ID = "patient_id";


    // ======================== General ======================== //
    public static final String GENERAL_STRING_TYPE = "string";
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
    public static final String DOCTOR_GET_PATH = "/{" + GENERAL_DATE_FIELD + "}";
    public static final String DOCTOR_DELETE_PATH = "/{" + GENERAL_PUBLIC_ID_FIELD + "}";
    public static final String PATIENT_POST_PATH = "/{" + GENERAL_PUBLIC_ID_FIELD + "}";
    public static final String PATIENT_GET_PATH = "/{" + PATIENT_PHONE_NUMBER_FIELD + "}";


    // ======================== Validation ======================== //
    public static final String GENERAL_TYPE_ALL = "all";
    public static final String GENERAL_TYPE_OPEN = "open";
    public static final String GENERAL_VALIDATION_TYPE_PATTERN = "open|all";
    public static final String GENERAL_VALIDATION_TYPE_FIELD_PATTERN_MESSAGE = "type field must be 'open' or 'all'";
    public static final String GENERAL_VALIDATION_PAGE_POSITIVE_INTEGER = "page must be a positive number";
    public static final String GENERAL_VALIDATION_SIZE_POSITIVE_INTEGER = "size must be a positive number";
    public static final String GENERAL_VALIDATION_DATE_MESSAGE = "date must NOT be null/blank/empty";
    public static final String GENERAL_VALIDATION_PUBLIC_ID_DEFAULT_MESSAGE = "Invalid public_id";
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
    public static final String PATIENT_VALIDATION_PHONE_NUMBER_PATTERN_MESSAGE = "phone_number most be 10 digits, with started by '9'";
    public static final int PATIENT_VALIDATION_MAX_LENGTH = 100;
    public static final int PATIENT_VALIDATION_MIN_LENGTH = 4;
    public static final String PATIENT_VALIDATION_LENGTH_MESSAGE = "name's length must be between " + PATIENT_VALIDATION_MIN_LENGTH + " and " + PATIENT_VALIDATION_MAX_LENGTH + " characters";


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
    public static final String PATIENT_DOCUMENT_PHONE_NUMBER_PATTERN_DESCRIPTION = "phone_number most be 10 digits, with started by '9'";
    public static final String PATIENT_DOCUMENT_PHONE_NUMBER_FIELD_EXAMPLE = "9131231234";
    public static final String PATIENT_DOCUMENT_NAME_PATTERN_DESCRIPTION = PATIENT_VALIDATION_LENGTH_MESSAGE;
    public static final String PATIENT_DOCUMENT_NAME_FIELD_EXAMPLE = "Foo bar";


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

    public static final String EXCEPTION_METHOD_METHOD_ARGUMENT_NOT_VALID = "MethodArgumentNotValid";
    public static final String EXCEPTION_METHOD_HTTP_MESSAGE_NOT_READABLE = "HttpMessageNotReadable";
    public static final String EXCEPTION_METHOD_APP_4XX_EXCEPTION = "App4xxExceptions";
    public static final String EXCEPTION_METHOD_APP_404_EXCEPTION = "App404Exceptions";
    public static final String EXCEPTION_METHOD_APP_406_EXCEPTION = "App406Exceptions";
    public static final String EXCEPTION_METHOD_APP_5XX_EXCEPTION = "App5xxExceptions";


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
    public static final String DATE_FORMAT_EXAMPLE = "16-09-2024";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIME_FORMAT_EXAMPLE = "18:14:05";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss a";


    // ======================== UTILITY ======================== //
    public static final String ANONYMOUS_USER = "AnonymousUser";

    public static final long LOG_DEFAULT_TIMEOUT = 15;
    public static final String LOG_PATTERN = "Executed {} in {} ms";

    public static final String PUBLIC_ID_PATTERN = "^[A-Za-z0-9-_]+$";
    public static final String PUBLIC_ID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    public static final String PUBLIC_ID_DESCRIPTION = "The public_id of the resource";
    public static final String PUBLIC_ID_EXAMPLE = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e";
    public static final int PUBLIC_ID_MIN_LENGTH = 32;
    public static final int PUBLIC_ID_MAX_LENGTH = 512;
    public static final int PUBLIC_ID_DEFAULT_LENGTH = 64;

    public static final String LOGGER_TOTAL_ELEMENTS = "Total Elements: {}";
    public static final String LOGGER_TOTAL_PAGES = "Total Pages: {}";
    public static final String LOGGER_NUMBER_OF_ELEMENTS = "Number of Elements: {}";
    public static final String LOGGER_SIZE = "Size: {}";

    // ======================== API Documents ======================== //
    public static final String API_DOCUMENT_DEFAULT_SUMMERY = "Default summary";
    public static final String API_DOCUMENT_DEFAULT_DESCRIPTION = "Default description";

    public static final String API_DOCUMENT_RESPONSE_CODE_200 = "200";
    public static final String API_DOCUMENT_RESPONSE_CODE_201 = "201";
    public static final String API_DOCUMENT_RESPONSE_CODE_400 = "400";
    public static final String API_DOCUMENT_RESPONSE_CODE_404 = "404";
    public static final String API_DOCUMENT_RESPONSE_CODE_500 = "500";

    public static final String API_DOCUMENT_400_DESCRIPTION = "Due to invalid inputs, it responded as a bad request";
    public static final String API_DOCUMENT_404_DESCRIPTION = "Due to invalid public_id, it responded a not-found";
    public static final String API_DOCUMENT_500_DESCRIPTION = "Internal server error has occurred";
    public static final String API_DOCUMENT_500_EXAMPLE = """
            {
              "action": false,
              "timestamp": "10/09/2024 10:20:30 PM",
              "result": {
                "path": "{METHOD} /{VERSION}/{PATH-PID-...}",
                "message": "Internal service error!"
              }
            }
            """;

    // @UpdateAnAppointmentRequestDocument
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_200_DESCRIPTION = "Updating an appointment with the provided info was successful";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_200_EXAMPLE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:27:38 PM",
              "result": {
                "date": "11-12-2024",
                "start": "11:30:10",
                "end": "12:00:10",
                "public_id": "i625bsvXoeAwTFiPGd6Huo4gGIXgCqkMtuF1rwPMTN_sUiDq4kuHgJWIljKc715O",
                "patient": {
                  "name": "Foo bar",
                  "phone_number": "9131231234"
                }
              }
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_404_EXAMPLE = """
            {
              "action": false,
              "timestamp": "10/09/2024 10:26:24 PM",
              "result": {
                "path": "{METHOD} /{VERSION}/{PATH-PID-...}",
                "message": "The resource with provided public_id not founded!"
              }
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_DESCRIPTION = "This request comes with a name and phone number";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_1_NAME = "A valid request with a valid body #1";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_1_SUMMERY = "Valid example #1";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_1_VALUE = """
            {
              "name": "Foo bar",
              "phone_number": "9131231234"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_2_NAME = "An invalid request without a valid body #1";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_2_SUMMERY = "Invalid example #1";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_2_VALUE = """
            {
              "phone_number": "9131231234"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_3_NAME = "An invalid request without a valid body #2";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_3_SUMMERY = "Invalid example #2";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_3_VALUE = """
            {
              "name": "Foo bar"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_4_NAME = "An invalid request without a valid body #3";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_4_SUMMERY = "Invalid example #3";
    public static final String API_DOCUMENT_APPOINTMENT_UPDATE_REQUEST_BODY_EXAMPLE_4_VALUE = """
            {
              "name": "Foo",
              "phone_number": "000"
            }
            """;

    // @ReadAllRequestDocument
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_DESCRIPTION = "Reading all appointments was successful";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_EXAMPLE_1_NAME = "when there are three items";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_EXAMPLE_1_SUMMERY = "Response with values";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_EXAMPLE_1_VALUE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:20:27 PM",
              "result": [
                {
                  "date": "11-12-2024",
                  "start": "11:30:10",
                  "end": "12:00:10",
                  "public_id": "6ZCGPsreW4NoaRj12dhZBFRMH1jjzDOdEVHYDsivUdr5r6_JLQ5Mx69VCFHb7aQ4",
                  "patient": null
                },
                {
                  "date": "11-12-2024",
                  "start": "12:00:10",
                  "end": "12:30:10",
                  "public_id": "f6we3b2vdp-VQtjmrSIbyEWdO3Klj02JgCGuQHHIB27Yk1wxv2layTeBrVT_YNry",
                  "patient": null
                }
              ]
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_EXAMPLE_2_NAME = "when there is no item!";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_EXAMPLE_2_SUMMERY = "Response without value";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ALL_200_EXAMPLE_2_VALUE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:20:30 PM",
              "result": []
            }
            """;

    // @ReadMyAppointmentsRequestDocument
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_DESCRIPTION = "Reading all appointments was successful";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_EXAMPLE_1_NAME = "when there are three items";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_EXAMPLE_1_SUMMERY = "Response with values";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_EXAMPLE_1_VALUE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:20:27 PM",
              "result": [
                {
                  "date": "11-12-2024",
                  "start": "11:30:10",
                  "end": "12:00:10",
                  "public_id": "i625bsvXoeAwTFiPGd6Huo4gGIXgCqkMtuF1rwPMTN_sUiDq4kuHgJWIljKc715O",
                  "patient": {
                    "name": "Foo bar",
                    "phone_number": "9131231234"
                  }
                },
                {
                  "date": "11-12-2024",
                  "start": "15:30:10",
                  "end": "16:00:10",
                  "public_id": "i625bsvXoeAwTFiPGd6Huo4gGIXgCqkMtuF1rwPMTN_sUiDq4kuHgJWIljKc715O",
                  "patient": {
                    "name": "Foo bar",
                    "phone_number": "9131231234"
                  }
                }
              ]
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_EXAMPLE_2_NAME = "when there is no item!";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_EXAMPLE_2_SUMMERY = "Response without value";
    public static final String API_DOCUMENT_APPOINTMENT_GET_ONE_200_EXAMPLE_2_VALUE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:20:30 PM",
              "result": []
            }
            """;

    // @PublicIdParameterDocument
    public static final String API_DOCUMENT_PUBLIC_ID_DESCRIPTION = "The public_id of the resource";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE_1_NAME = "Valid example";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE_1_VALUE = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE_1_SUMMERY = "Example public ID 1, Valid";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE_2_NAME = "Invalid example";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE_2_VALUE = "xyz789xyz789xyz789x@%&*";
    public static final String API_DOCUMENT_PUBLIC_ID_EXAMPLE_2_SUMMERY = "Example public ID 2, Invalid";

    // @DeleteAnAppointmentRequestDocument
    public static final String API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_200_DESCRIPTION = "Deleting an appointment with the provided public_id was successful";
    public static final String API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_200_EXAMPLE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:18:23 PM",
              "result": true
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_DELETE_BY_PUBLIC_ID_404_EXAMPLE = """
            {
              "action": false,
              "timestamp": "10/09/2024 10:17:26 PM",
              "result": {
                "path": "{METHOD} /{VERSION}/{PATH-PID-...}",
                "message": "The resource with provided public_id not founded!"
              }
            }
            """;

    // @CreateAppointmentRequestDocument
    public static final String API_DOCUMENT_APPOINTMENT_POST_201_DESCRIPTION = "Creating appointments with the provided info was successful";
    public static final String API_DOCUMENT_APPOINTMENT_POST_201_EXAMPLE = """
            {
              "action": true,
              "timestamp": "10/09/2024 10:20:30 PM",
              "result": [
                {
                  "date": "11-12-2024",
                  "start": "11:30:10",
                  "end": "12:00:10",
                  "public_id": "RIXzFN_gwWmX2qXtY_fMT3HsLZ8fB4Roypdwzf1cQCglaBu8yznm0VXXqdzc01BI",
                  "patient": null
                },
                {
                  "date": "11-12-2024",
                  "start": "12:00:10",
                  "end": "12:30:10",
                  "public_id": "1_ufS5cjPkmwmcTYIC1rTSvHdJIHP3IfDHY_N-2FXER1J3Vv4fLOE3Z8VYkfl3lG",
                  "patient": null
                }
              ]
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_POST_400_EXAMPLE = """
            {
              "action": false,
              "timestamp": "10/09/2024 10:01:13 PM",
              "result": {
                "publicId": "public_id must be null on the creation operation"
              }
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_DESCRIPTION = "This request comes with a date, start and end time";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_1_NAME = "A valid request with a valid body #1";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_1_SUMMERY = "Valid example #1";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_1_VALUE = """
            {
              "date": "11-12-2024",
              "start": "11:30:10",
              "end": "12:35:00"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_2_NAME = "An invalid request without a valid body #1";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_2_SUMMERY = "Invalid example #1";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_2_VALUE = """
            {
              "start": "11:30:10",
              "end": "12:35:00"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_3_NAME = "An invalid request without a valid body #2";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_3_SUMMERY = "Invalid example #2";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_3_VALUE = """
            {
              "date": "11-12-2024",
              "start": "11:30:10"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_4_NAME = "An invalid request without a valid body #3";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_4_SUMMERY = "Invalid example #3";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_4_VALUE = """
            {
              "date": "11-12-2024",
              "end": "12:35:00"
            }
            """;
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_5_NAME = "An invalid request without a valid body #4";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_5_SUMMERY = "Invalid example #4";
    public static final String API_DOCUMENT_APPOINTMENT_POST_REQUEST_BODY_EXAMPLE_5_VALUE = """
            {
              "date": "11-12-2024"
            }
            """;

    // ======================== API Definition ======================== //
    public static final String API_DOCUMENT_INFO_TITLE = "RedBank Doctor Appointment (RBDA)";
    public static final String API_DOCUMENT_INFO_DESCRIPTION = "RBDA microservice REST API documentation";
    public static final String API_DOCUMENT_INFO_VERSION = APP_VERSION;
    public static final String API_DOCUMENT_INFO_CONTACT_NAME = "Iman Salehi";
    public static final String API_DOCUMENT_INFO_CONTACT_URL = "https://www.linkedin.com/in/jimsa/";
    public static final String API_DOCUMENT_INFO_CONTACT_EMAIL = "cse.isalehi@gmail.com";
    public static final String API_DOCUMENT_INFO_LICENSE_NAME = "Apache 2.0";
    public static final String API_DOCUMENT_INFO_LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";
    public static final String API_DOCUMENT_EXTERNAL_DOCS_DESCRIPTION = "Source code repository";
    public static final String API_DOCUMENT_EXTERNAL_DOCS_URL = "https://github.com/j-imsa/RedDoctor";
    public static final String API_DOCUMENT_SERVERS_1_DESCRIPTION = "Dev, local";
    public static final String API_DOCUMENT_SERVERS_1_URL = "http://localhost:8088/";
    public static final String API_DOCUMENT_SERVERS_2_DESCRIPTION = "Dev, Server";
    public static final String API_DOCUMENT_SERVERS_2_URL = "http://152.11.42.185:8090/";

}
