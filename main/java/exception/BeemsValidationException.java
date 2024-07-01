package exception;

public class BeemsValidationException extends Exception {
    /*
    メッセージのみを受け取るコンストラクタ。
    メッセージと原因となる例外を受け取るコンストラクタ。
    原因となる例外のみを受け取るコンストラクタ。
    引数なしのデフォルトコンストラクタ。
    */
    public BeemsValidationException(String message) {
        super(message);
        System.err.println(message);
    }

    public BeemsValidationException(String message, Throwable cause) {
        super(message, cause);
        System.err.println(message);
    }

    public BeemsValidationException(Throwable cause) {
        super(cause);
        System.err.println(cause.getMessage());
    }

    public BeemsValidationException() {
        super();
    }
}
