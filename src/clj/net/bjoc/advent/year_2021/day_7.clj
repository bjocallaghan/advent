(ns net.bjoc.advent.year-2021.day-7
  (:use [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [clojure.string :as str]))

(defn file->positions [filename]
  (map parse-int (-> filename
                     slurp
                     (str/split #","))))

(defn fleet-expenditure [fleet-positions target-position]
  (reduce #(+ %1 (Math/abs (- %2 target-position))) 0 fleet-positions))

(defn best-expenditure [positions]
  (let [lower (apply min positions)
        upper (apply max positions)
        expenditure-fn (partial fleet-expenditure positions)]
    (apply min (map expenditure-fn (range lower (inc upper))))))
    
(defn file->best-expenditure [filename]
  (->> filename
       file->positions
       best-expenditure))

(defn part-1 []
  (println
   "Day 7 - Part 1 - Most efficient total expenditure:"
   (file->best-expenditure "data/year_2021/day_7.input")))

(defn fleet-expenditure* [fleet-positions target-position]
  (let [move-cost (fn [a b]
                    (let [diff (Math/abs (- a b))]
                      (reduce + (range (inc diff)))))]
    (reduce #(+ %1 (move-cost target-position %2)) 0 fleet-positions)))

(defn best-expenditure* [positions]
  (let [lower (apply min positions)
        upper (apply max positions)
        expenditure-fn (partial fleet-expenditure* positions)]
    (apply min (map expenditure-fn (range lower (inc upper))))))
    
(defn file->best-expenditure* [filename]
  (->> filename
       file->positions
       best-expenditure*))

(defn part-2 []
  (println
   "Day 7 - Part 2 - Most efficient total expenditure (modified algorithm):"
   (file->best-expenditure* "data/year_2021/day_7.input")))
