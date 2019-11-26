package com.danger.app.user.model;

import com.danger.common.validator.CanNotBeNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class IdMorm {

    @CanNotBeNull
    @Range(min = 1)
    private Long id;

//    private int x;
}
