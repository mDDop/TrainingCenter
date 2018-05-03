package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultModel<T> {

    private T t;
    private int result;

}
