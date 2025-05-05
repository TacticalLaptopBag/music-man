import com.github.tacticallaptopbag.HELP_TRACKNO
import com.github.tacticallaptopbag.strmanip.FixTrackNumbersCommand
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.Test

class FixTrackNumbersCommandTest {
    private val command = FixTrackNumbersCommand()

    private fun testFile(number: Int, extension: String = "mp3") =
        "$number track.$extension"
    private fun testFileFixed(number: Int, extension: String = "mp3", padLength: Int = 2) =
        "${number.toString().padStart(padLength, '0')} track.$extension"

    companion object {
        const val UNRELATED_FILE = "unrelated file.mp3"
    }

    private fun createFiles(testDir: Path, fileCount: Int = 10, extension: String = "mp3") {
        (1..fileCount).forEach {
            testDir.createFile(testFile(it, extension))
        }
        testDir.createFile(UNRELATED_FILE)
    }

    @Test
    fun testFixTrackNumbers(@TempDir testDir: Path) {
        createFiles(testDir)

        val fileChangelog = mutableListOf<Pair<String, String>>()
        (1..9).forEach {
            fileChangelog += testFile(it) to testFileFixed(it)
        }

        command.expect(
            "",
            testDir,
            fileChangelog = fileChangelog,
        )

        val expectedFiles = mutableListOf<String>()
        (1..10).forEach {
            expectedFiles += testFileFixed(it)
        }
        expectedFiles += UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFixTrackNumbers100(@TempDir testDir: Path) {
        createFiles(testDir, 100)

        val fileChangelog = mutableListOf<Pair<String, String>>()
        (1..99).forEach {
            fileChangelog += testFile(it) to testFileFixed(it, padLength = 3)
        }
        fileChangelog.sortBy { it.first }

        command.expect(
            "",
            testDir,
            fileChangelog = fileChangelog,
        )

        val expectedFiles = mutableListOf<String>()
        (1..100).forEach {
            expectedFiles += testFileFixed(it, padLength = 3)
        }
        expectedFiles += UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFixTrackNumbers9(@TempDir testDir: Path) {
        createFiles(testDir, 9)

        command.expect(
            "",
            testDir,
        )

        val expectedFiles = mutableListOf<String>()
        (1..9).forEach {
            expectedFiles += testFileFixed(it, padLength = 1)
        }
        expectedFiles += UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testDryFixTrackNumbers(@TempDir testDir: Path) {
        createFiles(testDir)

        val fileChangelog = mutableListOf<Pair<String, String>>()
        (1..9).forEach {
            fileChangelog += testFile(it) to testFileFixed(it)
        }

        command.expect(
            "--dry",
            testDir,
            dry = true,
            fileChangelog = fileChangelog,
        )

        val expectedFiles = mutableListOf<String>()
        (1..10).forEach {
            expectedFiles += testFile(it)
        }
        expectedFiles += UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFixTrackNumbersFileType(@TempDir testDir: Path) {
        createFiles(testDir)
        createFiles(testDir, extension = "wav")

        val fileChangelog = mutableListOf<Pair<String, String>>()
        (1..9).forEach {
            fileChangelog += testFile(it, "wav") to testFileFixed(it, "wav")
        }

        command.expect(
            "--filetype .wav",
            testDir,
            fileType = "wav",
            fileChangelog = fileChangelog,
        )

        val expectedFiles = mutableListOf<String>()
        (1..10).forEach {
            expectedFiles += testFile(it)
            expectedFiles += testFileFixed(it, "wav")
        }
        expectedFiles += UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testHelp() {
        command.expectHelp(HELP_TRACKNO)
    }
}