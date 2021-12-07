(ns net.bjoc.advent.util.misc)

(defn zip
  "Iterate over a sequence of collections, producing a sequence of an item from
  each one.

  Inspired by the Python zip function:
  https://docs.python.org/3/library/functions.html#zip"
  [& colls]
  (partition (count colls) (apply interleave colls)))

(def null-writer
  "A stream-writer that does nothing when written to. Like /dev/null."
  (proxy [java.io.Writer] [] (write [s])))

(defn find-first
  "Return the first element of a collection that satisfies a predicate."
  [pred coll]
  (first (filter pred coll)))

(defn single
  "Return the single value present in a collection. Throw an exception if the
  collection does not contain exactly one element."
  [coll]
  (when (empty? coll)
    (throw (ex-info "Expected a single value, but no values present."
                    {:coll coll})))
  (when-not (empty? (rest coll))
    (let [rest5 (take 5 coll)
          rest6 (take 6 coll)]
      (throw (ex-info "Expected a single value, but multiple values present."
                      (if (= rest5 rest6)
                        {:coll coll}
                        {:partial-coll rest5})))))
  (first coll))
