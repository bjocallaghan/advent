(ns net.bjoc.advent.util.matrix
  (:require [clojure.string :as str]))

(defn rotate
  ([matrix]
   (let [max-dim (->> (keys matrix) (map first) (apply max))]
     (reduce (fn [m [[col row] val]]
               (assoc m [(- max-dim row) col] val))
             {} matrix)))
  ([matrix num-rotations]


   (let [num-rotations* (mod num-rotations 4)]
     (if (zero? num-rotations*)
       matrix
       (recur (rotate matrix) (dec num-rotations*))))))

(defn flip
  ([matrix]
   (let [max-dim (->> (keys matrix) (map first) (apply max))]
     (reduce (fn [m [[col row] val]]
               (assoc m [(- max-dim col) row] val))
             {} matrix)))
  ([matrix num-flips]
   (if (odd? num-flips)
     (flip matrix)
     matrix)))

(defn matrix-size [m]
  (->> m keys (map first) (apply max)))
