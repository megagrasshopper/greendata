package ru.greendatasoft.app.domain.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@ApiModel(description = "Predicate to form query")
public class Predicate {

    @ApiModelProperty("list of predicates, that is right-hand-side of predicate expression")
    private List<Predicate> predicates;
    @ApiModelProperty("Query condition")
    @NotNull
    private Condition condition;
    @ApiModelProperty(value = "type of logical expression between 2 predicates")
    @NotNull
    private ConnectionType connectionType;
}