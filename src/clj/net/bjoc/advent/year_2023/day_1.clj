(ns net.bjoc.advent.year-2023.day-1
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(def digit? #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9})
(def not-digit? (complement digit?))

(defn line->num [line]
  (let [a (->> line
               (drop-while not-digit?)
               first)
        b (->> line
               reverse
               (drop-while not-digit?)
               first)]
    (-> [a b]
        str/join
        parse-long)))

(defn file->sum [filename]
  (->> filename
       file->lines
       (map line->num)
       (reduce +)
       ))

;;;

(defn get-digit [s] (re-find #"[1-9]|one|two|three|four|five|six|seven|eight|nine" s))
(defn get-digit* [s] (re-find #"[1-9]|eno|owt|eerht|ruof|evif|xis|neves|thgie|enin" s))

(defn parse-digit [s]
  (if-let [x (parse-long s)]
    x
    ({"one" 1
      "two" 2
      "three" 3
      "four" 4
      "five" 5
      "six" 6
      "seven" 7
      "eight" 8
      "nine" 9
      "eno" 1
      "owt" 2
      "eerht" 3
      "ruof" 4
      "evif" 5
      "xis" 6
      "neves" 7
      "thgie" 8
      "enin" 9}
     s)))

(defn line->num-2 [line]
  (let [a (->> line
               get-digit
               parse-digit)
        b (->> line
               reverse
               str/join
               get-digit*
               parse-digit)]
    (-> [a b]
        str/join
        parse-long)))

(defn file->sum-2 [filename]
  (->> filename
       file->lines
       (map line->num-2)
       (reduce +)
       ))

;;;

(advent/defrunner 1 file->sum "calibration sum")
(advent/defrunner 2 file->sum-2 "new calibration sum")
