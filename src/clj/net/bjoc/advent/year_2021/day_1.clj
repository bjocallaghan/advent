(ns net.bjoc.advent.year-2021.day-1
  (:use [net.bjoc.advent.util.misc :only [zip]])
  (:require [clojure.string :as str]))

(defn file->depths [filename]
  (map #(Integer/parseInt %)
       (-> (slurp filename)
           (str/split #"\n"))))

(defn file->num-depths-increases [filename]
  (let [depths (file->depths filename)]
    (->> (zip depths (drop 1 depths))
         (filter #(apply < %))
         count)))
  
(defn part-1 []
  (println
   "Day 1 - Part 1 - Number of depth drops:"
   (file->num-depths-increases "data/year_2021/day_1.input")))

(defn file->num-window-increases [filename]
  (let [depths (file->depths filename)
        windows (->> (zip depths (drop 1 depths) (drop 2 depths))
                     (map #(reduce + %)))]
    (->> (zip windows (drop 1 windows))
         (filter #(apply < %))
         count)))

(defn part-2 []
  (println
   "Day 1 - Part 2 - Number of window increases:"
   (file->num-window-increases "data/year_2021/day_1.input")))
