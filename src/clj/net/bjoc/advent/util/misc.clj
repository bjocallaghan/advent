(ns net.bjoc.advent.util.misc)

(defn zip
  "Iterate over a sequence of collections, producing a sequence of an item from
  each one.

  Inspired by the Python zip function:
  https://docs.python.org/3/library/functions.html#zip"
  [& colls]
  (partition (count colls) (apply interleave colls)))
