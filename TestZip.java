import java.io.*;
import java.util.zip.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class TestZip {
    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        
        zos.putNextEntry(new ZipEntry("Habits.csv"));
        zos.write("Name,Type\nHabit1,1".getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
        
        zos.putNextEntry(new ZipEntry("Checkmarks.csv"));
        zos.write("Date,Habit1\n2023-01-01,1".getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
        
        zos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ZipInputStream zis = new ZipInputStream(bais);
        ZipEntry entry = zis.getNextEntry();
        while(entry != null) {
            System.out.println(entry.getName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8));
            List<String> lines = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
            System.out.println(lines);
            zis.closeEntry();
            entry = zis.getNextEntry();
        }
    }
}
