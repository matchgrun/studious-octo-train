/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.validator;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public class ValidationError
implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private String errorMessage;

    public ValidationError(
            final String field,
            final String msg )
    {
        this.fieldName = field;
        this.errorMessage = msg;
    }

    public ValidationError(
            final String field )
    {
        this.fieldName = field;
    }
}
