(ns net.bjoc.advent.year-2021.day-13
  (:use [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.util.matrix-visualization :as vis]
            [net.bjoc.advent.core :as advent]))

(defn file->dots [filename]
  (->> filename
       slurp
       (#(str/split % #"\n\n"))
       first
       (#(str/split % #"\n"))
       (map #(str/split % #","))
       (map #(map parse-int %))
       (map vec)))

(defn file->instructions [filename]
  (->> filename
       slurp
       (#(str/split % #"\n\n"))
       second
       (#(str/split % #"\n"))
       (map #(let [[_ axis pos] (re-find #"(.)=(\d+)$" %)]
               {:axis (keyword axis) :pos (parse-int pos)}))))

(defn apply-instruction [dots {:keys [axis pos] :as instruction}]
  (for [[x y] dots]
    (case axis
      :x [(if (< x pos) x (- pos (- x pos))) y]
      :y [x (if (< y pos) y (- pos (- y pos)))])))

(defn file->post-first-fold-dot-count [filename]
  (let [dots (file->dots filename)
        instructions (file->instructions filename)]
    (->> (apply-instruction dots (first instructions))
         (into #{})
         count)))

;;;

(defn file->show-folded-result [filename]
  (let [dots (file->dots filename)
        instructions (file->instructions filename)
        dest-filename "visualizations/2021_day_13.png"]
    (->> (reduce apply-instruction dots instructions)
         (map (fn [xy] [xy \#]))
         (into {})
         (#(vis/write-image-file % dest-filename)))
    (format "Folded result image written to %s" dest-filename)))

;;;

(advent/defrunner 1 file->post-first-fold-dot-count "Dots visible after first fold")
(advent/defrunner 2 file->show-folded-result "Folding...")
