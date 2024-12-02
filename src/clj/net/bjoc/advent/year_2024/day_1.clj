(ns net.bjoc.advent.year-2024.day-1
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines zip]]))

(defn file->lists [filename]
  (let [pairs (->> filename
                   file->lines
                   (map #(str/split % #"\s+"))
                   (map #(map parse-long %))
                   )]
    [(map first pairs)
     (map second pairs)]))

(defn total-distance [[lst-1 lst-2]]
  (->> (zip (sort lst-1) (sort lst-2))
       (map #(apply - %))
       (map abs)
       (reduce +)))

(def file->total-distance (comp total-distance file->lists))

;;;

(defn similarity-score [[lst-1 lst-2]]
  (->> lst-1
       (map (fn [n] (* n
                       (->> lst-2 (filter #(= n %)) count))))
       (reduce +)))

(def file->similarity-score (comp similarity-score file->lists))

;;;

(advent/defrunner 1 file->total-distance "Total distance")
(advent/defrunner 2 file->similarity-score "Similarity score")
