package com.coderman.api.system.exception;

import com.coderman.api.system.enums.BaseCodeInterface;
import lombok.Data;

/**
 * 业务异常
 * @Author team
 * @Date 2020/4/1 14:47
 * @Version 1.0
 **/
@Data
public class BizException  extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected int errorCode;

    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
        this.errorMsg=message;
    }



    public BizException(BaseCodeInterface errorInfoInterface) {
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BizException(BaseCodeInterface errorInfoInterface,String message) {
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = message;
    }


}
