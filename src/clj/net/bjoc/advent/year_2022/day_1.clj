(ns net.bjoc.advent.year-2022.day-1
  (:require [clojure.java.io :as io]
            [net.bjoc.advent.core :as advent]))

(defn lines->chunks [lines]
  (lazy-seq
   (cons (take-while not-empty lines)
         (when-let [next (->> lines
                              (drop-while not-empty)
                              (drop 1)
                              not-empty)]
           (lines->chunks next)))))

(defn chunk->score [lines]
  (->> lines
       (map #(Integer/parseInt %))
       (reduce +)))

(defn- file->best-scores* [n filename]
  (with-open [rdr (io/reader filename)]
    (->> (line-seq rdr)
         (lines->chunks)
         (map chunk->score)
         sort
         reverse
         (take n)
         (reduce +))))

(def file->best-score (partial file->best-scores* 1))
(def file->best-3-scores (partial file->best-scores* 3))

(advent/defrunner 1 file->best-score "Best calorie score")
(advent/defrunner 2 file->best-3-scores "Sum of best 3 calorie scores")
