(ns net.bjoc.advent.year-2021.day-5
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.numeric :as num]))

(defn file->segments [filename]
  (let [extract-nums (fn [line]
                       (->> (re-find #"^(\d+),(\d+) -> (\d+),(\d+)$" line)
                            rest
                            (map num/parse-int)))
        segment-from-nums (fn [[a b c d]] [[a b] [c d]])]
    (->> (str/split (slurp filename) #"\n")
         (map extract-nums)
         (map segment-from-nums))))

(defn diagnonal? [[[start-x start-y] [end-x end-y]]]
  (not (or (= start-x end-x) (= start-y end-y))))

(defn segment-points [[[start-x start-y] [end-x end-y]]]
  (cond
    (= start-x end-x) (let [a (min start-y end-y)
                            b (max start-y end-y)]
                        (map #(vector start-x %) (range a (inc b))))
    (= start-y end-y) (let [a (min start-x end-x)
                            b (max start-x end-x)]
                        (map #(vector % start-y) (range a (inc b))))
    (and (< start-x end-x)
         (< start-y end-y)) (map vector
                                 (range start-x (inc end-x))
                                 (range start-y (inc end-y)))
    (and (< start-x end-x)
         (> start-y end-y)) (map vector
                                 (range start-x (inc end-x))
                                 (reverse (range end-y (inc start-y))))
    :else (segment-points [[end-x end-y] [start-x start-y]])))

(defn file->num-dangerous-points [filename]
  (->> (file->segments filename)
       (remove diagnonal?)
       (mapcat segment-points)
       frequencies
       (filter (fn [[k v]] (> v 1)))
       count))

(defn file->num-dangerous-points* [filename]
  (->> (file->segments filename)
       (mapcat segment-points)
       frequencies
       (filter (fn [[k v]] (> v 1)))
       count))

;;;

(advent/defrunner 1 file->num-dangerous-points "Dangerous points")
(advent/defrunner 2 file->num-dangerous-points* "Dangerous points (modified)")
