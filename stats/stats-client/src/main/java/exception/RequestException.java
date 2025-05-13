package exception;

public class RequestException extends RuntimeException {
    public RequestException(String method, String uri, int statusCode, String body) {
        super("Метод: " + method
              + "\n URI: " + uri
              + "\n Код: " + statusCode
              + "\n Ответ сервера: " + body);
    }
}
