package com.itszuvalex.itszulib.logistics

import com.itszuvalex.itszulib.api.core.Loc4

import scala.collection.mutable

/**
 * Created by Christopher on 7/29/2015.
 */
object LocationTracker {
  private val CHUNK_SIZE = 16
  private val trackerMap = mutable.HashMap[Int, mutable.HashMap[(Int, Int), mutable.HashSet[Loc4]]]()

  def trackLocation(loc: Loc4) = {
    trackerMap.getOrElseUpdate(loc.dim, mutable.HashMap[(Int, Int), mutable.HashSet[Loc4]]()).getOrElseUpdate(loc.chunkCoords, mutable.HashSet[Loc4]()) += loc
  }

  def removeLocation(loc: Loc4) = {
    trackerMap.get(loc.dim).flatMap(_.get(loc.chunkCoords).flatMap(_ -= loc))
  }

  def getLocationsInRange(loc: Loc4, range: Float): Iterable[Loc4] = {
    val chunkRadius = Math.ceil(range / CHUNK_SIZE).toInt
    val (cx, cz) = loc.chunkCoords
    for {
      i <- -chunkRadius to chunkRadius
      j <- -chunkRadius to chunkRadius
      locations <- getLocationsInChunk(loc.dim, (cx + i, cz + j))
      checkLoc <- locations if checkLoc.distSqr(loc) <= range * range
    } yield checkLoc
  }

  def getLocationsInRange(dim: Int, loc: (Float, Float, Float), range: Float): Iterable[Loc4] = {
    val (x, y, z) = loc
    val chunkRadius = Math.ceil(range / CHUNK_SIZE).toInt
    val (cx, cz) = (x.toInt >> 4, z.toInt >> 4)
    for {
      i <- -chunkRadius to chunkRadius
      j <- -chunkRadius to chunkRadius
      locations <- getLocationsInChunk(dim, (cx + i, cz + j))
      checkLoc <- locations if ((checkLoc.x - x) * (checkLoc.x - x) + (checkLoc.y - y) * (checkLoc.y - y) + (checkLoc.z - z) * (checkLoc.z - z)) <= range * range
    } yield checkLoc
  }

  def getLocationsInChunk(dim: Int, chunkLoc: (Int, Int)) =
    for {
      dimMap <- trackerMap.get(dim)
      chunk <- dimMap.get(chunkLoc)
    } yield chunk


}
