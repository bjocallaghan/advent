(ns net.bjoc.advent.year-2022.day-3
  (:require [clojure.string :as str]
            [clojure.set :refer [intersection]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [single file->lines]]))

(defn letter-score [letter]
  (inc (.indexOf "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" (str letter))))

(defn find-common [line]
  (let [compartment-size (/ (.length line) 2)
        part-1 (subs line 0 compartment-size)
        part-2 (subs line compartment-size)]
    (single (intersection (set part-1) (set part-2)))))

(defn file->priority-sum [filename]
  (->> filename
       file->lines
       (map find-common)
       (map letter-score)
       (reduce +)))

;;;

(defn find-badge [packs]
  (->> packs (map set) (apply intersection) single))

(defn file->badge-sum [filename]
  (->> filename
       file->lines
       (partition 3)
       (map find-badge)
       (map letter-score)
       (reduce +)))

;;;

(advent/defrunner 1 file->priority-sum "Sum of priorities")
(advent/defrunner 2 file->badge-sum "Sum of badge scores")
