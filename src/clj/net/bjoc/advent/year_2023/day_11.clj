(ns net.bjoc.advent.year-2023.day-11
  (:require [clojure.math.combinatorics :refer [combinations]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.util.coords :refer [manhattan-distance]]))

(def into-pairs #(combinations % 2))

(defn vacant-rows [matrix]
  (let [num-rows (second (mtx/size matrix))
        occupied? (->> matrix
                       (remove (fn [[_ c]] (= c \.)))
                       (map first)
                       (map second)
                       set)]
    (->> (range num-rows)
         (remove occupied?)
         set)))

(defn vacant-cols [matrix]
  (let [num-cols (first (mtx/size matrix))
        occupied? (->> matrix
                       (remove (fn [[_ c]] (= c \.)))
                       (map first)
                       (map first)
                       set)]
    (->> (range num-cols)
         (remove occupied?)
         set)))

(defn pair-distances-sum [expansion-factor matrix]
  (let [distance-fn (let [rows (vacant-rows matrix)
                          cols (vacant-cols matrix)]
                      (fn [[[x1 y1] [x2 y2]]]
                        (+ (manhattan-distance [x1 y1] [x2 y2])
                           (->> cols (filter #(< (min x1 x2) % (max x1 x2))) count (* (dec expansion-factor)))
                           (->> rows (filter #(< (min y1 y2) % (max y1 y2))) count (* (dec expansion-factor))))))]
    (->> matrix
         (map first)
         into-pairs
         (map distance-fn)
         (reduce +))))

(defn file->pair-distances-sum* [expansion-factor filename]
  (->> (mtx/from-file filename)
       (remove (fn [[_ c]] (= c \.)))
       (pair-distances-sum expansion-factor)))

(def file->pair-distances-sum (partial file->pair-distances-sum* 2))

;;;

(def file->pair-distances-sum-2 (partial file->pair-distances-sum* 1000000))

;;;

(advent/defrunner 1 file->pair-distances-sum "Sum of pair distances")
(advent/defrunner 2 file->pair-distances-sum-2 "Sum of pair distances (older)")
