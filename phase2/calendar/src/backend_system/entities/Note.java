package backend_system.entities;

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
     * Change current Note's message.
     *
     * @param note the note message.
     */
    public void setNote(String note){
        this.note = note;
    }

    /**
     * Get the note.
     * @return a String
     */
    public String getNote(){
        return note;
    }

    @Override
    public int hashCode() {
        int base = 207;
        return base + note.hashCode();
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

    /**
     * This new overriding version of equals.
     * @param o the Note to be compared
     * @return true if the same
     *          false if not the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note1 = (Note) o;
        return this.note.equals(note1.note);
    }
}