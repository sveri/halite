import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


class Logger implements Closeable {

    private PrintWriter out;

    public Logger(String botName) {
        try {
            out = new PrintWriter(botName + ".log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void info(String message) {
        out.println(message);
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
