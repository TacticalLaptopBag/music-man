import com.github.tacticallaptopbag.FORMAT_INCOMPLETE
import com.github.tacticallaptopbag.strmanip.FormatCommand
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.test.Test

class FormatCommandTest {
    private val command = FormatCommand()
    private val qobuzPrefix = (1..9).random()
    private val qobuzSuffix = (1..9).random()

    private fun testBandcampFile(number: Int, extension: String = "flac"): String =
        "Artist With Words - Album With Words - ${number.toString().padStart(2, '0')} Title With Words.$extension"

    private fun testQobuzFile(number: Int, extension: String = "flac"): String =
        "${qobuzPrefix}_${number}_Artist-With-Words_Title-With-Words_${qobuzSuffix}.$extension"

    private fun testFileFormatted(number: Int, extension: String = "flac"): String =
        "${number.toString().padStart(2, '0')} Title With Words.$extension"

    companion object {
        private const val UNRELATED_FILE = "unrelated file.flac"
    }

    private fun createBandcampFiles(testDir: Path, createUnrelated: Boolean = false) {
        (1..10).forEach {
            testDir.createFile(testBandcampFile(it))
        }
        if(createUnrelated) {
            testDir.createFile(UNRELATED_FILE)
        }
    }


    private fun createQobuzFiles(testDir: Path, createUnrelated: Boolean = false) {
        (1..10).forEach {
            testDir.createFile(testQobuzFile(it))
        }
        if(createUnrelated) {
            testDir.createFile(UNRELATED_FILE)
        }
    }
    @Test
    fun testFormatBandcamp(@TempDir testDir: Path) {
        createBandcampFiles(testDir)

        command.expect(
            "bandcamp --artist \"Artist With Words\" --album \"Album With Words\"",
            testDir,
            fileChangelog = (1..10).map {
                testBandcampFile(it) to testFileFormatted(it)
            },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatBandcampIncomplete(@TempDir testDir: Path) {
        createBandcampFiles(testDir, createUnrelated = true)

        command.expect(
            "bandcamp --artist \"Artist With Words\" --album \"Album With Words\"",
            testDir,
            fileChangelog = (1..10).map {
                testBandcampFile(it) to testFileFormatted(it)
            },
            stdout = "%s${FORMAT_INCOMPLETE.format(testDir.absolutePathString())}\n"
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) } + UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatBandcampAutomaticArtistAndAlbum(@TempDir testDir: Path) {
        val albumDir = testDir.resolve("Artist With Words/Album With Words").createDirectories()
        createBandcampFiles(albumDir)

        command.expect(
            "bandcamp",
            albumDir,
            fileChangelog = (1..10).map {
                testBandcampFile(it) to testFileFormatted(it)
            },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        albumDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatBandcampAutomaticArtist(@TempDir testDir: Path) {
        val albumDir = testDir.resolve("Artist With Words/Album Using Different Name").createDirectories()
        createBandcampFiles(albumDir)

        command.expect(
            "bandcamp --album \"Album With Words\"",
            albumDir,
            fileChangelog = (1..10).map {
                testBandcampFile(it) to testFileFormatted(it)
            },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        albumDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatBandcampAutomaticAlbum(@TempDir testDir: Path) {
        val albumDir = testDir.resolve("Artist Using Different Name/Album With Words").createDirectories()
        createBandcampFiles(albumDir)

        command.expect(
            "bandcamp --artist \"Artist With Words\"",
            albumDir,
            fileChangelog = (1..10).map {
                testBandcampFile(it) to testFileFormatted(it)
            }.sortedBy { it.second },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        albumDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatQobuz(@TempDir testDir: Path) {
        createQobuzFiles(testDir)

        var fileChangelog = (1..10).map {
            testQobuzFile(it) to testFileFormatted(it)
        }
        fileChangelog = fileChangelog.sortedBy { it.first }

        command.expect(
            "qobuz --artist \"Artist With Words\" --album \"Album With Words\"",
            testDir,
            fileChangelog = fileChangelog,
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatQobuzIncomplete(@TempDir testDir: Path) {
        createQobuzFiles(testDir, createUnrelated = true)

        command.expect(
            "qobuz --artist \"Artist With Words\" --album \"Album With Words\"",
            testDir,
            fileChangelog = (1..10).map {
                testQobuzFile(it) to testFileFormatted(it)
            }.sortedBy { it.first },
            stdout = "%s${FORMAT_INCOMPLETE.format(testDir.absolutePathString())}\n"
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) } + UNRELATED_FILE
        testDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatQobuzAutomaticArtistAndAlbum(@TempDir testDir: Path) {
        val albumDir = testDir.resolve("Artist With Words/Album With Words").createDirectories()
        createQobuzFiles(albumDir)

        command.expect(
            "qobuz",
            albumDir,
            fileChangelog = (1..10).map {
                testQobuzFile(it) to testFileFormatted(it)
            }.sortedBy { it.first },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        albumDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatQobuzAutomaticArtist(@TempDir testDir: Path) {
        val albumDir = testDir.resolve("Artist With Words/Album Using Different Name").createDirectories()
        createQobuzFiles(albumDir)

        command.expect(
            "qobuz --album \"Album With Words\"",
            albumDir,
            fileChangelog = (1..10).map {
                testQobuzFile(it) to testFileFormatted(it)
            }.sortedBy { it.first },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        albumDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }

    @Test
    fun testFormatQobuzAutomaticAlbum(@TempDir testDir: Path) {
        val albumDir = testDir.resolve("Artist Using Different Name/Album With Words").createDirectories()
        createQobuzFiles(albumDir)

        command.expect(
            "qobuz --artist \"Artist With Words\"",
            albumDir,
            fileChangelog = (1..10).map {
                testQobuzFile(it) to testFileFormatted(it)
            }.sortedBy { it.first },
        )

        val expectedFiles = (1..10).map{ testFileFormatted(it) }
        albumDir.expectFiles(expectedFiles = expectedFiles.toTypedArray())
    }
}