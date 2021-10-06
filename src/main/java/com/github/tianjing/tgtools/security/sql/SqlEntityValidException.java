package com.github.tianjing.tgtools.security.sql;

/**
 * @author 田径
 * @date 2021-01-28 16:40
 * @desc
 **/
public class SqlEntityValidException extends Exception {
    public SqlEntityValidException() {
        super();
    }


    public SqlEntityValidException(String message) {
        super(message);
    }

    public SqlEntityValidException(String message, Throwable cause) {
        super(message, cause);
    }


    public SqlEntityValidException(Throwable cause) {
        super(cause);
    }


    protected SqlEntityValidException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
