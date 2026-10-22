package pgdp.searchengine.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgdp.searchengine.pagerepository.Author;
import pgdp.searchengine.pagerepository.Document;
import pgdp.searchengine.pagerepository.DocumentCollection;
import pgdp.searchengine.util.Date;

public class SearchEngineTesting {

    static Date date;
    static Author author;
    static Document document;
    static DocumentCollection collection;

    @BeforeAll
    static void setUp(){
        date = new Date(5,3,2002);
        author = new Author("Ozgur", "Yilmaz", "Str", "mail", date);
        document = new Document("doc", "doc", "doc", date,author);
    }

    // Here I tested the constructor with a general case, in which I created a DocumentCollection and assigned it's
    // size to 4 using the constructor. Then I checked if the length of our bucket array is same with the
    // value I assigned using the constructor.
    @Test
    void testConstructor(){
        DocumentCollection newCollection = new DocumentCollection(4);
        assertEquals(4,newCollection.getBuckets().length);
    }

    // Here I tested the constructor with an unusual case. I assigned -1 as a size for our bucket array using the constructor.
    // Then I checked if the actual length of our array is initialized to 1 by the constructor. (as described in artemis)
    @Test
    void testConstructor2(){
        DocumentCollection newCollection = new DocumentCollection(-1);
        assertEquals(1, newCollection.getBuckets().length);
    }

    // Here I tested the add method with a general case. I just added a document to my documentCollection and checked
    // if my add method returned true. And I also checked if the document was added to the right place.
    @Test
    void testAdd(){
        collection = new DocumentCollection(2);
        assertTrue(collection.add(document));
        assertEquals(document, collection.getBuckets()[0].getHead().getDocument());
    }

    // Here I tested the add method with an unusual case, where I tried to add a null document to the collection.
    // I checked if the add method returned false and if the collection was empty still.
    @Test
    void testAdd2(){
        collection = new DocumentCollection(2);
        collection.add(null);
        assertFalse(collection.add(null));
        assertTrue(collection.isEmpty());
    }

    // Here I tested the find method with a general case, in which I first added a document with the ID 0 to the
    // collection. Then I checked if the find method really returns the document I added with the ID 0.
    @Test
    void testFind(){
        collection = new DocumentCollection(2);
        collection.add(document);
        assertEquals(document, collection.find(0));
    }

    // Here I didn't add any documents to my collection and tried to find a document with the ID 3. I tested if
    // the find method would return null in that case as in described in artemis.
    @Test
    void testFind2(){
        collection = new DocumentCollection(2);
        assertNull(collection.find(3));
    }

    // Here I tested the removeAll method. I added a document to the collection and checked if removeAll returned
    // true after I tried to remove all documents from the collection. Then, I also checked if the collection was
    // empty after removing all elements.
    @Test
    void testRemoveAll(){
        collection = new DocumentCollection(2);
        collection.add(document);
        assertTrue(collection.removeAll());
        assertTrue(collection.isEmpty());
    }

    // Here ı checked if removeAll method returned false when I tried to remove all documents from an already empty
    // collection.
    @Test
    void testRemoveAll2(){
        collection = new DocumentCollection(2);
        assertFalse(collection.removeAll());
    }

    // Here I first added the document with the ID 0. Then I tested the remove method by trying to remove a document
    // with the ID 0. I checked if the method returned true when I did so.
    @Test
    void testRemoveDocument(){
        collection = new DocumentCollection(2);
        collection.add(document);
        assertTrue(collection.removeDocument(0));
        assertTrue(collection.isEmpty());
    }

    // Here I tested if the method would return false when I tried to remove a document that doesn't exist in the collection.
    @Test
    void testRemoveDocument2(){
        collection = new DocumentCollection(2);
        assertFalse(collection.removeDocument(5));
    }

    // Here I tested if the removeDocumentsFromAuthor method would return true when I tried to remove a document
    // from a certain author, which was already added to the document. Then I checked if the document was
    // actually removed by checking if the collection is empty.
    @Test
    void testRemoveDocumentsFromAuthor(){
        collection = new DocumentCollection(2);
        collection.add(document);
        assertTrue(collection.removeDocumentsFromAuthor(author));
        assertTrue(collection.isEmpty());
    }

    // Here I tested if the method would return false when I tried to remove a document by an author when no such document
    // existed in the collection.
    @Test
    void testRemoveDocumentsFromAuthor2(){
        collection = new DocumentCollection(2);
        assertFalse(collection.removeDocumentsFromAuthor(author));
    }

    // Here I tested the removeOldDocuments method. Therefore, I created three new dates. All of them were really close
    // to each other and to the actual release date of the document in order to test the accuracy of the method better.
    // the release date of my document is already 5.3.2002, and I updated the last update date of my document to
    // date4 (10.3.2002). I called the removeOldDocuments method and gave date2 (12.3.2002) as the releaseDate parameter
    // and date3 (28.3.2002) as the lastUpdated parameter. This eay both the release date and the last update date of
    // my document is older than the parameters. In this case the removeOldDocuments method should return true.
    // I also checked if the collection was empty afterwards.
    @Test
    void testRemoveOldDocuments(){
        collection = new DocumentCollection(2);
        Date date2 = new Date(12,3,2002);
        Date date3 = new Date(28,3,2002);
        Date date4 = new Date(10,3,2002);
        document.setLastUpdateDate(date4);
        collection.add(document);
        assertTrue(collection.removeOldDocuments(date2,date3));
        assertTrue(collection.isEmpty());
    }

    // Here I did the exact same thing, but only switched the parameters of my method. This way the lastUpdated
    // parameter is now older than the releaseDate parameter. In this case the removeOldDocuments method
    // should return false and not remove anything from the list, even though the dates of my document were older
    // than the parameters.
    @Test
    void testRemoveOldDocuments2(){
        collection = new DocumentCollection(2);
        Date date2 = new Date(6,3,2002);
        Date date3 = new Date(28,3,2002);
        Date date4 = new Date(10,3,2002);
        document.setLastUpdateDate(date4);
        collection.add(document);
        assertFalse(collection.removeOldDocuments(date3,date2));
        assertFalse(collection.isEmpty());
    }


}
