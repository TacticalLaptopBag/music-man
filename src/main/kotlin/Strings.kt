package com.github.tacticallaptopbag

// Global
const val HELP_DRY = ""
const val HELP_FILETYPE = ""
const val FILETYPE_FAIL = "Unable to guess file type! Please specify it with --filetype"
const val DRY = "DRY RUN: no files will be modified"

// Convert
const val HELP_CONVERT = "Converts an audio file or directory of audio files to a number of different formats, " +
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
const val HELP_RM_PREFIX = ""
const val HELP_RM_SUFFIX = ""

