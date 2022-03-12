package calendar_simulation;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The class AccountManager handles store, read, and verify accounts information related to the calendar system.
 */
class AccountManager {

    private Map<String, String> users;
    private File file;

    /**
     * Read account information if there exists such accounts, create one if there does not exist.
     *
     * @param filePath the path of the file that store the accounts
     * @throws IOException            an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    AccountManager(String filePath) throws IOException, ClassNotFoundException {
        file = new File(filePath);

        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();

        if (file.createNewFile()) {
            users = new HashMap<>();
            write();
        } else {
            read();
        }
    }

    /**
     * Return true if there is such user.
     *
     * @param name name of a user
     * @return true if the file contains such user
     */
    boolean contains(String name) {
        return users.containsKey(name);
    }

    /**
     * Return true iff the user exists and the password is correct.
     *
     * @param name     a username
     * @param password a password
     * @return true if the username matches the password
     */
    boolean match(String name, String password) {
        return password.equals(users.get(name));
    }

    /**
     * set password for a user
     *
     * @param name     username
     * @param password new password
     * @throws IOException an exception in file I/O
     */
    void set(String name, String password) throws IOException {
        users.put(name, password);
        write();
    }

    /**
     * @param name user
     * @return true if successfully removed
     * @throws IOException an exception in file I/O
     */
    int remove(String name) throws IOException {
        if (users.remove(name) == null)
            return -1;
        write();
        return 1;
    }

    /**
     * read file.
     *
     * @throws IOException            an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    private void read() throws IOException, ClassNotFoundException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file));
             ObjectInput in = new ObjectInputStream(is)) {
            users = (HashMap<String, String>) in.readObject();
        }
    }

    /**
     * write file.
     *
     * @throws IOException an exception in I/O
     */
    private void write() throws IOException {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
             ObjectOutput out = new ObjectOutputStream(os)) {
            out.writeObject(users);
        }
    }
}
