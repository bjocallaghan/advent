(ns net.bjoc.advent.year-2019.day-6
  (:require [clojure.string :as str]))

(defn file->orbital-parents [filename]
  (->> (str/split (slurp filename) #"\n")
       (map #(str/split % #"\)"))
       (map #(vector (second %) (first  %)))
       (into {})))

(def center-of-mass? #(= % "COM"))

(defn count-orbits [orbital-parents orbiter]
  (loop [orbiter orbiter
         acc 0]
    (if (center-of-mass? orbiter)
      acc
      (recur (orbital-parents orbiter)
             (inc acc)))))

(defn file->orbit-count
  ([filename]
   (let [orbital-parents (file->orbital-parents filename)
         count-fn (partial count-orbits orbital-parents)]
     (->> (keys orbital-parents)
          (map count-fn)
          (reduce +))))
  ([filename orbiter]
   (count-orbits (file->orbital-parents filename) orbiter)))

(defn part-1 []
  (println 
   "Day 6 - Part 1 - Count of all orbits and suborbits in system"
   (file->orbit-count "data/year_2019/day_6.input")))

(defn ancestory [orbital-parents orbiter]
  (loop [orbiter orbiter
         ancestors (list)]
    (if (center-of-mass? orbiter)
      (conj ancestors orbiter)
      (recur (orbital-parents orbiter)
             (conj ancestors orbiter)))))

(defn common-ancestor [ancestory-1 ancestory-2]
  (->> (map vector ancestory-1 ancestory-2)
       (take-while #(apply = %))
       last
       first))

(defn file->transfer-count [filename]
  (let [orbital-parents (file->orbital-parents filename)
        ancestory-1 (ancestory orbital-parents "YOU")
        ancestory-2 (ancestory orbital-parents "SAN")
        ancestor (common-ancestor ancestory-1 ancestory-2)]
    (+ (count (drop-while #(not= % ancestor) ancestory-1))
       (count (drop-while #(not= % ancestor) ancestory-2))
       -4 ;; 4 unnecessary nodes: common-ancestor (twice), SAN, YOU
       )))

(defn part-2 []
  (println
   "Day 6 - Part 2 - Number of transfers between YOU and SAN:"
   (file->transfer-count "data/year_2019/day_6.input")))
