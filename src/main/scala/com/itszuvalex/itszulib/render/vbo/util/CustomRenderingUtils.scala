package com.itszuvalex.itszulib.render.vbo.util

import java.io.{BufferedReader, InputStreamReader}

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl._
import org.lwjgl.util.vector.Matrix4f

/**
  * Created by Alex on 28.02.2016.
  */
object CustomRenderingUtils {

  def loadShader(shaderLoc: ResourceLocation, stype: Int): Int = {
    val reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft.getResourceManager.getResource(shaderLoc).getInputStream))
    var shaderId = 0
    var shaderSrc = ""

    reader.lines().toArray.foreach(str => shaderSrc += str + "\n")
    reader.close()

    shaderId = GL20.glCreateShader(stype)
    GL20.glShaderSource(shaderId, shaderSrc)
    GL20.glCompileShader(shaderId)
    shaderId
  }

  def setupWorldRenderShaders(): Int = {
    val vsId = loadShader(new ResourceLocation("itszulib", "shader/customrender_world.vert"), GL20.GL_VERTEX_SHADER)
    val fsId = loadShader(new ResourceLocation("itszulib", "shader/customrender_world.frag"), GL20.GL_FRAGMENT_SHADER)

    val pId = GL20.glCreateProgram()
    GL20.glAttachShader(pId, vsId)
    GL20.glAttachShader(pId, fsId)

    GL20.glBindAttribLocation(pId, 0, "in_Position")
    GL20.glBindAttribLocation(pId, 1, "in_Normal")
    GL20.glBindAttribLocation(pId, 2, "in_TextureCoord")

    GL20.glLinkProgram(pId)
    GL20.glValidateProgram(pId)

    pId
  }

  /**
    *
    * @param fov Horizontal field of view angle, in radians.
    * @param aspect Aspect ratio of the render space
    * @param near Near clipping plane
    * @param far Far clipping plane
    * @return
    */
  def perspectiveMatrix(fov: Float, aspect: Float, near: Float, far: Float): Matrix4f = {
    val mat = new Matrix4f()
    val h: Float = Math.tan(fov * 0.5f).toFloat * near
    val w: Float = h * aspect
    mat.m00 = near / w
    mat.m11 = near / h
    mat.m22 = -(far + near) / (far - near)
    mat.m23 = -1.0f
    mat.m32 = -2.0f * far * near / (far - near)
    mat.m33 = 0.0f
    mat
  }

}
