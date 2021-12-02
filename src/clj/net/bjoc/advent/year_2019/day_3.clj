(ns net.bjoc.advent.year-2019.day-3
  (:use [clojure.set :only [intersection]]
        [net.bjoc.advent.util.coords :only [add-coords manhattan-distance]]
        [net.bjoc.advent.util.numeric :only [int-seq]])
  (:require [clojure.string :as str]))

(defn parse-fragment [s]
  (let [[c raw-n] (rest (re-find #"([UDLR])(\d+)" s))]
    [({"U" :up "D" :down "L" :left "R" :right} c) (Integer/parseInt raw-n)]))

(defn command->path [origin-xy [direction length]]
  (map #(add-coords origin-xy %)
       (for [x (range 1 (inc length))]
         (case direction
           :right [x 0]
           :left [(- x) 0]
           :up [0 x]
           :down [0 (- x )]))))

(defn line->path [line]
  (loop [xy [0 0]
         commands (->> (str/split line #",") (map parse-fragment))
         path #{xy}]
    (if (empty? commands)
      path
      (let [new-segment (command->path xy (first commands))]
        (recur (last new-segment)
               (rest commands)
               (reduce conj path new-segment))))))

(defn file->paths [filename]
  (->> (str/split (slurp filename) #"\s+")
       (map line->path)))

(defn intersections [& paths]
  (apply intersection paths))

(defn file->closest-intersection [filename]
  (->> (file->paths filename)
       (apply intersections)
       (map manhattan-distance)
       (remove zero?)
       (apply min)))

(defn part-1 []
  (println
   "Day 3 - Part 1 - Manhattan distance of closest intersection:"
   (file->closest-intersection "data/year_2019/day_3.input")))

(defn line->path* [line]
  (loop [xy [0 0]
         dist 0
         commands (->> (str/split line #",") (map parse-fragment))
         path {xy dist}]
    (if (empty? commands)
      path
      (let [new-segment (command->path xy (first commands))
            segment-length (-> commands first second)]
        (recur (last new-segment)
               (+ dist segment-length)
               (rest commands)
               (reduce (fn [path [xy dist]]
                         (if (path xy)
                           path
                           (assoc path xy dist)))
                       path
                       (map vector new-segment (int-seq (inc dist)))))))))

(defn file->paths* [filename]
  (->> (str/split (slurp filename) #"\s+")
       (map line->path*)))

(defn file->best-intersection [filename]
  (let [[lengths-1 lengths-2] (file->paths* filename)]
    (->> (file->paths filename)
         (apply intersections)
         (map (fn [xy] (+ (lengths-1 xy) (lengths-2 xy))))
         (remove zero?)
         (apply min))))

(defn part-2 []
  (println
   "Day 3 - Part 2 - Propagation distance of best intersection:"
   (->> (file->best-intersection "data/year_2019/day_3.input"))))
