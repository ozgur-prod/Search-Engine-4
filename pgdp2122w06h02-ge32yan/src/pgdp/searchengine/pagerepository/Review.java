package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;

public class Review {
    private final int postId;
    private static int nextPostId = 0;
    private String title;
    private String content;
    private final Date postDate;
    private final Author reviewer;
    private final Document reviewedDocument;

    public Review(String title, String content, Date postDate, Author reviewer, Document reviewedDocument) {
        postId = nextPostId++;
        setTitle(title);
        setContent(content);
        this.postDate = postDate;
        this.reviewer = reviewer;
        this.reviewedDocument = reviewedDocument;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public Date getPostDate() {
        return postDate;
    }
    public Author getReviewer() {
        return reviewer;
    }
    public Document getReviewedDocument() {
        return reviewedDocument;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getAgeText() {
        return "Vor " + postDate.daysUntil(Date.today()) + " Tagen gepostet";
    }

    public boolean equals(Review other) {
        return postId == other.postId;
    }

    public String toString() {
        return "[" + postId + ": " + reviewedDocument + "]";
    }

    public String toPrintText() {
        return "<|==== Review of " + reviewedDocument + " ====|>"
                + "\nId: " + postId
                + "\nTitle: " + title
                + "\n" + content
                + "\nPosted at " + postDate
                + "\nPosted by " + reviewer;
    }
}
