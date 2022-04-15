package net.randallalexander.collection

import cats.Show
import cats.implicits.*

import scala.reflect.ClassTag

//NOT THREAD SAFE
class RingBuffer[T: ClassTag](private val _size: Int) {
  private val _internalArray = new Array[Any](_size)
  private var _start: Int = -1
  private var _next: Int = 0

  def append(entry: T): RingBuffer[T] = {
    _internalArray.update(_next, entry)
    incNext()
    this
  }

  def available(): Int = {
    if (_start === -1) {
      0
    } else if (_start === _next) {
      _size
    } else if (_start > _next) {
      (_size - _start) + _next
    } else {
      _next - _start
    }
  }

  //expensive call
  def values(): Array[T] = {
    if (_start == -1) {
      Array.empty[T]
    } else if (_next <= _start) {
      //is there a better way to address the types than to use .map so an extra array is not allocated
      val startList = _internalArray.slice(_start, _size).map(_.asInstanceOf[T])
      val endList = _internalArray.slice(0, _next).map(_.asInstanceOf[T])
      val newValue = new Array[T](available())
      var next = 0
      for (i <- _start until _size) {
        newValue.update(next, _internalArray(i).asInstanceOf[T])
        next = next + 1
      }
      for (i <- 0 until _next) {
        newValue.update(next, _internalArray(i).asInstanceOf[T])
        next = next + 1
      }
      newValue
    } else {
      val newValue = new Array[T](available())
      var next = 0
      for (i <- _start until _next) {
        newValue.update(next, _internalArray(i).asInstanceOf[T])
        next = next + 1
      }
      newValue
    }
  }

  private def incNext(): Unit = {
    if (_start == -1) {
      _start = 0
      _next = 1
    } else {
      if (_next == _start) {
        incStart()
      }
      if (_next + 1 === _size) {
        _next = 0
      } else {
        _next = _next + 1
      }
    }
    ()
  }

  private def incStart(): Unit = {
    if (_start + 1 === _size) {
      _start = 0
    } else {
      _start = _start + 1
    }
    ()
  }

  def reset(): RingBuffer[T] = {
    _start = -1
    _next = 0
    for (i <- 0 until _internalArray.length) {
      _internalArray.update(i, null)
      ()
    }
    this
  }

  def pop(): Option[T] = {
    val result = peak()
    if (_start =!= -1) {
      val currAvail = this.available()
      if (currAvail - 1 <= 0) {
        reset()
      } else {
        //remove reference to element
        _internalArray.update(_start, null)
        incStart()
      }
    }
    result
  }

  def peak(): Option[T] = {
    if (_start == -1) {
      none[T]
    } else {
      _internalArray(_start).asInstanceOf[T].some
    }
  }

  def debugShow(): String = {
    s"start=${_start};next=${_next};internalArray=[${_internalArray.mkString(",")}]"
  }
}

object RingBuffer {
  implicit def show[T: ClassTag]: Show[RingBuffer[T]] =
    new Show[RingBuffer[T]] {
      def show(instance: RingBuffer[T]): String = {
        instance.debugShow()
      }
    }
}
