/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package codetree

import com.sparqline.codetree.mmap.Tag
import org.apache.commons.lang3.SerializationUtils

import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Isaac Griffith
 *
 */
class MemberMMap {

    private static int size = Integer.MAX_VALUE // 2GB
    private static int count = 0
    private static MappedByteBuffer map
    private static RandomAccessFile file

    static def init() {
        // Construct the file that will act as memory
        file = new RandomAccessFile("largefile.txt", "rw")

        // Construct the mapped byte buffer for the file
        map = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size)
    }

    static def close() {
        file.close()
        Path p = Paths.get("largefile.txt")
        Files.deleteIfExists(p)
    }

    static def getCount() {
        return count
    }

    static Tag store(AbstractNode node)
    {
        count++
        byte[] data = SerializationUtils.serialize(node)
        int index = map.position()
        map.put(data)
        int length = data.length

        return new Tag(index, length)
    }

    static CodeNode retrieve(Tag tag)
    {
        map.position(tag.getPosition())
        byte[] data = new byte[tag.getLength()]
        map.get(data)
        return (CodeNode) SerializationUtils.deserialize(data)
    }
}
