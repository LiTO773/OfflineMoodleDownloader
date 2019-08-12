package suppliers;

import models.Course;
import models.CurrentMoodle;
import models.Moodle;

import java.util.ArrayList;
import java.util.List;

public class MoodleCourseSupplier implements CourseSupplier {
    private MoodleWSConnection moodleWS = new MoodleWSConnection();

    @Override
    public List<Course> getAllCourses() throws Exception {
        // Gets all the courses, no need for additional parameters
        return moodleWS.getCourses();
    }

    @Override
    public void getCourseInfo(Course course) throws Exception {
        // Fills the course info (since it's passed as a reference)
        // Any problem it might encounter will be thrown
        moodleWS.fillCourse(course);
    }
}
