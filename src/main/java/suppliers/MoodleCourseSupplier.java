package suppliers;

import models.Course;

import java.util.ArrayList;
import java.util.List;

public class MoodleCourseSupplier implements CourseSupplier {
    @Override
    public List<Course> getAllCourses() throws Exception {
        MoodleWSConnection moodleWS = new MoodleWSConnection();

        // Gets all the courses, no need for additional parameters
        return moodleWS.getCourses();
    }

    @Override
    public List<Course> getInfoFromAllCourses() throws Exception {
        return null;
    }
}
