package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.Saveable
import com.itszuvalex.itszulib.core.TileEntityBase
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{IFluidHandler, FluidTankInfo, FluidStack, FluidTank}

/**
 * Created by Alex on 04.10.2015.
 */
trait TileMultiFluidTank extends TileEntityBase with IFluidHandler {
  @Saveable var tanks: Array[FluidTank] = defaultTanks

  def defaultTanks: Array[FluidTank]

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int

  def fill(id: Int, from: ForgeDirection, resource: FluidStack, doFill: Boolean) = tanks(id).fill(resource, doFill)

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack

  def drain(id: Int, from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tanks(id).getFluid)) null
    else tanks(id).drain(resource.amount, doDrain)
  }

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = {for (i <- 0 until tanks.length) yield tanks(i).getInfo}.toArray

}
