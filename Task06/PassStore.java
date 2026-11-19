import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class PassStore {
    private final Path file;
    public PassStore(Path file) { this.file = file; }

    public void add(String passId, String regNo, String name, int route, double amount)
            throws IOException {
        if (passId == null || passId.isBlank() || regNo == null || regNo.isBlank()
                || name == null || name.isBlank() || route < 0
                || !Double.isFinite(amount) || amount < 0)
            throw new IllegalArgumentException("Invalid pass details");
        if (passId.contains(",") || regNo.contains(",") || name.contains(","))
            throw new IllegalArgumentException("Commas are not supported in this simple CSV exercise");
        try (BufferedWriter w = Files.newBufferedWriter(file,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(String.join(",", passId, regNo, name,
                    String.valueOf(route), String.valueOf(amount)));
            w.newLine();
        }
    }

    /** Returns five fields, or null if not found. Damaged records are skipped. */
    public String[] find(String passId) throws IOException {
        if (!Files.exists(file)) return null;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length != 5) continue;
                try {
                    if (fields[0].isBlank() || fields[1].isBlank()
                            || fields[2].isBlank() || Integer.parseInt(fields[3]) < 0
                            || Double.parseDouble(fields[4]) < 0) continue;
                } catch (NumberFormatException ex) { continue; }
                if (fields[0].equals(passId)) return fields;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Path f = Files.createTempFile("passes", ".csv");
        PassStore store = new PassStore(f);
        store.add("P1001", "192311001", "Arun", 12, 4500);
        Files.writeString(f, "P1002,DAMAGED LINE\n", StandardOpenOption.APPEND);
        store.add("P1003", "192311003", "Divya", 7, 3800);
        System.out.println(Arrays.toString(store.find("P1003")));
        System.out.println(Arrays.toString(store.find("P1002")));
        Files.deleteIfExists(f);
    }
}
