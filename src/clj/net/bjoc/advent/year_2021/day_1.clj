(ns net.bjoc.advent.year-2021.day-1
  (:use [net.bjoc.advent.util.misc :only [zip]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(defn file->depths [filename]
  (map #(Integer/parseInt %)
       (-> (slurp filename)
           (str/split #"\n"))))

(defn file->num-depths-increases [filename]
  (let [depths (file->depths filename)]
    (->> (zip depths (drop 1 depths))
         (filter #(apply < %))
         count)))
  
;;;

(defn file->num-window-increases [filename]
  (let [depths (file->depths filename)
        windows (->> (zip depths (drop 1 depths) (drop 2 depths))
                     (map #(reduce + %)))]
    (->> (zip windows (drop 1 windows))
         (filter #(apply < %))
         count)))

;;;

(advent/defrunner 1 file->num-depths-increases "Number of depth drops")
(advent/defrunner 2 file->num-window-increases "Number of window increases")
