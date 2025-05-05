import com.github.ajalt.clikt.testing.test
import com.github.tacticallaptopbag.DRY
import com.github.tacticallaptopbag.MUSIC_MAN_DIR
import com.github.tacticallaptopbag.strmanip.ReplaceCommand
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.*

class ReplaceCommandTest {
    private val command = ReplaceCommand()

    private fun testFile(number: Int) =
        "hello there ${number.toString().padStart(2, '0')} there.mp3"
    private fun testFileReplaced(number: Int) =
        "hello earth ${number.toString().padStart(2, '0')} earth.mp3"
    private fun testFileRegexReplaced(number: Int) =
        "goodbye ${number.toString().padStart(2, '0')} there.mp3"

    companion object {
        private const val UNRELATED_FILE = "unrelated file.mp3"
    }

    private fun createFiles(testDir: Path) {
        testDir.resolve(testFile(1)).toFile().createNewFile()
        testDir.resolve(testFile(2)).toFile().createNewFile()
        testDir.resolve(testFile(3)).toFile().createNewFile()
        testDir.resolve(UNRELATED_FILE).toFile().createNewFile()
    }

    @Test
    fun testReplace(@TempDir testDir: Path) {
        createFiles(testDir)

        val result = command.test(
            "there earth",
            envvars = mapOf(MUSIC_MAN_DIR to testDir.toString())
        )
        assertEquals(testDir.toString(), command.baseDir)
        assertEquals(0, result.statusCode)
        assertNull(command.fileType)
        assertFalse(command.dry)
        assertEquals(
            "${testFile(1)} -> ${testFileReplaced(1)}\n" +
                    "${testFile(2)} -> ${testFileReplaced(2)}\n" +
                    "${testFile(3)} -> ${testFileReplaced(3)}\n",
            result.stdout
        )

        val files = testDir.toFile().list()?.toList()?.filterNotNull() ?: emptyList()
        assertEquals(4, files.size)
        assertContains(files, testFileReplaced(1))
        assertContains(files, testFileReplaced(3))
        assertContains(files, testFileReplaced(3))
        assertContains(files, UNRELATED_FILE)
    }

    @Test
    fun testReplaceRegex(@TempDir testDir: Path) {
        createFiles(testDir)

        val result = command.test(
            "\"hello \\\\w+\" goodbye --regex",
            envvars = mapOf(MUSIC_MAN_DIR to testDir.toString())
        )
        assertEquals(testDir.toString(), command.baseDir)
        assertEquals(0, result.statusCode)
        assertNull(command.fileType)
        assertFalse(command.dry)
        assertEquals(
            "${testFile(1)} -> ${testFileRegexReplaced(1)}\n" +
                    "${testFile(2)} -> ${testFileRegexReplaced(2)}\n" +
                    "${testFile(3)} -> ${testFileRegexReplaced(3)}\n",
            result.stdout
        )

        val files = testDir.toFile().list()?.toList()?.filterNotNull() ?: emptyList()
        assertEquals(4, files.size)
        assertContains(files, testFileRegexReplaced(1))
        assertContains(files, testFileRegexReplaced(3))
        assertContains(files, testFileRegexReplaced(3))
        assertContains(files, UNRELATED_FILE)
    }

    @Test
    fun testDryReplace(@TempDir testDir: Path) {
        createFiles(testDir)

        val result = command.test(
            "there earth --dry",
            envvars = mapOf(MUSIC_MAN_DIR to testDir.toString())
        )
        assertEquals(testDir.toString(), command.baseDir)
        assertEquals(0, result.statusCode)
        assertNull(command.fileType)
        assertTrue(command.dry)
        assertEquals(
            "$DRY\n" +
            "${testFile(1)} -> ${testFileReplaced(1)}\n" +
            "${testFile(2)} -> ${testFileReplaced(2)}\n" +
            "${testFile(3)} -> ${testFileReplaced(3)}\n",
            result.stdout
        )

        val files = testDir.toFile().list()?.toList()?.filterNotNull() ?: emptyList()
        assertEquals(4, files.size)
        assertContains(files, testFile(1))
        assertContains(files, testFile(3))
        assertContains(files, testFile(3))
        assertContains(files, UNRELATED_FILE)
    }
}