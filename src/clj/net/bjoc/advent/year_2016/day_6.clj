(ns net.bjoc.advent.year-2016.day-6
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip]]))

(def split-lines #(str/split % #"\n"))

(defn choose-by-frequency [score-fn coll]
  (->> coll
       frequencies
       (sort-by (fn [[_ n]] (score-fn n)))
       first
       first))

(def most-frequent (partial choose-by-frequency -))
(def least-frequent (partial choose-by-frequency +))

(defn decode* [f filename]
  (->> filename
       slurp
       split-lines
       (apply zip)
       (map f)
       str/join
       ))

(def decode (partial decode* most-frequent))

;;;

(def decode-2 (partial decode* least-frequent))

;;;

(advent/defrunner 1 decode "Message")
(advent/defrunner 2 decode-2 "Message")
