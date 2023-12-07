(ns net.bjoc.advent.year-2023.day-5
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn line->fn [line]
  (let [[dest-start src-start len] (->> (str/split line #"\s+")
                                        (map parse-long))]
    ;; original naive implementation: too memory-intensive
    ;; (into {} (map vector
    ;;               (range src-start (+ src-start len))
    ;;               (range dest-start (+ dest-start len))))

    ;; refactored to not use in-memory maps
    (fn [x]
      (when (<= src-start x (+ src-start len))
        (+ dest-start (- x src-start))))
    ))

(defn spec->fn [lines]
  (let [fns (->> lines
                 (drop 1)
                 (map line->fn))]
    (fn [x]
      (let [result (or (->> fns
                            (map #(% x))
                            (remove nil?)
                            first)
                       x)]
        ;; (println x "->" result)
        result))))

(defn file->xform [filename]
  (->> (str/split (slurp filename) #"\n\n")
       (drop 1)
       (map #(str/split % #"\n"))
       (map spec->fn)
       reverse
       (apply comp)))

(defn file->best-location [filename]
  (let [xform (file->xform filename)
        seeds (->> (str/split (slurp filename) #"\n")
                   first
                   (#(str/split % #"\s+"))
                   (map parse-long)
                   (remove nil?))]
    (apply min (map xform seeds))))
  
;;;

(defn min*
  "Return the minimum number within seq `xs`.

  Custom implementation for interface and performance. Avoid reifying the sequence."
  [xs]
  (reduce (fn [best x]
            (if (< x best) x best))
          (first xs) (rest xs)))

(defn file->best-location-2 [filename]
  (let [xform (file->xform filename)
        seed-specs (->> (str/split (slurp filename) #"\n")
                        first
                        (#(str/split % #"\s+"))
                        (map parse-long)
                        (remove nil?)
                        (partition 2)
                        (map vec))
        best-seed (fn [[seed-start len]]
                    (min* (map xform (range seed-start (+ seed-start len)))))]
    (min* (map best-seed seed-specs))))

;;;

(advent/defrunner 1 file->best-location "Best location")
(advent/defrunner 2 file->best-location-2 "Best location")
