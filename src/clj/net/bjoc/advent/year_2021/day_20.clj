(ns net.bjoc.advent.year-2021.day-20
  (:use [net.bjoc.advent.util.coords :only [add-coords]])
  (:use [net.bjoc.advent.util.binary :only [read-binary-string]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.core :as advent]))

(defn file->enhancer [filename]
  (->> filename
       slurp
       (#(str/split % #"\n\n"))
       first
       (#(str/split % #"\n"))
       (apply concat)
       vec))

(defn file->image [filename]
  (->> filename
       slurp
       (#(str/split % #"\n\n"))
       second
       mtx/from-string
       (filter #(= (val %) \#))
       (into {})))

(def lens [[-1 -1] [0 -1] [1 -1] [-1 0] [0 0] [1 0] [-1 1] [0 1] [1 1]])

(def light? #(= % \#))

(defn enhanced-pixel [image enhancer xy]
  (let [index (->> (map (partial add-coords xy) lens)
                   (map image)
                   (map #(if (light? %) \1 \0))
                   (apply str)
                   read-binary-string)]
    (enhancer index)))

;; definitely some kind of buffer is needed, but it probably doesn't need to be
;; as big as i'm making it.
(def buffer 10)

(defn evolve* [image enhancer]
  (let [enhancer* (partial enhanced-pixel image enhancer)
        xs (->> image keys (map first))
        ys (->> image keys (map second))
        x-min (apply min xs)
        x-max (apply max xs)
        y-min (apply min ys)
        y-max (apply max ys)]
    (->> (for [x (range (- x-min buffer) (+ (inc buffer) x-max))
               y (range (- y-min buffer) (+ (inc buffer) y-max))]
           [[x y] (enhancer* [x y])])
         (filter (fn [[k v]] (light? v)))
         (into {}))))

(defn evolve
  ([image enhancer] (evolve image enhancer 1))
  ([image enhancer num-steps]
   (if (zero? num-steps)
     image
     (recur (evolve* image enhancer)
            enhancer
            (dec num-steps)))))

(defn double-evolve* [image enhancer]
  (let [xs (->> image keys (map first))
        ys (->> image keys (map second))
        x-min (apply min xs)
        x-max (apply max xs)
        y-min (apply min ys)
        y-max (apply max ys)
        small-x-low-bound (- x-min buffer)
        small-x-upp-bound (+ x-max buffer)
        small-y-low-bound (- y-min buffer)
        small-y-upp-bound (+ y-max buffer)
        large-x-low-bound (- small-x-low-bound buffer)
        large-x-low-bound (+ small-x-low-bound buffer 1)
        large-y-low-bound (- small-y-low-bound buffer)
        large-y-low-bound (+ small-y-low-bound buffer 1)]
    (->> (evolve image enhancer 2)
         (filter (fn [[[x y] _]]
                   (and (< small-x-low-bound x small-x-upp-bound)
                        (< small-y-low-bound y small-y-upp-bound))))
         (into {}))))

(defn double-evolve
  ([image enhancer] (double-evolve image enhancer 1))
  ([image enhancer num-times]
   (if (pos? num-times)
     (recur (double-evolve* image enhancer) enhancer (dec num-times))
     image)))

(defn file->evolve-2-count [filename]
  (let [image (file->image filename)
        enhancer (file->enhancer filename)]
    (count (double-evolve image enhancer))))

;;;

(defn file->evolve-50-count [filename]
  (let [image (file->image filename)
        enhancer (file->enhancer filename)]
    (count (double-evolve image enhancer 25))))

;;;

(advent/defrunner 1 file->evolve-2-count "Light pixels after 2 steps")
(advent/defrunner 2 file->evolve-50-count "Light pixels after 50 steps")
