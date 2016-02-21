package com.itszuvalex.itszulib.render.vbo.obj

import java.io.{BufferedReader, InputStreamReader}
import java.nio.{FloatBuffer, ShortBuffer}
import java.util.regex.{Matcher, Pattern}

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.opengl._

import scala.collection.mutable

/**
  * Created by Alex on 19.02.2016.
  */
object VBOObjLoader {

  val V_PATTERN = Pattern.compile("^v +(?<x>\\-?\\d+(?:\\.\\d+)?) (?<y>\\-?\\d+(?:\\.\\d+)?) (?<z>\\-?\\d+(?:\\.\\d+)?)(?: (?<w>\\-?\\d+(?:\\.\\d+)?))? *$", Pattern.MULTILINE)
  val VN_PATTERN = Pattern.compile("^vn +(?<x>\\-?\\d+(?:\\.\\d+)?) (?<y>\\-?\\d+(?:\\.\\d+)?) (?<z>\\-?\\d+(?:\\.\\d+)?) *$", Pattern.MULTILINE)
  val VT_PATTERN = Pattern.compile("^vt +(?<u>\\-?\\d+(?:\\.\\d+)?) (?<v>\\-?\\d+(?:\\.\\d+)?)(?: (?<w>\\-?\\d+(?:\\.\\d+)?))? *$", Pattern.MULTILINE)
  val FACE_PATTERN_V = Pattern.compile("^f +(?<v1>\\d+) (?<v2>\\d+) (?<v3>\\d+) *$", Pattern.MULTILINE)
  val FACE_PATTERN_VT = Pattern.compile("^f +(?<v1>\\d+)\\/(?<vt1>\\d+) (?<v2>\\d+)\\/(?<vt2>\\d+) (?<v3>\\d+)\\/(?<vt3>\\d+) *$", Pattern.MULTILINE)
  val FACE_PATTERN_VN = Pattern.compile("^f +(?<v1>\\d+)\\/\\/(?<vn1>\\d+) (?<v2>\\d+)\\/\\/(?<vn2>\\d+) (?<v3>\\d+)\\/\\/(?<vn3>\\d+) *$", Pattern.MULTILINE)
  val FACE_PATTERN_VTN = Pattern.compile("^f +(?<v1>\\d+)\\/(?<vt1>\\d+)\\/(?<vn1>\\d+) (?<v2>\\d+)\\/(?<vt2>\\d+)\\/(?<vn2>\\d+) (?<v3>\\d+)\\/(?<vt3>\\d+)\\/(?<vn3>\\d+) *$", Pattern.MULTILINE)
  val GROUP_PATTERN = Pattern.compile("^[go] +(?<name>\\S+) *$", Pattern.MULTILINE)

  def load(model: ResourceLocation): WavefrontObject = {
    var vertPositions = mutable.IndexedSeq[VertPos](null)
    var vertTexCoords = mutable.IndexedSeq[VertTexture](null)
    var vertNormals = mutable.IndexedSeq[VertNormal](null)
    var vertCombos = mutable.IndexedSeq[Array[Float]]()
    var vertComboIndex = mutable.Map[(Int, Int, Int), Short]()
    var currentData = mutable.IndexedSeq[Float]()
    var currentIndices = mutable.IndexedSeq[Short]()
    var currentGroup: Group = null
    var nextGroup: Group = null
    var lastGroupStart = 0
    var matcher: Matcher = null
    var groupMatcher: Matcher = null
    var vnDefined, vtDefined = false

    val obj = new WavefrontObject

    val reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft.getResourceManager.getResource(model).getInputStream))
    var objFile: String = ""
    reader.lines().toArray.foreach(str => objFile += str + "\n")
    reader.close()

    matcher = V_PATTERN.matcher(objFile)
    while (matcher.find()) {
      vertPositions :+= new VertPos(matcher.group("x").toFloat, matcher.group("y").toFloat, matcher.group("z").toFloat)
    }
    matcher = VT_PATTERN.matcher(objFile)
    while (matcher.find()) {
      vertTexCoords :+= new VertTexture(matcher.group("u").toFloat, matcher.group("v").toFloat)
    }
    matcher = VN_PATTERN.matcher(objFile)
    while (matcher.find()) {
      vertNormals :+= new VertNormal(matcher.group("x").toFloat, matcher.group("y").toFloat, matcher.group("z").toFloat)
    }

    matcher = FACE_PATTERN_V.matcher(objFile)
    if (!matcher.find()) {
      matcher = FACE_PATTERN_VT.matcher(objFile)
      if (!matcher.find()) {
        matcher = FACE_PATTERN_VN.matcher(objFile)
        if (!matcher.find()) {
          matcher = FACE_PATTERN_VTN.matcher(objFile)
          vtDefined = true
          vnDefined = true
        } else vnDefined = true
      } else vtDefined = true
    }
    matcher.reset()

    groupMatcher = GROUP_PATTERN.matcher(objFile)
    groupMatcher.find()
    lastGroupStart = groupMatcher.start()
    currentGroup = new Group(groupMatcher.group("name"), vnDefined, vtDefined)
    while (groupMatcher.find()) {
      nextGroup = new Group(groupMatcher.group("name"), vnDefined, vtDefined)
      matcher.region(lastGroupStart, groupMatcher.start())
      lastGroupStart = groupMatcher.start()

      while (matcher.find()) {
        for (i <- 1 to 3) {
          val vInd = matcher.group("v" + i).toInt
          val vtInd = if (vtDefined) matcher.group("vt" + i).toInt else 0
          val vnInd = if (vnDefined) matcher.group("vn" + i).toInt else 0
          if (vertComboIndex.isDefinedAt((vInd, vtInd, vnInd))) {
            val comboInd = vertComboIndex((vInd, vtInd, vnInd))
            currentIndices :+= comboInd
          } else {
            var vertCombo = vertPositions(vInd).appendable()
            if (vnDefined) vertCombo ++= vertNormals(vnInd).appendable()
            if (vtDefined) vertCombo ++= vertTexCoords(vtInd).appendable()
            vertCombos :+= vertCombo
            val comboInd = (vertCombos.length - 1).toShort
            vertComboIndex((vInd, vtInd, vnInd)) = comboInd
            currentData ++= vertCombos(comboInd)
            currentIndices :+= comboInd
          }
        }
      }
      matcher.reset()
      val dataBuffer = BufferUtils.createFloatBuffer(currentData.length)
      dataBuffer.put(currentData.toArray)
      dataBuffer.flip()
      currentGroup.setDataBuffer(dataBuffer)
      val indBuffer = BufferUtils.createShortBuffer(currentIndices.length)
      indBuffer.put(currentIndices.toArray)
      indBuffer.flip()
      currentGroup.setIndexBuffer(indBuffer)
      currentGroup.compileVAO()
      obj.addGroup(currentGroup)

      currentGroup = nextGroup
      currentData = mutable.IndexedSeq[Float]()
      currentIndices = mutable.IndexedSeq[Short]()
    }

    obj
  }

}

class VertPos(var x: Float = 0,
              var y: Float = 0,
              var z: Float = 0) {

  def setPos(x: Float, y: Float, z: Float): VertPos = {
    this.x = x
    this.y = y
    this.z = z
    this
  }

  def appendable(): Array[Float] = Array[Float](x, y, z, 1f)

}

class VertNormal(var x: Float = 0,
                 var y: Float = 1f,
                 var z: Float = 0) {

  def setNormal(x: Float, y: Float, z: Float): VertNormal = {
    this.x = x
    this.y = y
    this.z = z
    this
  }

  def appendable(): Array[Float] = Array[Float](x, y, z)

}

class VertTexture(var u: Float = 0,
                  var v: Float = 0) {

  def setTexCoords(u: Float, v: Float): VertTexture = {
    this.u = u
    this.v = v
    this
  }

  def appendable(): Array[Float] = Array[Float](u, v)

}

class Group(val name: String, val vn: Boolean, val vt: Boolean) {

  var stride = 16
  var vnOffset = 16
  var vtOffset = 16
  (vn, vt) match {
    case (true, true) => stride += 20; vtOffset += 12
    case (true, false) => stride += 12
    case (false, true) => stride += 8
    case _ =>
  }

  var vertDataBuffer: FloatBuffer = null
  var indexBuffer: ShortBuffer = null
  var vaoId = 0
  var vboId = 0
  var vboiId = 0

  def setDataBuffer(buf: FloatBuffer): Unit = {
    vertDataBuffer = buf
  }

  def setIndexBuffer(buf: ShortBuffer): Unit = {
    indexBuffer = buf
  }

  def compileVAO(): Unit = {

    vaoId = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vaoId)

    vboId = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertDataBuffer, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, stride, 0)
    (vn, vt) match {
      case (true, true) =>
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, true, stride, vnOffset)
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, vtOffset)
      case (true, false) =>
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, true, stride, vnOffset)
      case (false, true) =>
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, vtOffset)
      case _ =>
    }
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

    GL30.glBindVertexArray(0)

    vboiId = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW)
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
  }

  def render(): Unit = {
    GL30.glBindVertexArray(vaoId)
    GL20.glEnableVertexAttribArray(0)
    if (vn || vt) GL20.glEnableVertexAttribArray(1)
    if (vn && vt) GL20.glEnableVertexAttribArray(2)

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId)

    GL11.glDrawElements(GL11.GL_TRIANGLES, indexBuffer.capacity(), GL11.GL_UNSIGNED_SHORT, 0)

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)

    GL20.glDisableVertexAttribArray(0)
    GL20.glDisableVertexAttribArray(1)
    GL20.glDisableVertexAttribArray(2)
    GL30.glBindVertexArray(0)
  }

}

class WavefrontObject {

  var groups = mutable.Map[String, Group]()

  def addGroup(group: Group): Unit = {
    groups(group.name) = group
  }

  def renderAll(): Unit = {
    groups.foreach(gr => gr._2.render())
  }

  def render(name: String*): Unit = {
    name.foreach(str => groups(str).render())
  }

  def renderExcept(name: String*): Unit = {
    groups.foreach(gr => if (!name.contains(gr._1)) gr._2.render())
  }

}
