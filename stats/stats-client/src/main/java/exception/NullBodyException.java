package exception;

public class NullBodyException extends RuntimeException {
    public NullBodyException() {
        super("Сервер вернул ответ, но тело пустое");
    }
}
