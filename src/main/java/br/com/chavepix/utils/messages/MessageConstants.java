package br.com.chavepix.utils.messages;

public class MessageConstants {

    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    //DATE FORMAT
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    //LOGS
    public static final String INTERNAL_ERROR_LOG = "LOG.0000";
    public static final String RESOURCE_UPDATE_LOG = "LOG.0001";
    public static final String RESOURCE_NOT_FOUND_CACHE = "LOG.0002";
    public static final String RESOURCE_FOUND = "LOG.0003";
    public static final String RESOURCE_NOT_FOUND = "LOG.0004";
    public static final String RESOURCE_EXCLUDE = "LOG.0005";
    public static final String CREATING = "LOG.0006";
    public static final String SAVING = "LOG.0007";
    public static final String PUBLISHING = "LOG.0008";
    public static final String EXECUTING_QUERY = "LOG.0009";
    public static final String EXCLUDING = "LOG.0010";
    public static final String PUBLISHING_QUEUE = "LOG.0011";
    public static final String READING_QUEUE = "LOG.0012";
    public static final String UNPROCESSABLE_ENTITY_LOG = "LOG.0013";
    public static final String RESOURCE_GET_BY_ID = "LOG.0014";
    public static final String RESOURCE_GET_PAGES = "LOG.0015";
    public static final String MAX_RESULT_PER_PAGE_LOG = "LOG.0016";
    public static final String ID_AND_PROTOCOL_NOT_FOUND = "LOG.0017";
    public static final String TRANSACTION_ID_ERROR_STATUS = "LOG.0018";
}