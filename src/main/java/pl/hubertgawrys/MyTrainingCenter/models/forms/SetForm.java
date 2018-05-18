package pl.hubertgawrys.MyTrainingCenter.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class SetForm {

    @Min(0)
    private Integer reps;
    @Min(0)
    private Integer kilos;
    private int workoutId;

}
