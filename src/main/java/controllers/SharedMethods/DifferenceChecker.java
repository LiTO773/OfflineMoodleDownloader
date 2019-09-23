package controllers.SharedMethods;

import models.Course;

import java.util.List;
import java.util.NoSuchElementException;

public class DifferenceChecker {
    public static boolean areDifferentLists(List<Course> originalList, List<Course> newList) {
        if(originalList.size() != newList.size()) return true;

        return !originalList.stream().allMatch(c -> newList.contains(c));
    }
}
