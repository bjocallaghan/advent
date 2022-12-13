(ns net.bjoc.advent.year-2022.day-13
  (:require [clojure.edn :as edn]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.numeric :refer [int-seq]]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn file->packets [filename]
  (->> filename
       file->lines
       (remove empty?)
       (map edn/read-string)))

(defn compare-packets [a b]
  (cond
    (and (vector? a) (int? b)) (compare-packets a [b])
    (and (int? a) (vector? b)) (compare-packets [a] b)
    (and (int? a) (int? b)) (compare a b)
    (and (empty? a) (empty? b)) 0
    (empty? a) -1
    (empty? b) 1
    :else (let [result (compare-packets (first a) (first b))]
            (if (= result 0)
              (compare-packets (subvec a 1) (subvec b 1))
              result))))

(defn file->good-index-sum [filename]
  (->> filename
       file->packets
       (partition 2)
       (map #(apply compare-packets %))
       (map vector (int-seq 1))
       (filter #(= -1 (second %)))
       (map first)
       (reduce +)))

;;;

(def divider-packets (set [[[2]] [[6]]]))

(defn file->decoder-key [filename]
  (->> filename
       file->packets
       (concat divider-packets)
       (sort-by identity compare-packets)
       (map vector (int-seq 1))
       (filter #(divider-packets (second %)))
       (map first)
       (reduce *)))

;;;

(advent/defrunner 1 file->good-index-sum "Sum of 'good' indexes")
(advent/defrunner 2 file->decoder-key "Decoder key")
