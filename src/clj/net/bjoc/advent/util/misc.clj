(ns net.bjoc.advent.util.misc
  (:require [clojure.string :as str]))

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

(defn take-until
  "Take items from a collection until one of the items satisfies a predicate.

  Return value includes the item which satisfied the predicate (unlike the very
  close cousin, take-while)."
  [pred coll]
  (when (not-empty coll)
    (let [el (first coll)]
      (lazy-seq (cons el (if (or (pred el) (empty? (rest coll)))
                           nil
                           (take-until pred (rest coll))))))))

(defn file->lines
  "Reads the entirety of a file and returns a sequence of its lines.

  Simplicity at the expense of a line-by-line reader's efficiency."
  [filename]
  (-> filename slurp (str/split #"\n")))

(defn keys-in
  "Returns a sequence of all key paths in a given map using DFS walk."
  [m]
  (letfn [(children [node]
            (let [v (get-in m node)]
              (if (map? v)
                (map (fn [x] (conj node x)) (keys v))
                [])))
          (branch? [node] (-> (children node) seq boolean))]
    (->> (keys m)
         (map vector)
         (mapcat #(tree-seq branch? children %)))))
