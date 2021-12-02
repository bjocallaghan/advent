(ns net.bjoc.advent.year-2019.day-4
  (:use [net.bjoc.advent.util.numeric :only [digit-seq]]))

(def lower-limit 353096)
(def upper-limit 843212)

(defn six-digits? [n]
  (and (>= n 100000) (<= n 999999)))

(defn increasing-digits? [n]
  (let [digits (digit-seq n)]
    (= digits (sort digits))))

(defn has-equal-adjacents? [n]
  (let [digits (digit-seq n)]
    (some #(apply = %) (map vector digits (drop 1 digits)))))

(def candidate? (every-pred six-digits? increasing-digits? has-equal-adjacents?))

(defn part-1 []
  (println 
   "Day 4 - Part 1 - Number of numbers matching criteria:"
   (->> (range lower-limit (inc upper-limit))
        (filter candidate?)
        count)))

(defn has-unique-equal-adjacents? [n]
  (let [digits (-> (digit-seq n) (conj -1) (concat [-1]))]
    (some (fn [[a b c d]] (and (= b c) (not= a b) (not= c d)))
          (map vector digits (drop 1 digits) (drop 2 digits) (drop 3 digits)))))

(def candidate?* (every-pred six-digits?
                             increasing-digits?
                             has-unique-equal-adjacents?))

(defn part-2 []
  (println
   "Day 4 - Part 2 - Number of numbers matching criteria:"
   (->> (range lower-limit (inc upper-limit))
        (filter candidate?*)
        count)))
