(ns net.bjoc.advent.year-2022.day-8
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.util.misc :refer [zip take-until]]))

(defn sightlines [matrix [x y]]
  (let [[x-max y-max] (mtx/size matrix)]
    [(reverse (for [x (range 0 x)] [x y]))
     (for [x (range (inc x) x-max)] [x y])
     (reverse (for [y (range 0 y)] [x y]))
     (for [y (range (inc y) y-max)] [x y])]))

(defn highest? [height heights]
  (empty? (remove #(< % height) heights)))

(defn visible-from-outside? [matrix xy]
  (let [height (matrix xy)]
    (some (partial highest? height) (->> (sightlines matrix xy)
                                         (map #(map matrix %))))))

(defn file->visible-tree-count [filename]
  (let [matrix (-> filename
                   slurp
                   mtx/from-string)
        visible?* (partial visible-from-outside? matrix)]
    (->> (keys matrix)
         (filter visible?*)
         count)))

;;;

(defn scenic-score [matrix xy]
  (let [taller? #(>= % (matrix xy))]
    (->> (sightlines matrix xy)
         (map #(map matrix %))
         (map #(take-until taller? %))
         (map count)
         (reduce *))))

(defn file->best-scenic-score [filename]
  (let [matrix (-> filename
                   slurp
                   mtx/from-string)
        scenic-score* (partial scenic-score matrix)]
    (->> (keys matrix)
         (map scenic-score*)
         (apply max))))

;;;

(advent/defrunner 1 file->visible-tree-count "Number of visible trees")
(advent/defrunner 2 file->best-scenic-score "Best scenic score")
