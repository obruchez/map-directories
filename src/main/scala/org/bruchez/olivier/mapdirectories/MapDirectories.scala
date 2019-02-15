package org.bruchez.olivier.mapdirectories

import java.nio.file.{Path, Paths}

object MapDirectories {
  def main(args: Array[String]): Unit = {
    if (args.length == 2) {
      mapDirectories(Paths.get(args(0)), Paths.get(args(1)))
    } else {
      println("Usage: MapDirectories src-directory dst-directory")
    }
  }

  def mapDirectories(srcDirectory: Path, dstDirectory: Path): Unit = {
    print(s"Parsing ${srcDirectory.toFile.getAbsolutePath}...")
    val srcFileInfos = allFileInfos(srcDirectory)
    println(s" -> file count: ${srcFileInfos.size}")
    println()

    print(s"Parsing ${dstDirectory.toFile.getAbsolutePath}...")
    val dstFileInfos = allFileInfos(dstDirectory)
    println(s" -> file count: ${dstFileInfos.size}")
    println()

    println(s"Mapping the two directories...")
    val mapping =
      srcFileInfos.map(srcFileInfo => srcFileInfo -> matchingFile(srcFileInfo, dstFileInfos))
    println()

    val (matchingFiles, nonMatchingFiles) = mapping.partition(_._2.isDefined)

    println(s"Matching file count: ${matchingFiles.size}")
    println()

    matchingFiles.flatMap(kv => kv._2.map(kv._1 -> _)).foreach {
      case (srcFileInfo, dstFileInfo) =>
        println(s" -   '${srcFileInfo.path.toFile.getAbsolutePath}'")
        println(s"   = '${dstFileInfo.path.toFile.getAbsolutePath}'")
    }
    println()

    println(s"Non-matching file count: ${nonMatchingFiles.size}")
    println()

    nonMatchingFiles.foreach {
      case (srcFileInfo, _) =>
        println(s" - '${srcFileInfo.path.toFile.getAbsolutePath}'")
    }
  }

  private def allFileInfos(directory: Path): Seq[FileInfo] =
    FileUtils
      .allFilesInPath(directory)
      .sortBy(_.toFile.getAbsolutePath)
      .map(FileInfo.apply)

  private def matchingFile(fileInfo: FileInfo, candidates: Seq[FileInfo]): Option[FileInfo] = {
    val sizeBasedCandidates = candidates.filter(_.size == fileInfo.size)

    if (sizeBasedCandidates.isEmpty) {
      // No candidate found using file size => no need to look at file hashes
      None
    } else {
      sizeBasedCandidates.find(_.sha256 == fileInfo.sha256)
    }
  }
}
