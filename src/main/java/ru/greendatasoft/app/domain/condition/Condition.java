package ru.greendatasoft.app.domain.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@ApiModel(description = "Query condition")
public class Condition {

    @ApiModelProperty("entity field, that is used in expression")
    @NotNull
    private String key;
    @ApiModelProperty("Right-hand-side of logical expression")
    private Object value;
    @ApiModelProperty("List of logical expressions in case of `in` sql statement")
    private List<Object> values = new ArrayList<>();
    @ApiModelProperty(value = "logical expression operand on field", example = ">=")
    @NotNull
    private LogicalConditionType logicalCondition;

}
