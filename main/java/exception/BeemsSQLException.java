package exception;

public class BeemsSQLException extends Exception {
    /*
    メッセージのみを受け取るコンストラクタ。
    メッセージと原因となる例外を受け取るコンストラクタ。
    原因となる例外のみを受け取るコンストラクタ。
    引数なしのデフォルトコンストラクタ。
    */
    public BeemsSQLException(String message) {
        super(message);
        System.err.println(message);
    }

    public BeemsSQLException(String message, Throwable cause) {
        super(message, cause);
        System.err.println(message);
        cause.printStackTrace();
    }

    public BeemsSQLException(Throwable cause) {
        super(cause);
        System.err.println(cause.getMessage());
    }

    public BeemsSQLException() {
        super();
    }
}
