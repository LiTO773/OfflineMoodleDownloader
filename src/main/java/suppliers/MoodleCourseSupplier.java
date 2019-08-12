package suppliers;

import models.Course;

import java.util.ArrayList;
import java.util.List;

public class MoodleCourseSupplier implements CourseSupplier {
    @Override
    public List<Course> getAllCourses() throws Exception {
        ArrayList<Course> result = new ArrayList<>();
        MoodleWSConnection moodleWS = new MoodleWSConnection();

        //
        return result;
    }

    @Override
    public List<Course> getInfoFromAllCourses() throws Exception {
        return null;
    }
}
