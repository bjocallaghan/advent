(ns net.bjoc.advent.year-2022.day-4
  (:require [clojure.string :as str]
            [clojure.set :refer [difference]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn line->sets [line]
  (let [[a b c d] (map #(Integer/parseInt %)
                       (rest (re-find #"^(\d+)-(\d+),(\d+)-(\d+)$" line)))]
    [(set (range a (inc b))) (set (range c (inc d)))]))

(defn redundant? [[a b]]
  (or (empty? (difference a b))
      (empty? (difference b a))))

(defn file->matching-line-count [pred filename]
  (->> filename
       file->lines
       (map line->sets)
       (filter pred)
       count))

(def file->redundant-count (partial file->matching-line-count redundant?))

;;;

(defn overlap? [[a b]] (some a b))

(def file->overlap-count (partial file->matching-line-count overlap?))

;;;

(advent/defrunner 1 file->redundant-count "Number of pairs with complete redundancy")
(advent/defrunner 2 file->overlap-count "Number of pairs which overlap")
