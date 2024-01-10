(ns net.bjoc.advent.year-2023.day-9
  (:require [clojure.string :as str]
            [clj-http.client :as http]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip file->lines]]))

(defn parse-line [line]
  (->> (str/split line #" ")
       (map parse-long)))

(def base-case? (partial every? zero?))
(def pair-diff #(- (second %) (first %)))

(defn calc-next [coll]
  (->> coll
       (iterate #(map pair-diff (zip % (rest %))))
       (take-while (complement base-case?))
       (map last)
       (reduce +)))

(defn file->nexts-sum [filename]
  (->> filename
       file->lines
       (map parse-line)
       (map calc-next)
       (reduce +)))

;;;

(defn alternate-neg [coll]
  (map * coll (cycle [1 -1])))

(defn calc-prev [coll]
  (->> coll
       (iterate #(map pair-diff (zip % (rest %))))
       (take-while (complement base-case?))
       (map first)
       (alternate-neg)
       (reduce +)))

(defn file->prevs-sum [filename]
  (->> filename
       file->lines
       (map parse-line)
       (map calc-prev)
       (reduce +)))

;;;

(advent/defrunner 1 file->nexts-sum "Sum of next values")
(advent/defrunner 2 file->prevs-sum "Sum of previous values")
