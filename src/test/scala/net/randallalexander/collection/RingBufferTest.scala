package net.randallalexander.collection

import cats.implicits._

class RingBufferTest extends munit.FunSuite {
  //todo:add tests for size 0
  test("RingBuffer filled returns an accurate size and values") {
    val ringBuffer = RingBuffer[Int](3)
    assert(ringBuffer.available() == 0)
    assert(ringBuffer.values().toList == List.empty[Int])
    assert(ringBuffer.peak() == None)

    ringBuffer.append(1)
    assert(ringBuffer.available() == 1)
    assert(ringBuffer.values().toList == List(1))
    assert(ringBuffer.peak() == 1.some)

    ringBuffer.append(2)
    assert(ringBuffer.available() == 2)
    assert(ringBuffer.values().toList == List(1, 2))
    assert(ringBuffer.peak() == 1.some)

    ringBuffer.append(3)
    assert(ringBuffer.available() == 3)
    assert(ringBuffer.values().toList == List(1, 2, 3))
    assert(ringBuffer.peak() == 1.some)

    ringBuffer.append(4)
    assert(ringBuffer.available() == 3)
    assert(ringBuffer.values().toList == List(2, 3, 4))
    assert(ringBuffer.peak() == 2.some)

    ringBuffer.append(5)
    assert(ringBuffer.available() == 3)
    assert(ringBuffer.values().toList == List(3, 4, 5))
    assert(ringBuffer.peak() == 3.some)

    ringBuffer.append(6)
    assert(ringBuffer.available() == 3)
    assert(ringBuffer.values().toList == List(4, 5, 6))
    assert(ringBuffer.peak() == 4.some)

    ringBuffer.append(7)
    assert(ringBuffer.available() == 3)
    assert(ringBuffer.values().toList == List(5, 6, 7))
    assert(ringBuffer.peak() == 5.some)
  }

  test("pop removes elements") {
    val ringBuffer = RingBuffer[Int](3)
    val full = ringBuffer.append(1).append(2).append(3)
    assert(full.available() == 3)

    assert(full.pop() == 1.some)
    assert(full.available() == 2)
    assert(full.values().toList == List(2, 3))

    assert(full.pop() == 2.some)
    assert(full.available() == 1)
    assert(full.values().toList == List(3))

    assert(full.pop() == 3.some)
    assert(full.available() == 0)
    assert(full.values().toList == List.empty)

    val wrapped = full.append(4).append(5).append(6).append(7)

    assert(wrapped.pop() == 5.some)
    assert(full.available() == 2)
    assert(full.values().toList == List(6, 7))

    assert(wrapped.pop() == 6.some)
    assert(full.available() == 1)
    assert(full.values().toList == List(7))

    assert(wrapped.pop() == 7.some)
    assert(full.available() == 0)
    assert(full.values().toList == List.empty)

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

    assert(ringBuffer.available() == 2)

    assert(ringBuffer.peak() == 6.some)
    assert(ringBuffer.available() == 2)
    assert(ringBuffer.values().toList == List(6, 7))
  }
}
