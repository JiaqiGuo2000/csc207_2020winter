package backend_system.managers;

import backend_system.entities.Event;
import backend_system.entities.Note;

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

    public NoteManager(HashMap<Note, List<Event>> notes, List<Note> noteList){
        this.noteList = noteList;
        this.notes = notes;
    }

    /**
     * show all the notes created before
     *
     * @return a list of Note
     */
    public List<Note> getAllNotes() {
        return noteList;
    }

    /**
     * add a given note to several events at a time
     *
     * @param note   the note to be added
     * @param events the list of events to be added on with note
     */
    public void addNotedListOfEvents(Note note, List<Event> events) {
        if (notes.containsKey(note)) {
            notes.get(note).addAll(events);
        } else {
            notes.put(note, events);
            noteList.add(note);
        }
    }

    /**
     * add a given note to a given event
     *
     * @param note  the note to be added
     * @param event the event to be added on with note
     */
    public void addNotedEvent(Note note, Event event) {
        List<Event> events = notes.get(note);
        if (events == null){
            List<Event> l = new ArrayList<>();
            l.add(event);
            notes.put(note, l);
            noteList.add(note);
        }else if (!events.contains(event)) {
            events.add(event);
        }
    }

    /**
     * return the HashMap with the relation of Note and List of Event
     *
     * @return a HashMap
     */
    public HashMap<Note, List<Event>> getNotes() {
        return notes;
    }

    /**
     * remove a given note from a given event
     *
     * @param event the event with the note to be removed from
     * @param note  the note to be removed
     */
    public void deleteNoteFromEvent(Event event, Note note) {
        notes.get(note).remove(event);
        if (notes.get(note).isEmpty()) {
            notes.remove(note);
            noteList.remove(note);
        }
    }

    /**
     * Remove a note entirely from the calendar
     *
     * @param note the note to be removed
     * @return  a list of events that are associated with this note
     */
    public List<Event> deleteEntireNote(Note note){
        List<Event> events = notes.get(note);
        notes.remove(note);
        noteList.remove(note);
        return events;
    }

    /**
     * Edit a note's message for a particular event associated.
     *
     * @param note  the old note to be removed
     * @param newNote the new note to be added
     * @param event the event with the note to be removed from
     */
    public void editNoteForOne(Note note, Note newNote, Event event){
        deleteNoteFromEvent(event, note);
        addNotedEvent(newNote, event);
    }

    /**
     * Edit a note's message for all events that associated.
     *
     * @param note  the old note to be removed
     * @param newNote the new note to be added
     * @return a list of events that are associated with this note
     */
    public List<Event> editEntireNote(Note note, Note newNote){
        List<Event> events = notes.get(note);
        addNotedListOfEvents(newNote, events);
        notes.remove(note);
        noteList.remove(note);
        return events;
    }

    /**
     * get the content of all notes in this manager
     *
     * @return a list of strings of note messages
     */
    public List<String> getContentOfAllNotes(){
        ArrayList<String> result = new ArrayList<>();
        for(Note i: this.noteList){
            result.add(i.getNote());
        }
        return result;
    }

}
