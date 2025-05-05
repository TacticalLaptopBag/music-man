import com.github.ajalt.clikt.testing.CliktCommandTestResult
import com.github.ajalt.clikt.testing.test
import com.github.tacticallaptopbag.BaseCommand
import com.github.tacticallaptopbag.DRY
import com.github.tacticallaptopbag.MUSIC_MAN_DIR
import java.nio.file.Path
import kotlin.test.*

fun BaseCommand.expectHelp(help: String) {
    val result = this.test(argv = "--help")
    assertEquals(0, result.statusCode)
    assertContains(
        result.output
            .replace("\n", "")
            .replace("  ", " "),
        help
    )
}

fun BaseCommand.expect(
    argv: String,
    testDir: Path? = null,
    statusCode: Int = 0,
    fileType: String? = null,
    dry: Boolean = false,
    fileChangelog: List<Pair<String, String>> = emptyList(),
    stdout: String? = null,
    stderr: String = "",
): CliktCommandTestResult {
    val result = this.test(
        argv = argv,
        envvars = if(testDir != null) mapOf(MUSIC_MAN_DIR to testDir.toString()) else emptyMap()
    )
    if(testDir != null) {
        assertEquals(testDir.toString(), this.baseDir)
    }
    assertEquals(statusCode, result.statusCode)

    if(fileType == null)
        assertNull(this.fileType)
    else
        assertEquals(fileType, this.fileType)

    if(dry)
        assertTrue(this.dry)
    else
        assertFalse(this.dry)

    if(fileChangelog.isNotEmpty()) {
        val dryString = if(dry) "$DRY\n" else ""
        val changelogStr = fileChangelog.joinToString("") {
            "${it.first} -> ${it.second}\n"
        }
        val expected = stdout?.format(dryString + changelogStr)
            ?: (dryString + changelogStr)

        assertEquals(expected, result.stdout)
    } else if(stdout != null) {
        assertEquals(stdout, result.stdout)
    }

    assertEquals(stderr, result.stderr)

    return result
}

fun Path.expectFiles(
    vararg expectedFiles: String
) {
    val files = this.toFile().list()?.toList()?.filterNotNull() ?: emptyList()
    assertEquals(expectedFiles.size, files.size)
    expectedFiles.forEach {
        assertContains(files, it)
    }
}

fun Path.createFile(fileName: String) =
    this.resolve(fileName).toFile().createNewFile()
