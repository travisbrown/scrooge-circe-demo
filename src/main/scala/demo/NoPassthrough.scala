package demo

import com.twitter.scrooge.TFieldBlob
import shapeless._, shapeless.ops.record.Remove

trait NoPassthrough[A, I] {
  type With <: HList
  type Without

  def to(t: I): Without
  def from(r: Without): I
}

object NoPassthrough {
  private[this] type Pt = Map[Short, TFieldBlob]

  type Aux[A, I, With0 <: HList, Without0] = NoPassthrough[A, I] {
    type With = With0
    type Without = Without0
  }

  implicit def genNoPassthrough[A, I, With0 <: HList, Without0](implicit
    gen: LabelledGeneric.Aux[I, With0],
    rem: Remove.Aux[With0, Pt, (Pt, Without0)]
  ): Aux[A, I, With0, Without0] = new NoPassthrough[A, I] {
    type With = With0
    type Without = Without0
    def to(t: I): Without = rem(gen.to(t))._2
    def from(r: Without): I = gen.from(rem.reinsert((Map.empty, r)))
  }
}

