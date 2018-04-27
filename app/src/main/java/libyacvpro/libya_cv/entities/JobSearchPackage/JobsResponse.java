package libyacvpro.libya_cv.entities.JobSearchPackage;

import java.util.List;

/**
 * Created by Asasna on 10/4/2017.
 */

public class JobsResponse {

    List<Jobs> jobsArray;

    public List<Jobs> getJobsArray() {
        return jobsArray;
    }

    public void setJobsArray(List<Jobs> jobsArray) {
        this.jobsArray = jobsArray;
    }
}
