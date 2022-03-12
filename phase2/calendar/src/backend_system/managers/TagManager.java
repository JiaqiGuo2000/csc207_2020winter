package backend_system.managers;

import backend_system.entities.Event;
import backend_system.entities.Note;

import java.util.List;

/**
 * A subclass of NoteManager contains all the Tag and manipulates Tag.
 */
public class TagManager extends NoteManager {

    private Note searchThroughHashMap(String note) {
        for (Note key : getNotes().keySet()) {
            if (key.toString().equals(note)) {
                return key;
            }
        }
        return null;
    }

    /**
     * show all the events with the given tag
     *
     * @param tag the target tag
     * @return a list of Event
     */
    public List<Event> searchEventByTag(String tag) {
        Note searchResult = searchThroughHashMap(tag);
        if (searchResult != null) {
            return getNotes().get(searchResult);
        }
        return null;
    }
}
