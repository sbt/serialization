sbt/serialization is no longer maintained as sbt 1 uses [sjson-new](https://github.com/eed3si9n/sjson-new).

sbt/serialization
=================

sbt serialization is an opinionated wrapper around [Scala pickling][pickling] focused on sbt's usage.
In particular it provides:

- JSON format that's nice
- static-only core picklers

  [pickling]: https://github.com/scala/pickling
