import java.io.*
import java.util.zip.*

val baos = ByteArrayOutputStream()
val zos = ZipOutputStream(baos)

zos.putNextEntry(ZipEntry("Habits.csv"))
zos.write("Name,Type\nHabit1,1".toByteArray())
zos.closeEntry()

zos.putNextEntry(ZipEntry("Checkmarks.csv"))
zos.write("Date,Habit1\n2023-01-01,1".toByteArray())
zos.closeEntry()

zos.close()

val bais = ByteArrayInputStream(baos.toByteArray())
val zis = ZipInputStream(bais)
var entry = zis.nextEntry
while(entry != null) {
    println(entry.name)
    val reader = BufferedReader(InputStreamReader(zis))
    val lines = reader.readLines()
    println(lines)
    zis.closeEntry()
    entry = zis.nextEntry
}
