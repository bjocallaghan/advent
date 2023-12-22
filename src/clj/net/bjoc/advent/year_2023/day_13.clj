(ns net.bjoc.advent.year-2023.day-13
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]))

(defn x-reflect-at-n? [matrix n]
  (reduce (fn [acc [[x y] c]]
            (and acc (let [target [(+ n 1 (- n x)) y]
                           target-value (get matrix target :out)]
                       (#{:out c} target-value))))
          true
          (filter (fn [[[x _] c]] (>= n x)) matrix)))

(defn y-reflect-at-n? [matrix n]
  (reduce (fn [acc [[x y] c]]
            (and acc (let [target [x (+ n 1 (- n y))]
                           target-value (get matrix target :out)]
                       (#{:out c} target-value))))
          true
          (filter (fn [[[_ y] c]] (>= n y)) matrix)))

(defn score* [f1 f2 m]
  (let [[x-size y-size] (mtx/size m)]
    (+ (->> (range (dec x-size))
            (filter (partial f1 m))
            (map inc)
            (reduce +))
       (->> (range (dec y-size))
            (filter (partial f2 m))
            (map inc)
            (map #(* 100 %))
            (reduce +)))))

(def score (partial score* x-reflect-at-n? y-reflect-at-n?))

(defn file->result* [score-fn filename]
  (->> filename
       slurp
       (#(str/split % #"\n\n"))
       (map mtx/from-string)
       (map score-fn)
       (reduce +)))

(def file->result (partial file->result* score))

;;;

(defn x-almost-reflect-at-n? [matrix n]
  (= 1 (reduce (fn [acc [[x y] c]]
                 (let [target [(+ n 1 (- n x)) y]
                       target-value (get matrix target :out)]
                   (+ acc
                      (if (#{:out c} target-value) 0 1))))
               0
               (filter (fn [[[x _] c]] (>= n x)) matrix))))

(defn y-almost-reflect-at-n? [matrix n]
  (= 1 (reduce (fn [acc [[x y] c]]
                 (let [target [x (+ n 1 (- n y))]
                       target-value (get matrix target :out)]
                   (+ acc
                      (if (#{:out c} target-value) 0 1))))
               0
               (filter (fn [[[_ y] c]] (>= n y)) matrix))))

(def score-2 (partial score* x-almost-reflect-at-n? y-almost-reflect-at-n?))

(def file->result-2 (partial file->result* score-2))

;;;

(advent/defrunner 1 file->result "Result")
(advent/defrunner 2 file->result-2 "Result")
