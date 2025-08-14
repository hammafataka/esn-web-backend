package dev.mfataka.esnzlin.exceptions;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 18.08.2024 23:18
 */
public class UserDisplayableException extends RuntimeException {

    public UserDisplayableException(String s) {
        super(s);
    }

    public UserDisplayableException(Throwable e) {
        super(e);
    }
}
