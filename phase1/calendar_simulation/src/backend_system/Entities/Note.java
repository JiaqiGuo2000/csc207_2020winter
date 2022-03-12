package backend_system.Entities;

import java.io.Serializable;

/**
 * The class Note represents a little message. Both Tag and Memo class extends
 * it to have the extensibility in the future.
 *
 * @see Tag
 * @see Note
 */
public class Note implements Serializable {

    private String note;

    /**
     * Creates Note with specified note message.
     *
     * @param note the note message.
     */
    public Note(String note) {
        this.note = note;
    }

    /**
     * Return the note message it represents.
     *
     * @return the note message that this object contains.
     */
    @Override
    public String toString() {
        return this.note;
    }

}