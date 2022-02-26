# map-directories

[![Scala CI](https://github.com/obruchez/map-directories/actions/workflows/scala.yml/badge.svg)](https://github.com/obruchez/map-directories/actions/workflows/scala.yml)

Try and map files from a source directory to a destination directory using file sizes and hashes (SHA-256). Hashes are computed "lazily", i.e. they're computed only when needed, for candidate files of the same size. Files can be renamed/moved between the two directories, i.e. filenames are completely ignored.
