(ns net.bjoc.advent.year-2016.day-9
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip]]))

(def opening? #(= \( %))
(def closing? #(= \) %))

(defn split-after [pred coll]
  (let [head (reduce (fn [acc el]
                       (if (pred el)
                         (reduced (conj acc el))
                         (conj acc el)))
                     [] coll)
        n (count head)]
    [head (->> coll (drop n))]))

(def as-str #(apply str %))

(defn parse-expansion [s]
  (let [[_ a b] (re-find #"\((\d+)x(\d+)\)" s)]
    {:length (parse-long a)
     :multiplier (parse-long b)}))

(defn expand [s]
  (loop [rem s
         acc '()]
    (if (empty? rem)
      acc
      (if (opening? (first rem))
        (let [[a b] (split-after closing? rem)
              {:keys [length multiplier]} (parse-expansion (as-str a))
              expansion (->> rem
                             (drop (count a))
                             (take length)
                             (repeat multiplier)
                             (apply concat))]
          (recur (drop length b)
                 (concat acc expansion)))
        (recur (rest rem)
               (concat acc [(first rem)]))))))

(defn count-expansion [filename]
  (->> filename
       slurp
       expand
       count))

;;;

(defn count-expansion-2 [s]
  (loop [rem s
         acc 0]
    (if (empty? rem)
      acc
      (if (opening? (first rem))
        (let [[a b] (split-after closing? rem)
              {:keys [length multiplier]} (parse-expansion (as-str a))
              expansion-length (->> b
                                    (take length)
                                    count-expansion-2
                                    (* multiplier))]
          (recur (drop length b)
                 (+ acc expansion-length)))
        (recur (rest rem)
               (inc acc))))))

;;;

(advent/defrunner 1 count-expansion "Expanded size")
(advent/defrunner 2 (comp count-expansion-2 slurp) "Expanded size")
