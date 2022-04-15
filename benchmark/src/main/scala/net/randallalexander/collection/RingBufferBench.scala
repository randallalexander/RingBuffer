package net.randallalexander.collection

import org.openjdk.jmh.annotations.{
  Benchmark,
  Fork,
  Level,
  Measurement,
  OutputTimeUnit,
  Param,
  Scope,
  Setup,
  State,
  TearDown,
  Warmup
}

import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
class RingBufferBench {
  @Param(Array("1000", "2000", "3000", "4000"))
  var baseSize: Int = _
  @Param(Array("1", "2", "3"))
  var appendMultiplier: Int = _
  var ringBuffer: RingBuffer[Int] = _
  var values: List[Int] = _

  @Setup(Level.Iteration)
  def up: Unit = {
    ringBuffer = new RingBuffer[Int](baseSize)
    val numberOfEntries = baseSize * appendMultiplier
    values = Range.inclusive(0, numberOfEntries).toList
    ()
  }

  @TearDown
  def down: Unit = {
    ringBuffer = ringBuffer.reset()
    values = List.empty[Int]
    ()
  }

  @Benchmark
  def append(): Unit = {
    values.foreach { cnt =>
      ringBuffer.append(cnt)
      ()
    }
  }
}
