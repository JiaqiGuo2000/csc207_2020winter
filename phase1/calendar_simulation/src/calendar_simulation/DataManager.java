package calendar_simulation;

import backend_system.Managers.EventManager;

import java.io.*;

/**
 * The class DataManager handles read and write data related to the calendar system to files.
 */
class DataManager {

    private File file;

    /**
     * Associate this DataManager with a file.
     * If the file does not exist, write the EventManager to the file.
     *
     * @param filePath the path of the file that should store the data
     * @param em       The event manager is going to be stored
     * @throws IOException an exception in file I/O.
     */
    /*
     * Associate this DataManager with a file.
     * If the file does not exist, write the EventManager to the file.
     */
    DataManager(String filePath, EventManager em) throws IOException {
        file = new File(filePath);

        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();

        if (file.createNewFile())
            write(em);
    }

    /**
     * read file to load all data about the event manager.
     *
     * @return the stored event manager
     * @throws IOException            an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    EventManager read() throws IOException, ClassNotFoundException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file));
             ObjectInput in = new ObjectInputStream(is)) {
            return (EventManager) in.readObject();
        }
    }

    /**
     * write file to store all data about the event manager.
     *
     * @param em The event manager is going to be stored
     * @throws IOException an exception in I/O
     */
    void write(EventManager em) throws IOException {
        if (file == null) return;
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
             ObjectOutput out = new ObjectOutputStream(os)) {
            out.writeObject(em);
        }
    }

    /**
     * delete the file that stores the user data.
     *
     * @return 1 if the file exists otherwise return -1
     */
    int delete() {
        int ret = file.delete() ? 1 : -1;
        file = null;
        return ret;
    }
}
