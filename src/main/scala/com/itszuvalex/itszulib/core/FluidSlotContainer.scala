package com.itszuvalex.itszulib.core

import java.util

import com.itszuvalex.itszulib.gui.FluidSlot

/**
  * Created by Alex on 10.10.2015.
  */
trait FluidSlotContainer {
   var fluidSlots: util.ArrayList[FluidSlot] = new util.ArrayList[FluidSlot]

   def addFluidSlotToContainer(slot: FluidSlot): FluidSlot = {
     slot.slotNumber = fluidSlots.size()
     fluidSlots.add(slot)
     slot
   }

   def getFluidSlot(id: Int): FluidSlot = fluidSlots.get(id)
 }
