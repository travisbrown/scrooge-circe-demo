import io.circe.{ Decoder, Encoder }
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import shapeless.LabelledGeneric

package object demo {
  implicit def decodeMap[K, V](implicit
    d: Decoder[Map[K, V]]
  ): Decoder[scala.collection.Map[K, V]] = d.map(_.toMap)

  implicit def encodeMap[K, V](implicit
    e: Encoder[Map[K, V]]
  ): Encoder[scala.collection.Map[K, V]] = e.contramap(_.toMap)

  implicit def materializeStructGen[A]: LabelledGeneric[A] =
    macro materializeStructGen_impl[A]

  def materializeStructGen_impl[A: c.WeakTypeTag](c: Context): c.Tree = {
    import c.universe._

    val A = weakTypeOf[A]
    val I = A.companion.member(TypeName("Immutable")) match {
      case NoSymbol => c.abort(c.enclosingPosition, "Not a valid Scrooge class")
      case symbol => symbol.asType.toType
    }
    val N = appliedType(typeOf[NoPassthrough[_, _]].typeConstructor, A, I)

    q"""{
      val np = _root_.shapeless.the[$N]
      new _root_.shapeless.LabelledGeneric[$A] {
        type Repr = np.Without
        def to(t: $A): Repr = np.to(t.copy().asInstanceOf[$I])
        def from(r: Repr): $A = np.from(r)
      }
    }"""
  }
}

