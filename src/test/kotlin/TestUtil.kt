import com.github.ajalt.clikt.testing.CliktCommandTestResult
import com.github.ajalt.clikt.testing.test
import com.github.tacticallaptopbag.BaseCommand
import com.github.tacticallaptopbag.DRY
import com.github.tacticallaptopbag.MUSIC_MAN_DIR
import java.nio.file.Path
import kotlin.test.*

fun BaseCommand.expect(
    argv: String,
    testDir: Path,
    statusCode: Int = 0,
    fileType: String? = null,
    dry: Boolean = false,
    fileChangelog: List<Pair<String, String>> = emptyList(),
    stdout: String? = null,
    stderr: String = "",
): CliktCommandTestResult {
    assertFalse(
        stdout != null && fileChangelog.isNotEmpty(),
    "Cannot expect both a stdout and a file changelog"
    )

    val result = this.test(argv = argv, envvars = mapOf(MUSIC_MAN_DIR to testDir.toString()))
    assertEquals(testDir.toString(), this.baseDir)
    assertEquals(statusCode, result.statusCode)

    if(fileType == null)
        assertNull(this.fileType)
    else
        assertEquals(fileType, this.fileType)

    if(dry)
        assertTrue(this.dry)
    else
        assertFalse(this.dry)

    if(stdout == null) {
        val dryString = if(dry) "$DRY\n" else ""
        val changelogStr = fileChangelog.joinToString("") {
            "${it.first} -> ${it.second}\n"
        }

        assertEquals(
            dryString + changelogStr,
            result.stdout
        )
    } else {
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
