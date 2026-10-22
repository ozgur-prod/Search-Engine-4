package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;

public class DocumentCollection {

    private final Bucket[] buckets;

    public DocumentCollection(int size) {
        if (size > 0) {
            buckets = new Bucket[size];
            for (int i = 0; i < size; i++) {
                Bucket bucket = new Bucket();
                buckets[i] = bucket;
            }
        } else {
            buckets = new Bucket[1];
            Bucket bucket = new Bucket();
            buckets[0] = bucket;
        }
    }

    public Bucket[] getBuckets() {
        return buckets;
    }

    private int indexFunction(int id) {
        //int index = id % buckets.length;
        return id % buckets.length;
    }

    public boolean add(Document document) {

        if (document == null){
            return false;
        }

        DocumentListElement element = new DocumentListElement(document);
        int index = indexFunction(document.getDocumentId());

        if (!(document.getDocumentId() < 0) && !contains(document)) {

            if (buckets[index].getHead() == null) {
                buckets[index].setHead(element);
                buckets[index].setTail(element);
            } else {
                buckets[index].getTail().setNext(element);
                element.setPre(buckets[index].getTail());
                buckets[index].setTail(element);

                DocumentListElement current;
                DocumentListElement after;
                Document temp;

                for (current = buckets[index].getHead(); current != null; current = current.getNext()) {
                    for (after = current.getNext(); after != null; after = after.getNext()) {
                        if (current.getDocument().getDocumentId() > after.getDocument().getDocumentId()) {
                            temp = current.getDocument();
                            current.setDocument(after.getDocument());
                            after.setDocument(temp);
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        for (Bucket bucket : buckets) {
            if (bucket.getHead() != null) {
                return false;
            }
        }
        return true;
    }

    public Document find(int id) {

        for (Bucket bucket : buckets) {

            DocumentListElement current;

            for (current = bucket.getHead(); current != null; current = current.getNext()) {
                if (current.getDocument().getDocumentId() == id) {
                    return current.getDocument();
                }
            }
        }
        return null;
    }

    public boolean removeDocument(int id) {
        boolean isRemoved = false;

        for (Bucket bucket : buckets) {

            DocumentListElement current;

            for (current = bucket.getHead(); current != null; current = current.getNext()) {
                if (current.getDocument().getDocumentId() == id) {
                    isRemoved = true;
                    removing(current,bucket);
                }
            }
        }
        return isRemoved;
    }

    public boolean removeDocumentsFromAuthor(Author author) {
        boolean isRemoved = false;

        for (Bucket bucket : buckets) {

            DocumentListElement current;

            for (current = bucket.getHead(); current != null; current = current.getNext()) {
                if (current.getDocument().getAuthor() == author) {
                    isRemoved = true;
                    removing(current,bucket);
                }
            }
        }
        return isRemoved;
    }

    public boolean removeAll() {
        if (isEmpty()) {
            return false;
        } else {
            for (Bucket bucket : buckets) {
                bucket.setHead(null);
                bucket.setTail(null);
            }
            return true;
        }
    }

    private boolean isAfterDate(Date first, Date second) {
        if (first.getYear() > second.getYear()) {
            return true;
        } else if (first.getYear() == second.getYear() && first.getMonth() > second.getMonth()) {
            return true;
        } else{
            return first.getYear() == second.getYear() && first.getMonth() == second.getMonth() && first.getDay() > second.getDay();
        }
    }

    public boolean removeOldDocuments(Date releaseDate, Date lastUpdated) {
        boolean isRemoved = false;

        if (releaseDate == null && lastUpdated == null) {
            removeAll();
            isRemoved = true;
        } else if (releaseDate == null) {
            for (Bucket bucket : buckets) {

                DocumentListElement current;

                for (current = bucket.getHead(); current != null; current = current.getNext()) {
                    if (isAfterDate(lastUpdated, current.getDocument().getLastUpdateDate())) {
                        isRemoved = true;
                        removing(current,bucket);
                    }
                }
            }
        } else if (lastUpdated == null) {
            for (Bucket bucket : buckets) {

                DocumentListElement current;

                for (current = bucket.getHead(); current != null; current = current.getNext()) {
                    if (isAfterDate(releaseDate, current.getDocument().getReleaseDate())) {
                        isRemoved = true;
                        removing(current,bucket);
                    }
                }
            }
        } else if (isAfterDate(releaseDate, lastUpdated)) {
            return false;
        } else {
            for (Bucket bucket : buckets) {

                DocumentListElement current;

                for (current = bucket.getHead(); current != null; current = current.getNext()) {
                    if (isAfterDate(releaseDate, current.getDocument().getReleaseDate()) && isAfterDate(lastUpdated, current.getDocument().getLastUpdateDate())) {
                        isRemoved = true;
                        removing(current,bucket);
                    }
                }
            }
        }
        return isRemoved;
    }


    public DocumentListElement getHead(int bucketIndex) {
        if (bucketIndex < 0 || bucketIndex > buckets.length - 1) {
            return null;
        } else {
            return buckets[bucketIndex].getHead();
        }
    }


    public DocumentListElement getTail(int bucketIndex) {
        if (bucketIndex < 0 || bucketIndex > buckets.length - 1) {
            return null;
        } else {
            return buckets[bucketIndex].getTail();
        }
    }

    public boolean contains(Document document) {
        for (Bucket bucket : buckets) {

            DocumentListElement current;

            for (current = bucket.getHead(); current != null; current = current.getNext()) {
                if (current.getDocument().getDocumentId() == document.getDocumentId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int size(){
        int size = 0;
        for (Bucket bucket : buckets) {

            DocumentListElement current;

            for (current = bucket.getHead(); current != null; current = current.getNext()) {
                size++;
            }
        }
        return size;
    }

    private void removing(DocumentListElement current, Bucket bucket){
        if (current == bucket.getHead() && current == bucket.getTail()) {
            bucket.setHead(null);
            bucket.setTail(null);
        } else if (current == bucket.getHead()) {
            current.getNext().setPre(null);
            bucket.setHead(current.getNext());
        } else if (current == bucket.getTail()) {
            current.getPre().setNext(null);
            bucket.setTail(current.getPre());
        } else {
            current.getNext().setPre(current.getPre());
            current.getPre().setNext(current.getNext());
        }
    }
}
