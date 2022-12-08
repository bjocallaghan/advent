(ns net.bjoc.advent.year-2022.day-8
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.util.misc :refer [zip take-until]]))

(defn sightline [matrix xy direction]
  (let [inner (fn inner [[x y] [dx dy]]
                (lazy-seq (cons [x y] (inner [(+ x dx) (+ y dy)] [dx dy]))))]
    (take-while matrix (inner xy direction))))

(defn sightlines [matrix xy]
  (map #(rest (sightline matrix xy %))
       [[0 1] [0 -1] [1 0] [-1 0]]))

(defn highest? [height heights]
  (empty? (remove #(< % height) heights)))

(defn visible-from-outside? [matrix xy]
  (let [height (matrix xy)]
    (some (partial highest? height) (->> (sightlines matrix xy)
                                         (map #(map matrix %))))))

(defn file->visible-tree-count [filename]
  (let [matrix (mtx/from-file filename)
        visible?* (partial visible-from-outside? matrix)]
    (->> (keys matrix)
         (filter visible?*)
         count)))

;;;

(defn scenic-score [matrix xy]
  (let [height (matrix xy)
        taller? #(>= % height)]
    (->> (sightlines matrix xy)
         (map #(map matrix %))
         (map #(take-until taller? %))
         (map count)
         (reduce *))))

(defn file->best-scenic-score [filename]
  (let [matrix (mtx/from-file filename)]
    (->> (keys matrix)
         (map (partial scenic-score matrix))
         (apply max))))

;;;

(advent/defrunner 1 file->visible-tree-count "Number of visible trees")
(advent/defrunner 2 file->best-scenic-score "Best scenic score")
