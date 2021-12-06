(ns net.bjoc.advent.year-2021.day-6
  (:use [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [clojure.string :as str]))

(defn file->state [filename]
  (->> (str/split (slurp filename) #",")
       (map parse-int)))

(defn evolve [n] (if (zero? n) 6 (dec n)))

(defn step
  ([state] (step state 1))
  ([state num-steps]
   (if (zero? num-steps)
     state
     (recur (let [num-new (->> state (filter zero?) count)]
              (concat (map evolve state) (repeat num-new 8)))
            (dec num-steps)))))

(defn file->population-at-day [filename day]
  (-> filename
      file->state
      (step day)
      count))

(defn part-1 []
  (println
   "Day 6 - Part 1 - Population after 80 days:"
   (file->population-at-day "data/year_2021/day_6.input" 80)))

;; the setup and explanation for part 1 practically beg you to implement as an
;; ever growing array, leading to disaster at a larger number of iterations. but
;; by using a hashmap / associative array, things are managable

(defn file->state* [filename]
  (->> (str/split (slurp filename) #",")
       (map parse-int)
       frequencies))

(defn sum
  "Add arguments. Tolerates (disregards) nil."
  [& args]
  (reduce + (remove nil? args)))

(defn step*
  ([state] (step state 1))
  ([state num-steps]
   (if (zero? num-steps)
     state
     (recur (reduce-kv (fn [m k v]
                         (if (zero? k)
                           (-> m (assoc 8 v) (update 6 sum v))
                           (-> m (update (dec k) sum v))))
                       {} state)
            (dec num-steps)))))


(defn file->population-at-day* [filename day]
  (reduce + (-> filename
                file->state*
                (step* day)
                vals)))

(defn part-2 []
  (println
   "Day 6 - Part 2 - Population after 256 days:"
   (file->population-at-day* "data/year_2021/day_6.input" 256)))
