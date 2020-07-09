package ru.greendatasoft.app.domain.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.LinkedHashMap;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@ApiModel(description = "request to filter data")
public class FilterRequest {
    private Predicate predicate;
    @ApiModelProperty("page number")
    @NotNull
    private Integer page = 0;
    @ApiModelProperty("Page size")
    @NotNull
    private Integer size = 10;
    @ApiModelProperty(value = "map of sorting keys")
    private LinkedHashMap<String, SortDirection> sort = new LinkedHashMap<>();
}
