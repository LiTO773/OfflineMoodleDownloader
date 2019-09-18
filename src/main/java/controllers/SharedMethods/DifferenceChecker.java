package controllers.SharedMethods;

import models.Course;

import java.util.List;

public class DifferenceChecker {
    public static boolean areDifferentLists(List<Course> originalList, List<Course> newList) {
        return originalList.stream().allMatch(c -> newList.contains(c));
    }
}
