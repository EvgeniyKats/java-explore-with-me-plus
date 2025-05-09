package exception;

public class NullBody extends RuntimeException {
    public NullBody() {
        super("Сервер вернул ответ, но тело пустое");
    }
}
