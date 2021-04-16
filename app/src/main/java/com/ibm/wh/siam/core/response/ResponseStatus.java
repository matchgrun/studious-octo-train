/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response;

import java.io.Serializable;
import java.util.List;

import com.ibm.wh.siam.core.validator.ValidationError;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public class ResponseStatus
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String status;
    private String errorCode;
    private String message;
    private String info = "";
    private List<ValidationError> validationErrors;
}
