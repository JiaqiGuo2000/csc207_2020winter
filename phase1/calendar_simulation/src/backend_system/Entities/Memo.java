package backend_system.Entities;

/**
 * The class Memo represents a small tag that can be associated with Event in calendar. It inherits from Note.
 *
 * @see Note
 */
public class Memo extends Note {

    /**
     * Creates a Memo with specified memo content.
     *
     * @param memo the memo content.
     */
    public Memo(String memo) {
        super(memo);
    }
}