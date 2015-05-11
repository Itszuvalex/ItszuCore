package com.itszuvalex.itszulib.util

/**
 * Created by Christopher Harris (Itszuvalex) on 5/6/15.
 */
class ValueTracker[Val](size: Int) {
  val values = new Array[Val](size)

  def addValue(v: Val): Unit = {
    for (i <- (0 until size - 1).reverse) {
      values(i + 1) = values(i)
    }
    values(0) = v
  }
}
