import com.github.tacticallaptopbag.FILETYPE_FAIL
import com.github.tacticallaptopbag.strmanip.ReplaceCommand
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.*

class ReplaceCommandTest {
    private val command = ReplaceCommand()

    private fun testFile(number: Int, extension: String = "mp3") =
        "hello there ${number.toString().padStart(2, '0')} there.$extension"
    private fun testFileReplaced(number: Int, extension: String = "mp3") =
        "hello earth ${number.toString().padStart(2, '0')} earth.$extension"
    private fun testFileRegexReplaced(number: Int, extension: String = "mp3") =
        "goodbye ${number.toString().padStart(2, '0')} there.$extension"
    private fun unrelatedFile(extension: String = "mp3") =
        "unrelated file.$extension"

    private fun createFiles(testDir: Path, extension: String = "mp3", unrelatedExtension: String = "mp3") {
        testDir.createFile(testFile(1, extension))
        testDir.createFile(testFile(2, extension))
        testDir.createFile(testFile(3, extension))
        testDir.createFile(unrelatedFile(unrelatedExtension))
    }

    @Test
    fun testReplace(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "there earth",
            testDir,
            fileChangelog = listOf(
                testFile(1) to testFileReplaced(1),
                testFile(2) to testFileReplaced(2),
                testFile(3) to testFileReplaced(3),
            ),
        )

        testDir.expectFiles(
            testFileReplaced(1),
            testFileReplaced(2),
            testFileReplaced(3),
            unrelatedFile(),
        )
    }

    @Test
    fun testReplaceRegex(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "\"hello \\\\w+\" goodbye --regex",
            testDir,
            fileChangelog = listOf(
                testFile(1) to testFileRegexReplaced(1),
                testFile(2) to testFileRegexReplaced(2),
                testFile(3) to testFileRegexReplaced(3),
            ),
        )

        testDir.expectFiles(
            testFileRegexReplaced(1),
            testFileRegexReplaced(2),
            testFileRegexReplaced(3),
            unrelatedFile(),
        )
    }

    @Test
    fun testDryReplace(@TempDir testDir: Path) {
        createFiles(testDir)

        command.expect(
            "there earth --dry",
            testDir,
            dry = true,
            fileChangelog = listOf(
                testFile(1) to testFileReplaced(1),
                testFile(2) to testFileReplaced(2),
                testFile(3) to testFileReplaced(3),
            ),
        )

        testDir.expectFiles(
            testFile(1),
            testFile(2),
            testFile(3),
            unrelatedFile(),
        )
    }

    @Test
    fun testReplaceFileType(@TempDir testDir: Path) {
        createFiles(testDir)
        createFiles(testDir, "wav")

        command.expect(
            "there earth --filetype .wav",
            testDir,
            fileType = "wav",
            fileChangelog = listOf(
                testFile(1, "wav") to testFileReplaced(1, "wav"),
                testFile(2, "wav") to testFileReplaced(2, "wav"),
                testFile(3, "wav") to testFileReplaced(3, "wav"),
            ),
        )

        testDir.expectFiles(
            testFile(1),
            testFile(2),
            testFile(3),
            testFileReplaced(1, "wav"),
            testFileReplaced(2, "wav"),
            testFileReplaced(3, "wav"),
            unrelatedFile(),
        )
    }

    @Test
    fun testFileTypeGuessFail(@TempDir testDir: Path) {
        createFiles(testDir, "txt", "txt")

        command.expect(
            "there earth",
            testDir,
            statusCode = 1,
            stderr = "$FILETYPE_FAIL\n",
        )

        testDir.expectFiles(
            testFile(1, "txt"),
            testFile(2, "txt"),
            testFile(3, "txt"),
            unrelatedFile("txt"),
        )
    }
}