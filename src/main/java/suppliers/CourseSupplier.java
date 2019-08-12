package suppliers;

import models.Course;

import java.util.List;

/**
 * Defines how a course Service should behave
 * The functions don't receive any params since they rely on CurrentMoodle
 */
public interface CourseSupplier {
    // Gets all the courses, without modules, files and folders
    List<Course> getAllCourses() throws Exception;

    // Gets each module, file and folder in CurrentMoodle's moodle
    List<Course> getInfoFromAllCourses() throws Exception;
}
