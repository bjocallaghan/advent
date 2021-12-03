(ns net.bjoc.advent.util.matrix
  (:require [clojure.string :as str]))

(defn rotate
  "Rotate a matrix clockwise."
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
  "Transform a matrix so that all values are flipped about its vertical axis."
  ([matrix]
   (let [max-dim (->> (keys matrix) (map first) (apply max))]
     (reduce (fn [m [[col row] val]]
               (assoc m [(- max-dim col) row] val))
             {} matrix)))
  ([matrix num-flips]
   (if (odd? num-flips)
     (flip matrix)
     matrix)))

(defn size
  "Infer the size of a given matrix by examination of its indexes / keys."
  [matrix]
  (->> matrix keys (map first) (apply max) inc))

(def ^:private single-char-string? #(and (string? %) (count %)))

(defn guess-type
  "Infer the type of matrix based on examination of element values."
  [matrix]
  (let [m-vals (vals matrix)]
    (cond
      (every? char? m-vals) :characters
      (every? single-char-string? m-vals) :characters
      (every? number? m-vals) :numbers
      :else (throw
             (ex-info "Cannot determine matrix type based on element values."
                      {:values m-vals})))))

(defn dump
  "Write a representation of the matrix to standard out."
  [matrix]
  (let [sz (size matrix)]
    (doseq [row (range sz)]
      (println (str/join (map #(matrix [% row]) (range sz)))))))
