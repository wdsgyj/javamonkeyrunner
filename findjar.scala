import java.io._
import java.util
import java.util.zip.{ZipEntry, ZipFile}
import scala.collection.JavaConversions

/**
 * Created with IntelliJ IDEA.
 * Date: 14-3-3
 */

def copyStream(inputStream: InputStream, outputStream: OutputStream) {
  val input = if (!inputStream.isInstanceOf[BufferedInputStream]) new BufferedInputStream(inputStream) else inputStream
  val output = if (!outputStream.isInstanceOf[BufferedOutputStream]) new BufferedOutputStream(outputStream) else outputStream
  val buf = new Array[Byte](512)

  def copyInternal(in: InputStream, out: OutputStream, bytes: Array[Byte]) {
    val count = in.read(bytes)
    if (count == -1) return
    else {
      out.write(bytes, 0, count)
      copyInternal(in, out, bytes)
    }
  }

  try {
    copyInternal(input, output, buf)
  } finally {
    input.close()
    output.close()
  }
}

def readStream(inputStream: InputStream): String = {
  val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
  copyStream(inputStream, stream)
  stream.toString
}

def analysisContent(content: String): util.Set[String] = {
  val rst = new util.HashSet[String]()
  val indexOfClassPath = content.indexOf("Class-Path")
  if (indexOfClassPath == -1) {
    return rst
  }
  var subContent = content.substring(indexOfClassPath)
  subContent = " " + subContent.substring(subContent.indexOf(':') + 1).trim
  val sb = new StringBuilder
  var isEnd = false
  for (line <- subContent.lines) {
    if (!line.startsWith(" ")) isEnd = true
    if (!isEnd) sb.append(line.trim)
  }
  val elements = sb.toString().split(' ')
  for (e <- elements) {
    rst.add(e)
  }
  rst
}

def readJarManifestFile(zipFile: ZipFile): util.Set[String] = {
  val rst = new util.HashSet[String]()
  val entry: ZipEntry = zipFile.getEntry("META-INF/MANIFEST.MF")
  if (entry == null) {
    println("没有找到 META-INF/MANIFEST.MF -> " + zipFile.getName)
    return rst
  }
  val stream: InputStream = zipFile.getInputStream(entry)
  try {
    val content: util.Set[String] = analysisContent(readStream(stream))
    if (content.size() == 0) {
      println("没有找到 Class-Path -> " + zipFile.getName)
    }
    return content
  } finally {
    stream.close()
    zipFile.close()
  }
  rst
}

val scannedSet = new util.HashSet[String]()
val scanNeeded = new util.HashSet[String]()

def process(dir: File) {
  val buf = new util.HashSet[String]()
  for (string <- JavaConversions.collectionAsScalaIterable(scanNeeded)) {
    val file: File = new File(dir, string)
    if (file.isFile) {
      buf.addAll(readJarManifestFile(new ZipFile(file)))
    } else println("没有找到 " + file.getName)
  }
  scannedSet.addAll(scanNeeded)
  scanNeeded.clear()
  scanNeeded.addAll(buf)
  scanNeeded.removeAll(scannedSet)
  if (scanNeeded.size() != 0) process(dir)
}

val file = new File(args(0))
scanNeeded.add(file.getName)
val parent = file.getParentFile
process(parent)

for (string <- JavaConversions.collectionAsScalaIterable(scannedSet)) {
  val f: File = new File(parent, string)
  // 拷贝到当前目录下的 libs 中
  copyStream(new FileInputStream(f), new FileOutputStream(new File("libs", string)))
  println(f.getAbsolutePath)
}
