package net.randallalexander.collection

import cats.implicits._

class RingBufferTest extends munit.FunSuite {
  //todo:add tests for size 0
  test("RingBuffer filled returns an accurate size and values") {
    val ringBuffer = RingBuffer[Int](3)
    assertEquals(ringBuffer.available(), 0)
    assertEquals(ringBuffer.values().toList, List.empty[Int])
    assertEquals(ringBuffer.peak(), None)

    ringBuffer.append(1)
    assertEquals(ringBuffer.available(), 1)
    assertEquals(ringBuffer.values().toList, List(1))
    assertEquals(ringBuffer.peak(), 1.some)

    ringBuffer.append(2)
    assertEquals(ringBuffer.available(), 2)
    assertEquals(ringBuffer.values().toList, List(1, 2))
    assertEquals(ringBuffer.peak(), 1.some)

    ringBuffer.append(3)
    assertEquals(ringBuffer.available(), 3)
    assertEquals(ringBuffer.values().toList, List(1, 2, 3))
    assertEquals(ringBuffer.peak(), 1.some)

    ringBuffer.append(4)
    assertEquals(ringBuffer.available(), 3)
    assertEquals(ringBuffer.values().toList, List(2, 3, 4))
    assertEquals(ringBuffer.peak(), 2.some)

    ringBuffer.append(5)
    assertEquals(ringBuffer.available(), 3)
    assertEquals(ringBuffer.values().toList, List(3, 4, 5))
    assertEquals(ringBuffer.peak(), 3.some)

    ringBuffer.append(6)
    assertEquals(ringBuffer.available(), 3)
    assertEquals(ringBuffer.values().toList, List(4, 5, 6))
    assertEquals(ringBuffer.peak(), 4.some)

    ringBuffer.append(7)
    assertEquals(ringBuffer.available(), 3)
    assertEquals(ringBuffer.values().toList, List(5, 6, 7))
    assertEquals(ringBuffer.peak(), 5.some)
  }

  test("pop removes elements") {
    val ringBuffer = RingBuffer[Int](3)
    val full = ringBuffer.append(1).append(2).append(3)
    assertEquals(full.available(), 3)

    assertEquals(full.pop(), 1.some)
    assertEquals(full.available(), 2)
    assertEquals(full.values().toList, List(2, 3))

    assertEquals(full.pop(), 2.some)
    assertEquals(full.available(), 1)
    assertEquals(full.values().toList, List(3))

    assertEquals(full.pop(), 3.some)
    assertEquals(full.available(), 0)
    assertEquals(full.values().toList, List.empty)

    val wrapped = full.append(4).append(5).append(6).append(7)

    assertEquals(wrapped.pop(), 5.some)
    assertEquals(full.available(), 2)
    assertEquals(full.values().toList, List(6, 7))

    assertEquals(wrapped.pop(), 6.some)
    assertEquals(full.available(), 1)
    assertEquals(full.values().toList, List(7))

    assertEquals(wrapped.pop(), 7.some)
    assertEquals(full.available(), 0)
    assertEquals(full.values().toList, List.empty)
  }

  test("combining append/pop still maintains internal state") {
    val ringBuffer = RingBuffer[Int](3)
    ringBuffer.append(1)
    ringBuffer.pop()
    ringBuffer.pop()
    ringBuffer.append(2)
    ringBuffer.append(3)
    ringBuffer.append(4)
    ringBuffer.append(5)
    ringBuffer.pop()
    ringBuffer.append(6)
    ringBuffer.pop()
    ringBuffer.pop()
    ringBuffer.append(7)

    assertEquals(ringBuffer.available(), 2)

    assertEquals(ringBuffer.peak(), 6.some)
    assertEquals(ringBuffer.available(), 2)
    assertEquals(ringBuffer.values().toList, List(6, 7))
  }
}
