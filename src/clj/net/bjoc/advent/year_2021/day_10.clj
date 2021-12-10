(ns net.bjoc.advent.year-2021.day-10
  (:use [net.bjoc.advent.util.misc :only [zip]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(def openers #{\{ \[ \( \<})

(def closer {\{ \}, \[ \], \( \), \< \>})

(defn check [line]
  (loop [tokens (seq line)
         stack (list)]
    (if (empty? tokens)
      (if (empty? stack)
        {:status :good}
        {:status :incomplete :stack stack})
      (let [c (first tokens)
            expecting (-> stack first closer)]
        (if (not (or (openers c)
                     (= c expecting)))
          {:status :corrupt :expecting expecting :actual c}
          (recur (rest tokens)
                 (if (openers c)
                   (conj stack c)
                   (rest stack))))))))

(defn corrupt? [line] (= :corrupt (-> line check :status)))

(def syntax-score {\) 3, \] 57, \} 1197, \> 25137})

(defn file->lines [filename]
  (-> filename
      slurp
      (str/split #"\n")))

(defn file->total-syntax-score [filename]
  (->> filename
       file->lines
       (filter corrupt?)
       (map check)
       (map :actual)
       (map syntax-score)
       (reduce +)
       ))

;;;

(defn incomplete? [line] (= :incomplete (-> line check :status)))

(defn auto-score [line]
  (let [weight {\( 1, \[ 2, \{ 3, \< 4}]
    (loop [stack (-> line check :stack)
           acc 0]
      (if (empty? stack)
        acc
        (recur (rest stack)
               (+ (-> stack first weight) (* acc 5)))))))

(defn file->middle-auto-score [filename]
  (let [incompletes (->> filename
                         file->lines
                         (filter incomplete?))]
    (->> incompletes
         (map auto-score)
         sort
         (drop (quot (count incompletes) 2))
         first)))

;;;

(advent/defrunner 1 file->total-syntax-score "Total syntax error score")
(advent/defrunner 2 file->middle-auto-score "Middle autocomplete score")
