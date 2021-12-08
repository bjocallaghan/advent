(ns net.bjoc.advent.year-2021.day-8
  (:use [clojure.set :only [intersection]]
        [net.bjoc.advent.util.misc :only [find-first single]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(defn alphabetize [s] (str/join (sort s)))

(defn line->notes [line]
  (let [[part1 part2] (str/split line #" \| ")]
    {:signals (map alphabetize (str/split part1 #"\s+"))
     :outputs (map alphabetize (str/split part2 #"\s+"))}))

(defn file->all-notes [filename]
  (let [lines (-> filename slurp (str/split #"\n"))]
    (map line->notes lines)))

(def is-1478? #(#{2 3 4 7} (count %)))

(defn file->count-1478 [filename]
  (->> (file->all-notes filename)
       (mapcat :outputs)
       (filter is-1478?)
       count))

;;;

(def has-n #(= %1 (count %2)))
(def has-2? (partial has-n 2))
(def has-3? (partial has-n 3))
(def has-4? (partial has-n 4))
(def has-5? (partial has-n 5))
(def has-6? (partial has-n 6))
(def has-7? (partial has-n 7))

(defn has-segs+? [base-signal candidate-signal]
  (every? (set candidate-signal) base-signal))

(defn has-f-pred [lookup]
  (let [f-char (single (intersection (set (lookup 1)) (set (lookup 6))))]
    (fn [signal]
      ((set signal) f-char))))

(def deduce-order [1 7 4 8
                   3 6
                   2 5
                   9
                   0])

(defn deduce [signals]
  (->> (reduce (fn [lookup digit]
                 (assoc lookup digit
                        (case digit
                          ;; tier 1: can figure out right away
                          1 (find-first has-2? signals)
                          4 (find-first has-4? signals)
                          7 (find-first has-3? signals)
                          8 (find-first has-7? signals)

                          ;; tier 2: requires tier 1 knowledge
                          6 (->> signals
                                 (filter has-6?)
                                 (remove (partial has-segs+? (lookup 1)))
                                 single)

                          3 (->> signals
                                 (filter has-5?)
                                 (filter (partial has-segs+? (lookup 1)))
                                 single)

                          ;; tier 3: requires tier 2 knowledge
                          2 (->> signals
                                 (remove (-> lookup vals set))
                                 (filter has-5?)
                                 (remove (has-f-pred lookup))
                                 single)
                          5 (->> signals
                                 (remove (-> lookup vals set))
                                 (filter has-5?)
                                 (filter (has-f-pred lookup))
                                 single)

                          ;; tier 4: requires tier 3 knowledge
                          9 (->> signals
                                 (remove (-> lookup vals set))
                                 (filter has-6?)
                                 (filter (partial has-segs+? (lookup 5)))
                                 single)

                          ;; tier 5: process of elimination
                          0 (->> signals
                                 (remove (-> lookup vals set))
                                 single))))
               {} deduce-order)
       (reduce-kv (fn [m k v] (assoc m v k)) {})))

(defn notes->n [{:keys [signals outputs] :as notes}]
  (let [lookup (deduce signals)]
    (->> outputs
         (map lookup)
         (reduce (fn [acc n] (+ n (* 10 acc))) 0))))

(defn file->sum [filename]
  (->> filename
       file->all-notes
       (map notes->n)
       (reduce +)))

;;;

(advent/defrunner 1 file->count-1478 "Number of 1s, 4s, 7s, and 8s")
(advent/defrunner 2 file->sum "Sum of all outputs")
