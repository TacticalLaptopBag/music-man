package com.github.tacticallaptopbag

// Global
const val MUSIC_MAN_DIR = "MUSIC_MAN_DIR"
const val HELP_DRY = "Shows the result of a command without actually performing it"
const val HELP_FILETYPE =
    "The file extension to run this command on. " +
    "Leaving this blank will run the command on the most common file extension in the directory."
const val FILETYPE_FAIL = "Unable to guess file type! Please specify it with --filetype"
const val DRY = "DRY RUN: no files will be modified"

// Convert
const val HELP_CONVERT =
    "Converts an audio file or directory of audio files to a number of different formats, " +
    "specified by provided options."
const val HELP_CONVERT_SOURCE = "An audio file or directory containing audio files to be converted"
const val HELP_CONVERT_MP3PATH =
    "Path to place converted MP3 files in. If omitted, no files are converted to MP3. " +
    "If source is a single file and an .mp3 extension is provided, the output file is renamed to this. " +
    "e.g. `music/song.flac --mp3 my-song.mp3` will make an mp3 titled my-song.mp3 in the current directory, " +
    "while `music/song.flac --mp3 my-song` will make an mp3 titled song.flac in a new directory titled my-song."
const val HELP_CONVERT_SAMPLERATE = "The sample rate in Hz to use for the output file. Defaults to CD quality, 44100 Hz"
const val HELP_CONVERT_BITRATE =
    "The sample rate to use when converting. Higher numbers means higher quality, but bigger files. " +
    "Add a 'k', 'm', or 'g' suffix to specify kb, mb, or gb. No suffix assumes bits."
const val CONVERT_NONE_SPECIFIED = "No converters specified! Use `--help` to see what converters are available."
const val CONVERT_FAIL = "Failed to convert %s"

// Remove
const val HELP_RM = ""
const val HELP_RM_PREFIX = "Removes a given prefix from all files in the current directory"
const val HELP_RM_SUFFIX = "Removes a given suffix from all files in the current directory"

// Replace
const val HELP_REPL = "Replaces a substring with a given phrase in all files in the current directory"
const val HELP_REPL_PHRASE = "The substring to replace"
const val HELP_REPL_NEW_PHRASE =
    "The substring to use in place of the original phrase. " +
    "By default, this is the empty string."
const val HELP_REPL_REGEX =
    "When used, treats <phrase> as regex, rather than a substring. " +
    "All instances found by this regex will be replaced, so it's recommended to do a dry run first."

