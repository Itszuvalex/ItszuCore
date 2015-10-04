package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.Saveable
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{FluidTankInfo, FluidStack, FluidTank}

/**
 * Created by Alex on 04.10.2015.
 */
trait TileMultiFluidTank extends TileFluidTank {
  @Saveable var tanks: Array[FluidTank] = defaultTanks
  override val tank = if (tanks.length > 0) tanks(0) else null

  def defaultTanks: Array[FluidTank]

  override def defaultTank: FluidTank = if (tanks.length > 0) tanks(0) else null

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean) = tank.fill(resource, doFill)

  def fill(id: Int, from: ForgeDirection, resource: FluidStack, doFill: Boolean) = tanks(id).fill(resource, doFill)

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tank.getFluid)) null
    else tank.drain(resource.amount, doDrain)
  }

  def drain(id: Int, from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tanks(id).getFluid)) null
    else tanks(id).drain(resource.amount, doDrain)
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean) = tank.drain(maxDrain, doDrain)

  def drain(id: Int, from: ForgeDirection, maxDrain: Int, doDrain: Boolean) = tanks(id).drain(maxDrain, doDrain)

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = Array(tank.getInfo)

  def getTankInfo(id: Int, from: ForgeDirection): Array[FluidTankInfo] = Array(tanks(id).getInfo)

}
