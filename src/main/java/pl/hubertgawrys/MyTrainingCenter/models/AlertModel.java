package pl.hubertgawrys.MyTrainingCenter.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertModel {

    private int id;
    private String name;
    private String alert;
}
