import com.github.tacticallaptopbag.HELP_REPL
import com.github.tacticallaptopbag.HELP_RM_SUFFIX
import com.github.tacticallaptopbag.strmanip.remove.RemoveSuffixCommand
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.*

class RemoveSuffixCommandTest {
    private val command = RemoveSuffixCommand()

    private fun testFile(number: Int, extension: String = "mp3") =
        "file $number test.$extension"
    private fun testFileSuffix(number: Int, extension: String = "mp3") =
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
    fun testRemoveSuffix(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "\" test\"",
            testDir,
            fileChangelog = listOf(
                testFile(1) to testFileSuffix(1),
                testFile(2) to testFileSuffix(2),
                testFile(3) to testFileSuffix(3),
            )
        )

        testDir.expectFiles(
            testFileSuffix(1),
            testFileSuffix(2),
            testFileSuffix(3),
            UNRELATED_FILE,
        )
    }

    @Test
    fun testDryRemoveSuffix(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "\" test\" --dry",
            testDir,
            dry = true,
            fileChangelog = listOf(
                testFile(1) to testFileSuffix(1),
                testFile(2) to testFileSuffix(2),
                testFile(3) to testFileSuffix(3),
            )
        )

        testDir.expectFiles(
            testFile(1),
            testFile(2),
            testFile(3),
            UNRELATED_FILE,
        )
    }

    @Test
    fun testRemoveSuffixFileType(@TempDir testDir: Path) {
        createFiles(testDir)
        createFiles(testDir, "wav")

        command.expect(
            "\" test\" --filetype .wav",
            testDir,
            fileType = "wav",
            fileChangelog = listOf(
                testFile(1, "wav") to testFileSuffix(1, "wav"),
                testFile(2, "wav") to testFileSuffix(2, "wav"),
                testFile(3, "wav") to testFileSuffix(3, "wav"),
            )
        )

        testDir.expectFiles(
            testFile(1),
            testFile(2),
            testFile(3),
            testFileSuffix(1, "wav"),
            testFileSuffix(2, "wav"),
            testFileSuffix(3, "wav"),
            UNRELATED_FILE,
        )
    }

    @Test
    fun testHelp() {
        command.expectHelp(HELP_RM_SUFFIX)
    }
}