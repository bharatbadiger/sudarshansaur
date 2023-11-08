package co.bharat.sudarshansaur.exception.handler;

public class EntityValidationException extends RuntimeException {
    private static final long serialVersionUID = -432471719083449101L;
	private final String entityName;
    private final String errorMessage;

    public EntityValidationException(String entityName, String errorMessage) {
        this.entityName = entityName;
        this.errorMessage = errorMessage;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
