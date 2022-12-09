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

(defn best-scenic-score [matrix]
  (->> (keys matrix)
       (map (partial scenic-score matrix))
       (apply max)))

(def file->best-scenic-score (comp best-scenic-score mtx/from-file))

;;;

(advent/defrunner 1 file->visible-tree-count "Number of visible trees")
(advent/defrunner 2 file->best-scenic-score "Best scenic score")

;;;

(comment
  (require '[net.bjoc.advent.util.matrix-visualization :as vis])
  (import '[java.awt Color])

  (let [filename "data/year_2022/day_8.input"
        color-fn #(. Color getHSBColor 0.3 0.8 (-> % (/ 9.0) (* 0.5) (+ 0.4)))
        matrix (mtx/from-file filename)]
    (vis/write-image-file (mtx/update-all color-fn matrix)
                          "visualizations/2022_day_8_tree_heights.png"
                          :type :colors :img-size [800 800]))

  (let [filename "data/year_2022/day_8.input"
        matrix (mtx/from-file filename)
        color-fn #(if (visible-from-outside? matrix %1)
                    (. Color getHSBColor 0.3 0.8 (-> %2 (/ 9.0) (* 0.5) (+ 0.4)))
                    (. Color lightGray))]
    (vis/write-image-file (mtx/update-all-indexed color-fn matrix)
                          "visualizations/2022_day_8_visible_trees.png"
                          :type :colors :img-size [800 800]))

  (let [filename "data/year_2022/day_8.input"
        matrix (mtx/from-file filename)
        grade (let [squeeze #(Math/pow % 0.1)
                    best-score (best-scenic-score matrix)]
                #(-> % squeeze (/ (squeeze best-score)) (* 0.5) (+ 0.4)))
        best-viewshed? (let [best-xy (->> (sort-by second #(compare %2 %1) matrix) first first)
                             height (matrix best-xy)
                             taller? #(>= (matrix %) height)]
                         (conj (->> (sightlines matrix best-xy)
                                    (map #(take-until taller? %))
                                    (apply concat)
                                    set)
                               best-xy))
        color-fn #(. Color getHSBColor (if (best-viewshed? %1) 0.55 0.3) 0.8 (grade %2))]
    (vis/write-image-file (->> matrix
                               (mtx/update-all-indexed (fn [xy _] (scenic-score matrix xy)))
                               (mtx/update-all-indexed color-fn))
                          "visualizations/2022_day_8_scenic_scores.png"
                          :type :colors :img-size [800 800]))
  )
