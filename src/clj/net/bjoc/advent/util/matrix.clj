(ns net.bjoc.advent.util.matrix
  (:use [clojure.java.io :as io]
        [net.bjoc.advent.util.numeric :only [int-str?]])
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
  "Return a sequence of the dimensional sizes of a matrix."
  [matrix]
  (into [] (for [i (range (-> matrix keys first count))]
             (->> matrix keys (map #(nth % i)) (apply max) inc))))

(defn rows
  "Return the row values of a two dimensional matrix."
  [matrix]
  (let [[num-cols num-rows] (size matrix)]
    (for [row (range num-rows)]
      (->> (map vector (range num-cols) (repeat row))
           (map matrix)))))

(defn cols
  "Return the column values of a two dimensional matrix."
  [matrix]
  (let [[num-cols num-rows] (size matrix)]
    (for [col (range num-cols)]
      (->> (map vector (repeat col) (range num-rows))
           (map matrix)))))

(def ^:private single-char-string? #(and (string? %) (= 1 (count %))))

(defn guess-type
  "Infer the type of matrix based on examination of element values."
  [matrix]
  (let [m-vals (vals matrix)]
    (cond
      (every? char? m-vals) :characters
      (every? single-char-string? m-vals) :characters
      (every? number? m-vals) :numbers
      (every? string? m-vals) :strings
      (every? keyword? m-vals) :keywords
      :else (throw
             (ex-info "Cannot determine matrix type based on element values."
                      {:values m-vals})))))

(defn dump
  "Write a string representation of the matrix."
  [matrix
   & {:keys [stream-writer value-repr-fn]
      :or {stream-writer *out*
           value-repr-fn identity}
      :as options}]
  (let [matrix (reduce-kv (fn [m k v] (assoc m k (value-repr-fn v))) {} matrix)
        separator (if (= :characters (guess-type matrix)) "" " ")
        matrix (reduce (fn [m [k v]] (update m k str v)) {} matrix)
        [num-cols num-rows] (size matrix)
        column-widths (for [i (range num-cols)]
                        (->> matrix
                             (filter (fn [[[col row] v]] (= col i)))
                             (map (fn [[_ v]] (str v)))
                             (map count)
                             (apply max)))
        s (str/join "\n"
                    (for [row (range num-rows)]
                      (str/join separator
                                (map #(format (str "%" (str (nth column-widths %)) "s")
                                              (matrix [% row]))
                                     (range num-cols)))))]
    (.write stream-writer (format "%s\n" s))
    s))

(defn- contains-whitespace? [s] (re-find #"\s" s))

(defn- try-int-promote [matrix]
  (if (every? int-str? (->> matrix vals (map str)))
    (reduce (fn [m [k v]] (assoc m k (Integer/parseInt (str v)))) {} matrix)
    matrix))

(defn from-string
  "Return a matrix based on an input string."
  [s
   & {:keys [type]
      :as options}]
  (try-int-promote
   (into {}
         (let [lines (str/split s #"\n")]
           (if (or (= type :characters)
                   (not-any? contains-whitespace? lines))
             (for [[row line] (map-indexed vector lines)
                   [col c] (map-indexed vector line)]
               [[col row] c])
             (for [[row line] (map-indexed vector lines)
                   [col s] (map-indexed vector (str/split (str/trim line) #"\s+"))]
               [[col row] s]))))))

(defn from-file
  "Return a matrix based on an input file."
  [filename & options]
  (apply (partial from-string (slurp filename))
         (mapcat identity options)))

(defn transform?
  "Return truthy if one matrix is equivalent (a flipped or rotated version) of
  another."
  [matrix-1 matrix-2]
  (some identity (for [num-rotations (range 4)
                       num-flips (range 2)]
                   (= matrix-1 (-> matrix-2
                                   (rotate num-rotations)
                                   (flip num-flips))))))

(defn update-all
  "Transform a matrix by applying a function to each element.

  The supplied function takes a single argument, the element's value."
  [f matrix]
  (reduce (fn [m [k v]] (assoc m k (f v))) {} matrix))

(defn update-all-indexed
  "Transform a matrix by applying a function to each indexed element.

  The supplied function takes two arguments, the element's index and its value."
  [f matrix]
  (reduce (fn [m [k v]] (assoc m k (f k v))) {} matrix))

(defn crop
  "Eliminate cells from a matrix that don't fit in a cropping window."
  [matrix lower-left-xy upper-right-xy]
  (let [[x-min y-min] lower-left-xy
        [x-max y-max] upper-right-xy
        out-of-bounds? (some-fn (fn [[x _]] (> x x-max))
                                (fn [[x _]] (< x x-min))
                                (fn [[_ y]] (> y y-max))
                                (fn [[_ y]] (< y y-min)))]
    (into {} (remove (fn [[xy _]] (out-of-bounds? xy)) matrix))))
