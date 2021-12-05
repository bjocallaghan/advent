(ns net.bjoc.advent.year-2020.day-1
  (:use [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [clojure.string :as str]))

(defn file->numbers [filename]
  (map parse-int (-> filename slurp (str/split #"\n"))))

(defn target-pair [target numbers]
  (->> (for [a numbers b numbers :when (< a b)] [a b])
       (filter #(= target (reduce + %)))
       first))

(def target-pair-2020 (partial target-pair 2020))

(defn file->answer [filename]
  (->> (file->numbers filename)
       target-pair-2020
       (reduce *)))

(defn part-1 []
  (println
   "Day 1 - Part 1 - Answer:"
   (file->answer "data/year_2020/day_1.input")))

(defn target-triplet [target numbers]
  (->> (for [a numbers b numbers c numbers :when (< a b c)] [a b c])
       (filter #(= target (reduce + %)))
       first))

(def target-triplet-2020 (partial target-triplet 2020))

(defn file->answer* [filename]
  (->> (file->numbers filename)
       target-triplet-2020
       (reduce *)))

(defn part-2 []
  (println
   "Day 1 - Part 2 - Answer:"
   (file->answer* "data/year_2020/day_1.input")))
