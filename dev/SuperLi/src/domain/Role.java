package domain;

public record Role(String tag) {
    public String getName() {
        return tag;
    }
}
