(ns net.bjoc.advent.year-2024.day-4
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mat]
            [net.bjoc.advent.util.coords :refer [add-coords scale]]
            [net.bjoc.advent.util.misc :refer [zip]]))

(def directions
  [[ 1  0]
   [ 1  1]
   [ 0  1]
   [-1  1]
   [-1  0]
   [-1 -1]
   [ 0 -1]
   [ 1 -1]])

(defn get-word [matrix direction coord]
  (->> (range 4)
       (map #(add-coords coord (scale direction %)))
       (map #(get matrix %))
       str/join))

(def xmas? #(= "XMAS" %))

(defn xmas-count [matrix]
  (->> (for [coord (keys matrix)
             dir directions]
         (get-word matrix dir coord))
       (filter xmas?)
       count))

(def file->xmas-count (comp xmas-count mat/from-file))

;;;

(def a? #(= \A %))
(def mmss? #{"MMSS" "MSSM" "SSMM" "SMMS"})

(defn get-corners [matrix coord]
  (->> [[1 1] [1 -1] [-1 -1] [-1 1]]
       (map #(add-coords coord %))
       (map matrix)
       (remove nil?)
       str/join))

(defn xmas-count* [matrix]
  (->> matrix
       (filter (fn [[_ v]] (a? v)))
       keys
       (map #(get-corners matrix %))
       (filter mmss?)
       count))

(def file->xmas-count* (comp xmas-count* mat/from-file))

;;;

(advent/defrunner 1 file->xmas-count "XMAS count")
(advent/defrunner 2 file->xmas-count* "XMAS count (revised)")
