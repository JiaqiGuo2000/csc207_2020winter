package backend_system.Entities;

/**
 * The class Tag represents a small tag that can be associated with Event in calendar. It inherits Note.
 *
 * @see Note
 */
public class Tag extends Note {

    /**
     * Creates a Tag with specified tag message.
     *
     * @param tag the tag message.
     */
    public Tag(String tag) {
        super(tag);
    }

    //For future extendability, e.g., color...
}
