namespace java demo
#@namespace scala demo

enum State {
  A = 1
  B = 2
}

struct StringOpt {
  1: optional string str
}

struct IntOpt {
  1: optional i32 int
}

union Opt {
  1: StringOpt strOpt
  2: IntOpt intOpt
}

struct Foo {
  1: list<string> myList
  2: map<State, string> stateMap
  3: map<i32, Opt> optMap
}
