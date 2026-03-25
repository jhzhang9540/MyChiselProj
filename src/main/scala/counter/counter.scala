package counter

import chisel3._

import _root_.circt.stage.ChiselStage

class Counter4bit extends Module {
  val io = IO(new Bundle {
    val enable = Input(Bool())      // 计数使能，高电平有效
    val count  = Output(UInt(4.W))  // 4 位计数值
  })

  // 寄存器初始值为 0，使用隐式时钟和复位
  val regCount = RegInit(0.U(4.W))

  // 当使能有效时，计数器加 1（自动循环，因为 UInt(4.W) 会溢出）
  when(io.enable) {
    regCount := regCount + 1.U
  }

  // 输出当前计数值
  io.count := regCount
}

object Counter4bit extends App {
  ChiselStage.emitSystemVerilogFile(
    new Counter4bit,
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info", "-default-layer-specialization=enable")
  )
}