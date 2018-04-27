package libyacvpro.libya_cv.entities.TrainingPackage;

import java.util.List;

/**
 * Created by Asasna on 9/28/2017.
 */

public class TrainingResponse {
    List<Training> training;

    public List<Training> getTraining() {
        return training;
    }

    public void setTraining(List<Training> trainings) {
        this.training = trainings;
    }
}
