package com.itszuvalex.itszulib.logistics

import com.itszuvalex.itszulib.api.core.Loc4
import com.itszuvalex.itszulib.logistics.LocationTracker._

import scala.collection.mutable

/**
 * Created by Christopher on 7/29/2015.
 */
object LocationTracker {
  private val CHUNK_SIZE = 16
}


class LocationTracker {
  private val trackerMap = mutable.HashMap[Int, mutable.HashMap[(Int, Int), mutable.HashSet[Loc4]]]()

  def trackLocation(loc: Loc4) = {
    trackerMap.getOrElseUpdate(loc.dim, mutable.HashMap[(Int, Int), mutable.HashSet[Loc4]]()).getOrElseUpdate(loc.chunkCoords, mutable.HashSet[Loc4]()) += loc
  }

  def removeLocation(loc: Loc4) = {
    trackerMap.get(loc.dim) match {
      case None =>
      case Some(dim) =>
        dim.get(loc.chunkCoords) match {
          case None =>
          case Some(chunk) =>
            chunk -= loc
            if (chunk.isEmpty) dim -= loc.chunkCoords
            if (dim.isEmpty) trackerMap -= loc.dim
        }
    }
  }

  def getLocationsInRange(loc: Loc4, range: Float): Iterable[Loc4] = for {
    chunk <- getChunkCoordsInRadius(loc.chunkCoords, Math.ceil(range / CHUNK_SIZE).toInt)
    checkLoc <- getLocationsInChunk(loc.dim, chunk)
    if checkLoc.distSqr(loc) <= range * range
  } yield checkLoc

  def getLocationsInRange(dim: Int, loc: (Float, Float, Float), range: Float): Iterable[Loc4] = {
    val (x, y, z) = loc
    for {
      chunk <- getChunkCoordsInRadius((x.toInt >> 4, z.toInt >> 4), Math.ceil(range / CHUNK_SIZE).toInt)
      checkLoc <- getLocationsInChunk(dim, chunk)
      if ((checkLoc.x - x) * (checkLoc.x - x) + (checkLoc.y - y) * (checkLoc.y - y) + (checkLoc.z - z) * (checkLoc.z - z)) <= range * range
    } yield checkLoc
  }

  def getLocationsInChunk(dim: Int, chunkLoc: (Int, Int)): Iterable[Loc4] = {
    trackerMap.getOrElse(dim, return Set[Loc4]()).getOrElse(chunkLoc, return Set[Loc4]())
  }


  def getChunkCoordsInRadius(loc: (Int, Int), radius: Int) = for {
    i <- -radius to radius
    j <- -radius to radius
  } yield (loc._1 + i, loc._2 + j)


  def clear(): Unit = {
    trackerMap.clear()
  }

}
