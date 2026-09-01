package com.gm.ace.common.exception;

import com.gm.ace.common.result.ResultCode;
import lombok.Getter;

/**
 * 业务异常，携带 {@link ResultCode}
 *
 * @author guoym
 */
@Getter
public class BizException extends RuntimeException {

    private final ResultCode resultCode;

    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
}
