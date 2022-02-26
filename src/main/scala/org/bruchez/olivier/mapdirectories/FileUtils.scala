package org.bruchez.olivier.mapdirectories

import com.google.common.hash._
import com.google.common.io.{Files => GoogleFiles}
import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

object FileUtils {
  def allFilesInPath(path: Path): Seq[Path] = {
    val fileVisitOptions = java.util.Arrays.asList(FileVisitOption.FOLLOW_LINKS)
    val fileVisitOptionSet = new java.util.HashSet[FileVisitOption](fileVisitOptions)

    val files = scala.collection.mutable.Buffer[Path]()

    val simpleFileVisitor: FileVisitor[Path] =
      new SimpleFileVisitor[Path]() {
        override def visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult = {
          files.append(file)
          FileVisitResult.CONTINUE
        }

        override def visitFileFailed(file: Path, e: IOException): FileVisitResult =
          FileVisitResult.SKIP_SUBTREE

        override def preVisitDirectory(
            directory: Path,
            attributes: BasicFileAttributes
        ): FileVisitResult = {
          // Hack to avoid security exceptions
          if (directory.toFile.listFiles().isEmpty) {
            FileVisitResult.SKIP_SUBTREE
          } else {
            FileVisitResult.CONTINUE
          }
        }
      }

    Files.walkFileTree(path, fileVisitOptionSet, Integer.MAX_VALUE, simpleFileVisitor)

    files.toSeq
  }
}

case class FileInfo(path: Path) {
  lazy val size: Long = path.toFile.length()

  lazy val sha256: String = GoogleFiles.asByteSource(path.toFile).hash(Hashing.sha256).toString
}
