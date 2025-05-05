import com.github.tacticallaptopbag.strmanip.remove.RemovePrefixCommand
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.*

class RemovePrefixCommandTest {
    private val command = RemovePrefixCommand()

    private fun testFile(number: Int, extension: String = "mp3") =
        "test file $number.$extension"
    private fun testFilePrefix(number: Int, extension: String = "mp3") =
        "file $number.$extension"

    companion object {
        const val UNRELATED_FILE = "unrelated file.mp3"
    }

    private fun createFiles(testDir: Path, extension: String = "mp3") {
        testDir.createFile(testFile(1, extension))
        testDir.createFile(testFile(2, extension))
        testDir.createFile(testFile(3, extension))
        testDir.createFile(UNRELATED_FILE)
    }

    @Test
    fun testRemovePrefix(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "\"test \"",
            testDir,
            fileChangelog = listOf(
                testFile(1) to testFilePrefix(1),
                testFile(2) to testFilePrefix(2),
                testFile(3) to testFilePrefix(3),
            ),
        )

        testDir.expectFiles(
            testFilePrefix(1),
            testFilePrefix(2),
            testFilePrefix(3),
            UNRELATED_FILE,
        )
    }

    @Test
    fun testDryRemovePrefix(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "\"test \" --dry",
            testDir,
            dry = true,
            fileChangelog = listOf(
                testFile(1) to testFilePrefix(1),
                testFile(2) to testFilePrefix(2),
                testFile(3) to testFilePrefix(3),
            ),
        )

        testDir.expectFiles(
            testFile(1),
            testFile(2),
            testFile(3),
            UNRELATED_FILE,
        )
    }

    @Test
    fun testRemovePrefixFileType(@TempDir testDir: Path) {
        createFiles(testDir)
        createFiles(testDir, "wav")

        command.expect(
            "\"test \" --filetype .wav",
            testDir,
            fileType = "wav",
            fileChangelog = listOf(
                testFile(1, "wav") to testFilePrefix(1, "wav"),
                testFile(2, "wav") to testFilePrefix(2, "wav"),
                testFile(3, "wav") to testFilePrefix(3, "wav"),
            ),
        )

        testDir.expectFiles(
            testFile(1),
            testFile(2),
            testFile(3),
            testFilePrefix(1, "wav"),
            testFilePrefix(2, "wav"),
            testFilePrefix(3, "wav"),
            UNRELATED_FILE,
        )
    }
}