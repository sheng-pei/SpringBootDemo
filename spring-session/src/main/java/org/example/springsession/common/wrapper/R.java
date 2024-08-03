package org.example.springsession.common.wrapper;

import ppl.common.utils.ArrayUtils;

public class R<T> {
    private final ResponseCode code;
    private String message;
    private T data;

    R(ResponseCode code, Object... info) {
        this.code = code;
        this.message = code.message(info);
    }

    public void message(String message) {
        this.message = message;
    }

    public <D> R<D> data(D data) {
        @SuppressWarnings({"rawtypes", "unchecked"})
        R<D> res = (R) this;
        res.data = data;
        return res;
    }

    public ResponseCode getCode() {
        return code;
    }

    public boolean isSuccess() {
        return ResponseCode.OK == code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> R<T> fromException(Throwable t) {
        ResponseCode rc = ResponseCode.fromException(t);
        Object[] params = ArrayUtils.zero();
        if (t instanceof MessageParameter) {
            params = ((MessageParameter) t).params();
        }
        return rc.res(params);
    }

}
