(ns net.bjoc.advent.year-2016.day-3
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines zip]]))

(defn parse-nums [s]
  (map parse-long
       (-> s str/trim (str/split #"\s+"))))

(defn line->sides [line]
  (->> line
       parse-nums))

(defn valid? [sides]
  (let [[a b c] (sort sides)]
    (> (+ a b) c)))

(defn row-method [filename]
  (->> filename
       file->lines
       (map line->sides)
       (filter valid?)
       count))

;;;

(defn column-method [filename]
  (->> filename
       file->lines
       (map parse-nums)
       (partition 3)
       (map #(apply zip %))
       (apply concat)
       (filter valid?)
       count))

;;;

(advent/defrunner 1 row-method "Valid triangles from reading rows")
(advent/defrunner 2 column-method "Valid triangles from reading columns")
