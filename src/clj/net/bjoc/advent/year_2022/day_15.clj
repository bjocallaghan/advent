(ns net.bjoc.advent.year-2022.day-15
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines find-first]]
            [net.bjoc.advent.util.coords :as coords]
            [net.bjoc.advent.util.matrix-visualization :as vis]))

;;; Fully realized, drawable. Naive.

(defn within [xy dist]
  (set
   (map (partial coords/add-coords xy)
        (set
         (apply concat
                (for [x (range (inc dist))
                      y (range (- (inc dist) x))]
                  [[x y] [(- x) y] [x (- y)] [(- x) (- y)]]))))))

(defn parse-line [line & {:keys [include-empties?] :as options}]
  (let [[a b c d] (->> (re-seq #"=(-?\d+)" line)
                       (map second)
                       (map #(Integer/parseInt %)))
        dist (coords/manhattan-distance [a b] [c d])]
    (merge
     (when include-empties?
       (reduce #(assoc %1 %2 :empty) {} (within [a b] dist)))
     {[a b] :sensor [c d] :beacon})))

(defn file->grid [filename & {:as options}]
  (->> filename
       file->lines
       (map #(parse-line % options))
       (apply merge)))

(defn file->num-empties [row filename]
  (->> (file->grid filename :include-empties? true)
       (filter (fn [[[_ y] v]] (and (= v :empty) (= y row))))
       count))

(comment
  ;; neat but useless
  (->> (file->grid "data/year_2022/day_15_example1.input" :include-empties? true)
       vis/translate-to-origin
       vis/show)
  )

;;;

(defn parse-line-2 [line & {:keys [include-empties?] :as options}]
  (let [[a b c d] (->> (re-seq #"=(-?\d+)" line)
                       (map second)
                       (map #(Integer/parseInt %)))
        dist (coords/manhattan-distance [a b] [c d])]
    [{:location [a b] :distance dist}
     {:location [c d] :distance 0}]))

(defn file->circles [filename] (mapcat parse-line-2 (file->lines filename)))

(defn consolidate-spans [spans]
  (let [inner (fn [spans [start-2 end-2]]
                (let [[start-1 end-1] (peek spans)]
                  (if (>= end-1 start-2)
                    (conj (pop spans) [start-1 (max end-1 end-2)])
                    (conj spans [start-2 end-2]))))
        sorted (sort spans)]
   (reduce inner [(first sorted)] (rest sorted))))

(defn row-spec [row circles]
  (->> circles
       (map (fn [{[x y] :location dist :distance}]
              (let [dist-from-row (Math/abs (- row y))
                    span (- dist dist-from-row)]
                (when (>= span 0)
                  [(- x span) (+ x span)]))))
       (remove nil?)
       consolidate-spans))

(defn file->num-empties-2 [row filename]
  (->> filename
       file->circles
       (row-spec row)
       (map (fn [[a b]] (- b a)))
       (reduce +)))

;;;

(defn file->tuning-frequency [limit filename]
  (let [[row [[_ a]]] (->> (for [row (range limit)]
                             (do
                               (when (zero? (mod row 40000))
                                 (println (format "%s%% complete" (quot row 40000))))
                               [row (->> filename
                                         file->circles
                                         (row-spec row))]))
                           (find-first #(not-empty (drop 1 (second %)))))]
    (+ (* 4000000 (inc a)) row)))

;;;

(advent/defrunner 1 (partial file->num-empties-2 2000000) "Empty locations")
(advent/defrunner 2 (partial file->tuning-frequency 4000000) "Tuning frequency")
