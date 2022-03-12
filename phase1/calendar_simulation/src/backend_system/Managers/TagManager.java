package backend_system.Managers;

import backend_system.Entities.Event;
import backend_system.Entities.Note;

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
    List<Event> searchEventByTag(String tag) {
        Note searchResult = searchThroughHashMap(tag);
        if (searchResult != null) {
            return getNotes().get(searchResult);
        }
        return null;
    }
}
