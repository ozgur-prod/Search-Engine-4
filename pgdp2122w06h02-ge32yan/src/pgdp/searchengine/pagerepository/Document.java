package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;

public class Document {
    private final int documentId;
    private static int nextDocumentId = 0;

    private String title;
    private String description;
    private String content;

    private final Date releaseDate;
    private Date lastUpdateDate;

    private final Author author;

    public Document(String title, String description, String content, Date releaseDate, Author author) {
        documentId = nextDocumentId++;
        setTitle(title);
        setDescription(description);
        this.content = content;
        this.releaseDate = releaseDate;
        this.lastUpdateDate = releaseDate;
        this.author = author;
    }

    public int getDocumentId() {
        return documentId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getContent() {
        return content;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    public Author getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        setLastUpdateDate(Date.today());
        this.title = title;
    }
    public void setDescription(String description) {
        setLastUpdateDate(Date.today());
        this.description = description;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int yearsSinceRelease() {
        return releaseDate.yearsUntil(Date.today());
    }

    public int daysSinceLastUpdate() {
        return lastUpdateDate.daysUntil(Date.today());
    }

    public boolean equals(Document other) {
        return documentId == other.documentId;
    }

    public String toString() {
        return title + ", by " + author + ", released " + releaseDate;
    }

    public String toPrintText() {
        return "<|==== " + title + " ====|>"
                + "\nBy " + author
                + "\n" + description
                + "Last updated at " + lastUpdateDate;
    }

    public static int numberOfCreatedDocuments() {
        return nextDocumentId;
    }
}
