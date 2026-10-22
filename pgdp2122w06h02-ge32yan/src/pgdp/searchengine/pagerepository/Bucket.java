package pgdp.searchengine.pagerepository;

public class Bucket {

    private DocumentListElement head;
    private DocumentListElement tail;


    public Bucket(){
        head = null;
        tail = null;
    }

    public DocumentListElement getHead() {
        return head;
    }

    public DocumentListElement getTail() {
        return tail;
    }

    public void setHead(DocumentListElement head) {
        this.head = head;
    }

    public void setTail(DocumentListElement tail) {
        this.tail = tail;
    }



}
