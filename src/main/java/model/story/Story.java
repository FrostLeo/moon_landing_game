package model.story;

public interface Story {
    boolean isLegalStep(int step);
    String toJson();
}
