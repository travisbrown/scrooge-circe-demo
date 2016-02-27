# Shapeless-powered generic derivation for Scrooge

This project is a demonstration of some tools for using
[Shapeless][shapeless]'s generic programming machinery with Scala code generated
by [Scrooge][scrooge].

For example:

```scala
import cats.data.Xor, io.circe._, io.circe.generic.auto._

implicit val decodeKeyState: KeyDecoder[demo.State] =
  KeyDecoder.instance(demo.State.valueOf)

implicit val encodeKeyState: KeyEncoder[demo.State] =
  KeyEncoder.instance(_.toString)

implicit val decodeUnknownOpt: Decoder[demo.Opt.UnknownUnionField] =
  Decoder.instance(c =>
    Xor.left(DecodingFailure("Opt.UnknownUnionField", c.history))
  )

implicit val encodeUnknownOpt: Encoder[demo.Opt.UnknownUnionField] =
  Encoder.instance(_ => Json.Null)
```

And then:

```scala
scala> import io.circe.syntax._
import io.circe.syntax._

scala> val foo = Foo(
     |   List("a", "b"),
     |   Map(State.A -> "a"),
     |   Map(1 -> Opt.IntOpt(IntOpt(Some(10))))
     | )
foo: demo.Foo = Foo(List(a, b),Map(A -> a),Map(1 -> IntOpt(IntOpt(Some(10)))))

scala> val json = foo.asJson
json: io.circe.Json =
{
  "myList" : [
    "a",
    "b"
  ],
  "stateMap" : {
    "A" : "a"
  },
  "optMap" : {
    "1" : {
      "IntOpt" : {
        "intOpt" : {
          "int" : 10
        }
      }
    }
  }
}

scala> val andBack = Decoder[Foo].decodeJson(json)
andBack: io.circe.Decoder.Result[demo.Foo] = Right(Foo(Vector(a, b),Map(A -> a),Map(1 -> IntOpt(IntOpt(Some(10))))))
```

[shapeless]: https://github.com/milessabin/shapeless
[scrooge]: https://github.com/twitter/scrooge
