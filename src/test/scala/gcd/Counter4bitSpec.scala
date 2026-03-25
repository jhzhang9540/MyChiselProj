package counter

import chisel3._
import chisel3.experimental.BundleLiterals._
import chisel3.simulator.scalatest.ChiselSim
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class Counter4bitSpec extends AnyFreeSpec with Matchers with ChiselSim {
  "Counter4bit should count correctly" in {
    simulate(new Counter4bit) { dut =>
      // 复位初始化
      dut.reset.poke(true.B)
      dut.clock.step()
      dut.reset.poke(false.B)
      dut.clock.step()
      dut.io.enable.poke(false.B)

      // 初始计数值应为0
      dut.io.count.expect(0.U)

      // 使能计数，连续计数5个周期
      dut.io.enable.poke(true.B)
      for (i <- 1 to 5) {
        dut.clock.step()
        dut.io.count.expect(i.U)
      }

      // 停止使能，计数保持不变
      dut.io.enable.poke(false.B)
      dut.clock.step()
      dut.io.count.expect(5.U)
      dut.clock.step()
      dut.io.count.expect(5.U)

      // 重新使能，继续计数
      dut.io.enable.poke(true.B)
      for (i <- 6 to 15) {
        dut.clock.step()
        dut.io.count.expect(i.U)
      }

      // 下一个周期应溢出到0
      dut.clock.step()
      dut.io.count.expect(0.U)

      // 再次使能，从0开始计数
      dut.clock.step()
      dut.io.count.expect(1.U)
    }
  }
}