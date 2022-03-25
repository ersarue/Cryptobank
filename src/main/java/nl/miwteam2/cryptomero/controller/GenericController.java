package nl.miwteam2.cryptomero.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;


//todo vind overeenstemming of we dit willen gebruiken en wat we precies terug geven
public interface GenericController<E> {
    public ResponseEntity<?> findById(int id);
    public ResponseEntity<?> storeOne(E e);
    public List<E> getAll();
    public ResponseEntity<?> updateOne(E e);
    public ResponseEntity<?> deleteOne(int id);
}
