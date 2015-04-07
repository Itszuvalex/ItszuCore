package com.itszuvalex.itszucore.core.traits.tile

import com.itszuvalex.itszucore.api.core.Saveable
import com.itszuvalex.itszucore.core.TileEntityBase
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 * Created by Chris on 11/30/2014.
 */
trait FluidTank extends TileEntityBase with IFluidHandler {
  @Saveable val tank = defaultTank

  def defaultTank: IFluidTank

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean) = tank.fill(resource, doFill)

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tank.getFluid)) null
    else tank.drain(resource.amount, doDrain)
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean) = tank.drain(maxDrain, doDrain)

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = Array(tank.getInfo)
}
