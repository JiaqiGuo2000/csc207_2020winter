package backend_system.Managers;

import backend_system.Entities.Event;
import backend_system.Entities.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * a class contains all the Note and manipulates Note
 */
public class NoteManager implements Serializable {

    private HashMap<Note, List<Event>> notes;
    private List<Note> noteList;

    /**
     * create a new NoteManager
     */
    public NoteManager() {
        notes = new HashMap<>();
        noteList = new ArrayList<>();
    }

    /**
     * show all the notes created before
     *
     * @return a list of Note
     */
    List<Note> getAllNotes() {
        return noteList;
    }

    /**
     * add a given note to several events at a time
     *
     * @param note   the note to be added
     * @param events the list of events to be added on with note
     */
    void addNotedListOfEvents(Note note, List<Event> events) {
        if (notes.containsKey(note)) {
            notes.get(note).addAll(events);
        } else {
            notes.put(note, events);
        }
    }

    /**
     * add a given note to a given event
     *
     * @param note  the note to be added
     * @param event the evnet to be added on with note
     */
    void addNotedEvent(Note note, Event event) {
        if (notes.containsKey(note)) {
            notes.get(note).add(event);
        } else {
            List<Event> l = new ArrayList<>();
            l.add(event);
            notes.put(note, l);
        }
        noteList.add(note);
    }

    /**
     * return the HashMap with the relation of Note and List of Event
     *
     * @return a HashMap
     */
    HashMap<Note, List<Event>> getNotes() {
        return notes;
    }

    /**
     * remove a given note from a given event
     *
     * @param event the event with the note to be removed from
     * @param note  the note to be removed
     */
    void deleteNote(Event event, Note note) {
        notes.get(note).remove(event);
    }
}
