package backend_system;

import java.io.*;

/**
 * This is the helper class for serialization
 * @param <T> the object to be serialized
 */
class SerHelper<T extends Serializable> {
    private File file;

    /**
     * Constructor of the class.
     * @param file the file stores the serialized data
     * @param x the object to be serialized
     * @throws IOException an exception in file I/O
     */
    SerHelper(File file, T x) throws IOException {
        this.file = file;

        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();

        if (file.createNewFile())
            write(x);
    }

    /**
     * This method reads from files to generate objects.
     * @return an object stored in the file
     * @throws IOException an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    T read() throws IOException, ClassNotFoundException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file));
             ObjectInput in = new ObjectInputStream(is)) {
            return (T) in.readObject();
        }
    }

    /**
     * This method put serialized data into the file
     * @param x the object to be serialized
     * @throws IOException an exception in file I/O
     */
    void write(T x) throws IOException {
        if (file == null) return;
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
             ObjectOutput out = new ObjectOutputStream(os)) {
            out.writeObject(x);
        }
    }

    /**
     * This method deletes the file.
     * @return 1 if successful
     *          -1 if unsuccessful
     */
    int delete() {
        int ret = file.delete() ? 1 : -1;
        file = null;
        return ret;
    }
}
